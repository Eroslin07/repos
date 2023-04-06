package com.newtouch.uctp.module.system.service.tenant;

import cn.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.system.controller.admin.tenant.vo.packages.TenantPackageCreateReqVO;
import com.newtouch.uctp.module.system.controller.admin.tenant.vo.packages.TenantPackagePageReqVO;
import com.newtouch.uctp.module.system.controller.admin.tenant.vo.packages.TenantPackageUpdateReqVO;
import com.newtouch.uctp.module.system.convert.tenant.TenantPackageConvert;
import com.newtouch.uctp.module.system.dal.dataobject.tenant.TenantDO;
import com.newtouch.uctp.module.system.dal.dataobject.tenant.TenantPackageDO;
import com.newtouch.uctp.module.system.dal.mysql.tenant.TenantPackageMapper;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.system.enums.ErrorCodeConstants.*;

/**
 * 租户套餐 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TenantPackageServiceImpl implements TenantPackageService {

    @Resource
    private TenantPackageMapper tenantPackageMapper;

    @Resource
    @Lazy // 避免循环依赖的报错
    private TenantService tenantService;

    @Override
    public Long createTenantPackage(TenantPackageCreateReqVO createReqVO) {
        // 插入
        TenantPackageDO tenantPackage = TenantPackageConvert.INSTANCE.convert(createReqVO);
        tenantPackageMapper.insert(tenantPackage);
        // 返回
        return tenantPackage.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTenantPackage(TenantPackageUpdateReqVO updateReqVO) {
        // 校验存在
        TenantPackageDO tenantPackage = validateTenantPackageExists(updateReqVO.getId());
        // 更新
        TenantPackageDO updateObj = TenantPackageConvert.INSTANCE.convert(updateReqVO);
        List<TenantDO> tenants = tenantService.getTenantListByPackageId(tenantPackage.getId());
        if (!CollectionUtils.isEmpty(tenants) && !Objects.equals(tenantPackage.getType(), updateObj.getType())) {
            throw exception(TENANT_PACKAGE_TYPE_NOT_UPDATE);
        }
        tenantPackageMapper.updateById(updateObj);
        // 如果菜单发生变化，则修改每个租户的菜单
        if (!CollUtil.isEqualList(tenantPackage.getMenuIds(), updateReqVO.getMenuIds())) {
            tenants.forEach(tenant -> tenantService.updateTenantRoleMenu(tenant.getId(), updateReqVO.getMenuIds()));
        }
    }

    @Override
    public void deleteTenantPackage(Long id) {
        // 校验存在
        validateTenantPackageExists(id);
        // 校验正在使用
        validateTenantUsed(id);
        // 删除
        tenantPackageMapper.deleteById(id);
    }

    private TenantPackageDO validateTenantPackageExists(Long id) {
        TenantPackageDO tenantPackage = tenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        return tenantPackage;
    }

    private void validateTenantUsed(Long id) {
        if (tenantService.getTenantCountByPackageId(id) > 0) {
            throw exception(TENANT_PACKAGE_USED);
        }
    }

    @Override
    public TenantPackageDO getTenantPackage(Long id) {
        return tenantPackageMapper.selectById(id);
    }

    @Override
    public PageResult<TenantPackageDO> getTenantPackagePage(TenantPackagePageReqVO pageReqVO) {
        return tenantPackageMapper.selectPage(pageReqVO);
    }

    @Override
    public TenantPackageDO validTenantPackage(Long id) {
        TenantPackageDO tenantPackage = tenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        if (tenantPackage.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_PACKAGE_DISABLE, tenantPackage.getName());
        }
        return tenantPackage;
    }

    @Override
    public List<TenantPackageDO> getTenantPackageListByStatus(Integer status) {
        return tenantPackageMapper.selectListByStatus(status);
    }

    @Override
    public List<TenantPackageDO> getTenantPackageListByStatusAndType(Integer status, Integer type) {
        return tenantPackageMapper.selectList(new LambdaQueryWrapper<TenantPackageDO>().eq(TenantPackageDO::getStatus, status).eq(TenantPackageDO::getType, type));
    }

}
