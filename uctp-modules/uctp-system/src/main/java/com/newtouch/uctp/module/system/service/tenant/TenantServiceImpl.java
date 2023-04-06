package com.newtouch.uctp.module.system.service.tenant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.util.collection.CollectionUtils;
import com.newtouch.uctp.framework.common.util.date.DateUtils;
import com.newtouch.uctp.framework.tenant.config.TenantProperties;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import com.newtouch.uctp.module.system.controller.admin.tenant.vo.tenant.TenantCreateReqVO;
import com.newtouch.uctp.module.system.controller.admin.tenant.vo.tenant.TenantExportReqVO;
import com.newtouch.uctp.module.system.controller.admin.tenant.vo.tenant.TenantPageReqVO;
import com.newtouch.uctp.module.system.controller.admin.tenant.vo.tenant.TenantUpdateReqVO;
import com.newtouch.uctp.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import com.newtouch.uctp.module.system.convert.tenant.TenantConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.dal.dataobject.permission.MenuDO;
import com.newtouch.uctp.module.system.dal.dataobject.permission.RoleDO;
import com.newtouch.uctp.module.system.dal.dataobject.tenant.TenantDO;
import com.newtouch.uctp.module.system.dal.dataobject.tenant.TenantPackageDO;
import com.newtouch.uctp.module.system.dal.mysql.tenant.TenantMapper;
import com.newtouch.uctp.module.system.enums.dept.DeptIdEnum;
import com.newtouch.uctp.module.system.enums.dept.MarketTenantDeptEnum;
import com.newtouch.uctp.module.system.enums.permission.RoleCodeEnum;
import com.newtouch.uctp.module.system.enums.permission.RoleTypeEnum;
import com.newtouch.uctp.module.system.enums.tenant.TenantPackageTypeEnum;
import com.newtouch.uctp.module.system.service.dept.DeptService;
import com.newtouch.uctp.module.system.service.permission.MenuService;
import com.newtouch.uctp.module.system.service.permission.PermissionService;
import com.newtouch.uctp.module.system.service.permission.RoleService;
import com.newtouch.uctp.module.system.service.tenant.handler.TenantInfoHandler;
import com.newtouch.uctp.module.system.service.tenant.handler.TenantMenuHandler;
import com.newtouch.uctp.module.system.service.user.AdminUserService;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.system.enums.ErrorCodeConstants.*;
import static java.util.Collections.singleton;

/**
 * 租户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class TenantServiceImpl implements TenantService {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false) // 由于 uctp.tenant.enable 配置项，可以关闭多租户的功能，所以这里只能不强制注入
    private TenantProperties tenantProperties;

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private TenantPackageService tenantPackageService;
    @Resource
    @Lazy // 延迟，避免循环依赖报错
    private AdminUserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private DeptService deptService;

    @Override
    public List<Long> getTenantIdList() {
        List<TenantDO> tenants = tenantMapper.selectList();
        return CollectionUtils.convertList(tenants, TenantDO::getId);
    }

    @Override
    public void validTenant(Long id) {
        TenantDO tenant = getTenant(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        if (tenant.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_DISABLE, tenant.getName());
        }
        if (DateUtils.isExpired(tenant.getExpireTime())) {
            throw exception(TENANT_EXPIRE, tenant.getName());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTenant(TenantCreateReqVO createReqVO) {
        // 校验套餐被禁用
        TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(createReqVO.getPackageId());
        if (!Objects.equals(createReqVO.getType(), tenantPackage.getType())) {
            throw exception(TENANT_TYPE_NOT_EQ);
        }
        //根据租户名校验租户是否被创建
        if (ObjectUtil.isNotNull(tenantMapper.selectByName(StringUtils.trimWhitespace(createReqVO.getName())))) {
            throw exception(TENANT_NAME_CHECK, createReqVO.getName());
        }

        // 创建租户
        TenantDO tenant = TenantConvert.INSTANCE.convert(createReqVO);
        tenantMapper.insert(tenant);

        TenantUtils.execute(tenant.getId(), () -> {
            //创建部门
            Long deptId = createDept(tenant);
            // 创建角色
            Long roleId = createRole(tenantPackage);
            // 创建用户，并分配角色
            Long userId = createUser(deptId, roleId, createReqVO);
            // 修改租户的管理员
            tenantMapper.updateById(new TenantDO().setId(tenant.getId()).setContactUserId(userId));
        });
        return tenant.getId();
    }

    private Long createUser(Long deptId, Long roleId, TenantCreateReqVO createReqVO) {
        // 创建用户
        UserCreateReqVO userCreateReqVO = TenantConvert.INSTANCE.convert02(createReqVO);
        userCreateReqVO.setDeptId(deptId);
        Long userId = userService.createUser(userCreateReqVO);
        // 分配角色
        permissionService.assignUserRole(userId, singleton(roleId));
        return userId;
    }

    private Long createRole(TenantPackageDO tenantPackage) {
        // 创建角色
        RoleCreateReqVO reqVO = new RoleCreateReqVO();
        reqVO.setName(RoleCodeEnum.TENANT_ADMIN.getName()).setCode(RoleCodeEnum.TENANT_ADMIN.getCode())
                .setSort(0).setRemark("系统自动生成");
        Long roleId = roleService.createRole(reqVO, RoleTypeEnum.SYSTEM.getType());
        // 分配权限
        permissionService.assignRoleMenu(roleId, tenantPackage.getMenuIds());
        return roleId;
    }

    private Long createDept(TenantDO tenant) {
        if (ObjectUtil.equals(TenantPackageTypeEnum.MARKET.getType(), tenant.getType())) {
            // 以“租户名称”作为市场名称，即作为部门（一级）名称
            DeptDO tenantDept = new DeptDO();
            tenantDept.setName(tenant.getName());
            tenantDept.setParentId(DeptIdEnum.ROOT.getId());
            tenantDept.setTenantId(tenant.getId());
            tenantDept.setAttr(0);
            tenantDept.setSort(1);
            tenantDept.setStatus(0);
            Long tenantDeptId = deptService.createTenantDept(tenantDept);
            // 创建默认二级组织：商户方
            DeptDO merchantDept = new DeptDO();
            merchantDept.setName(MarketTenantDeptEnum.MERCHANT.getDesc());
            merchantDept.setParentId(tenantDeptId);
            merchantDept.setTenantId(tenant.getId());
            merchantDept.setAttr(MarketTenantDeptEnum.MERCHANT.getType());
            merchantDept.setSort(2);
            merchantDept.setStatus(0);
            deptService.createTenantDept(merchantDept);
            // 创建默认二级组织：市场方
            DeptDO marketDept = new DeptDO();
            marketDept.setName(MarketTenantDeptEnum.MARKET.getDesc());
            marketDept.setParentId(tenantDeptId);
            marketDept.setTenantId(tenant.getId());
            marketDept.setAttr(MarketTenantDeptEnum.MARKET.getType());
            marketDept.setSort(1);
            marketDept.setStatus(0);
            return deptService.createTenantDept(marketDept);
        }
        else if (ObjectUtil.equals(TenantPackageTypeEnum.MERCHANT.getType(), tenant.getType())) {
            DeptDO tenantDept = new DeptDO();
            tenantDept.setName(tenant.getName());
            tenantDept.setParentId(DeptIdEnum.ROOT.getId());
            tenantDept.setTenantId(tenant.getId());
            tenantDept.setAttr(MarketTenantDeptEnum.MERCHANT.getType());
            tenantDept.setSort(1);
            tenantDept.setStatus(0);
            return deptService.createTenantDept(tenantDept);
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTenant(TenantUpdateReqVO updateReqVO) {
        // 校验存在
        TenantDO tenant = validateUpdateTenant(updateReqVO.getId());
        // 校验套餐被禁用
        TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(updateReqVO.getPackageId());
        //原先的租户属性与现在的租户属性不一致
        if (!Objects.equals(tenant.getType(), updateReqVO.getType())) {
            throw exception(TENANT_TYPE_NOT_UPDATE);
        }
        //当前租户套餐与租户属性（即：套餐属性）匹配不上
        if (!Objects.equals(updateReqVO.getType(), tenantPackage.getType())) {
            throw exception(TENANT_TYPE_NOT_EQ);
        }

        //根据租户名校验租户是否被创建
        TenantDO tenantDO = tenantMapper.selectByName(StringUtils.trimWhitespace(updateReqVO.getName()));
        if (ObjectUtil.isNotNull(tenantDO) && ObjectUtil.notEqual(tenantDO.getId(), updateReqVO.getId())) {
            throw exception(TENANT_NAME_CHECK, updateReqVO.getName());
        }

        // 更新租户
        TenantDO updateObj = TenantConvert.INSTANCE.convert(updateReqVO);
        tenantMapper.updateById(updateObj);
        // 如果套餐发生变化，则修改其角色的权限
        if (ObjectUtil.notEqual(tenant.getPackageId(), updateReqVO.getPackageId())) {
            updateTenantRoleMenu(tenant.getId(), tenantPackage.getMenuIds());
        }
        //如果租户名称发生变化
        if (ObjectUtil.notEqual(tenant.getName(), updateReqVO.getName())) {
            DeptDO deptDO = deptService.getDeptByTenantIdAndParentId(tenant.getId(), DeptIdEnum.ROOT.getId());
            deptDO.setName(updateReqVO.getName());
            deptService.updateTenantDept(deptDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTenantRoleMenu(Long tenantId, Set<Long> menuIds) {
        TenantUtils.execute(tenantId, () -> {
            // 获得所有角色
            List<RoleDO> roles = roleService.getRoleListByStatus(null);
            roles.forEach(role -> Assert.isTrue(tenantId.equals(role.getTenantId()), "角色({}/{}) 租户不匹配",
                    role.getId(), role.getTenantId(), tenantId)); // 兜底校验
            // 重新分配每个角色的权限
            roles.forEach(role -> {
                // 如果是租户管理员，重新分配其权限为租户套餐的权限
                if (Objects.equals(role.getCode(), RoleCodeEnum.TENANT_ADMIN.getCode())) {
                    permissionService.assignRoleMenu(role.getId(), menuIds);
                    log.info("[updateTenantRoleMenu][租户管理员({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), menuIds);
                    return;
                }
                // 如果是其他角色，则去掉超过套餐的权限
                Set<Long> roleMenuIds = permissionService.getRoleMenuIds(role.getId());
                roleMenuIds = CollUtil.intersectionDistinct(roleMenuIds, menuIds);
                permissionService.assignRoleMenu(role.getId(), roleMenuIds);
                log.info("[updateTenantRoleMenu][角色({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), roleMenuIds);
            });
        });
    }

    @Override
    public void deleteTenant(Long id) {
        // 校验存在
        validateUpdateTenant(id);
        // 删除
        tenantMapper.deleteById(id);
    }

    private TenantDO validateUpdateTenant(Long id) {
        TenantDO tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        // 内置租户，不允许删除
        if (isSystemTenant(tenant)) {
            throw exception(TENANT_CAN_NOT_UPDATE_SYSTEM);
        }
        return tenant;
    }

    @Override
    public TenantDO getTenant(Long id) {
        return tenantMapper.selectById(id);
    }

    @Override
    public List getTenantList() {
        return tenantMapper.selectList();
    }

    @Override
    public PageResult<TenantDO> getTenantPage(TenantPageReqVO pageReqVO) {
        return tenantMapper.selectPage(pageReqVO);
    }

    @Override
    public List<TenantDO> getTenantList(TenantExportReqVO exportReqVO) {
        return tenantMapper.selectList(exportReqVO);
    }

    @Override
    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectByName(name);
    }

    @Override
    public Long getTenantCountByPackageId(Long packageId) {
        return tenantMapper.selectCountByPackageId(packageId);
    }

    @Override
    public List<TenantDO> getTenantListByPackageId(Long packageId) {
        return tenantMapper.selectListByPackageId(packageId);
    }

    @Override
    public void handleTenantInfo(TenantInfoHandler handler) {
        // 如果禁用，则不执行逻辑
        if (isTenantDisable()) {
            return;
        }
        // 获得租户
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        // 执行处理器
        handler.handle(tenant);
    }

    @Override
    public void handleTenantMenu(TenantMenuHandler handler) {
        // 如果禁用，则不执行逻辑
        if (isTenantDisable()) {
            return;
        }
        // 获得租户，然后获得菜单
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        Set<Long> menuIds;
        if (isSystemTenant(tenant)) { // 系统租户，菜单是全量的
            menuIds = CollectionUtils.convertSet(menuService.getMenuList(), MenuDO::getId);
        } else {
            menuIds = tenantPackageService.getTenantPackage(tenant.getPackageId()).getMenuIds();
        }
        // 执行处理器
        handler.handle(menuIds);
    }

    private static boolean isSystemTenant(TenantDO tenant) {
        return Objects.equals(tenant.getPackageId(), TenantDO.PACKAGE_ID_SYSTEM);
    }

    private boolean isTenantDisable() {
        return tenantProperties == null || Boolean.FALSE.equals(tenantProperties.getEnable());
    }

}
