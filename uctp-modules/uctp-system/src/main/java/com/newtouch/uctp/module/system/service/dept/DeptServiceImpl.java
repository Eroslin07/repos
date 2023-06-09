package com.newtouch.uctp.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptUserUpdateReqVO;
import com.newtouch.uctp.module.system.convert.dept.DeptConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.dal.mysql.dept.DeptMapper;
import com.newtouch.uctp.module.system.enums.dept.DeptIdEnum;
import com.newtouch.uctp.module.system.mq.producer.dept.DeptProducer;
import com.newtouch.uctp.module.system.service.user.AdminUserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.system.enums.ErrorCodeConstants.*;

/**
 * 部门 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    /**
     * 部门缓存
     * key：部门编号 {@link DeptDO#getId()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<Long, DeptDO> deptCache;
    /**
     * 父部门缓存
     * key：部门编号 {@link DeptDO#getParentId()}
     * value: 直接子部门列表
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Multimap<Long, DeptDO> parentDeptCache;

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private DeptProducer deptProducer;

    @Resource
    @Lazy
    private AdminUserService adminUserService;
    /**
     * 初始化 {@link #parentDeptCache} 和 {@link #deptCache} 缓存
     */
    @Override
    @PostConstruct
    public synchronized void initLocalCache() {
        // 注意：忽略自动多租户，因为要全局初始化缓存
        TenantUtils.executeIgnore(() -> {
            // 第一步：查询数据
            List<DeptDO> depts = deptMapper.selectList();
            log.info("[initLocalCache][缓存部门，数量为:{}]", depts.size());

            // 第二步：构建缓存
            ImmutableMap.Builder<Long, DeptDO> builder = ImmutableMap.builder();
            ImmutableMultimap.Builder<Long, DeptDO> parentBuilder = ImmutableMultimap.builder();
            depts.forEach(sysRoleDO -> {
                builder.put(sysRoleDO.getId(), sysRoleDO);
                parentBuilder.put(sysRoleDO.getParentId(), sysRoleDO);
            });
            deptCache = builder.build();
            parentDeptCache = parentBuilder.build();
        });
    }

    @Override
    public Long createDept(DeptCreateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(null, reqVO.getParentId(), reqVO.getName());
        // 插入部门
        DeptDO dept = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.insert(dept);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
        return dept.getId();
    }

    @Override
    public Long createTenantDept(DeptDO deptDO) {
        // 校验正确性
        if (deptDO.getParentId() == null) {
            deptDO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(null, deptDO.getParentId(), deptDO.getName());
        // 插入部门
        deptMapper.insert(deptDO);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
        return deptDO.getId();
    }

    @Override
    public void updateDept(DeptUpdateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(reqVO.getId(), reqVO.getParentId(), reqVO.getName());
        // 更新部门
        DeptDO updateObj = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.updateById(updateObj);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
    }

    @Override
    public void updateTenantDept(DeptDO deptDO) {
        // 校验正确性
        if (deptDO.getParentId() == null) {
            deptDO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(deptDO.getId(), deptDO.getParentId(), deptDO.getName());
        // 更新部门
        deptMapper.updateById(deptDO);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
    }

    @Override
    public void deleteDept(Long id) {
        // 校验是否存在
        validateDeptExists(id);
        // 校验是否有子部门
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw exception(DEPT_EXITS_CHILDREN);
        }
        // 删除部门
        deptMapper.deleteById(id);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
    }

    @Override
    public List<DeptDO> getDeptList(DeptListReqVO reqVO) {
        return deptMapper.selectList(reqVO);
    }

    @Override
    public List<DeptDO> getDeptListByParentIdFromCache(Long parentId, boolean recursive) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        List<DeptDO> result = new ArrayList<>();
        // 递归，简单粗暴
        getDeptsByParentIdFromCache(result, parentId,
                recursive ? Integer.MAX_VALUE : 1, // 如果递归获取，则无限；否则，只递归 1 次
                parentDeptCache);
        return result;
    }

    /**
     * 递归获取所有的子部门，添加到 result 结果
     *
     * @param result 结果
     * @param parentId 父编号
     * @param recursiveCount 递归次数
     * @param parentDeptMap 父部门 Map，使用缓存，避免变化
     */
    private void getDeptsByParentIdFromCache(List<DeptDO> result, Long parentId, int recursiveCount,
                                             Multimap<Long, DeptDO> parentDeptMap) {
        // 递归次数为 0，结束！
        if (recursiveCount == 0) {
            return;
        }

        // 获得子部门
        Collection<DeptDO> depts = parentDeptMap.get(parentId);
        if (CollUtil.isEmpty(depts)) {
            return;
        }
        // 针对多租户，过滤掉非当前租户的部门
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            depts = CollUtil.filterNew(depts, dept -> tenantId.equals(dept.getTenantId()));
        }
        result.addAll(depts);

        // 继续递归
        depts.forEach(dept -> getDeptsByParentIdFromCache(result, dept.getId(),
                recursiveCount - 1, parentDeptMap));
    }

    private void validateForCreateOrUpdate(Long id, Long parentId, String name) {
        // 校验自己存在
        validateDeptExists(id);
        // 校验父部门的有效性
        validateParentDeptEnable(id, parentId);
        // 校验部门名的唯一性
        validateDeptNameUnique(id, parentId, name);
    }

    private void validateParentDeptEnable(Long id, Long parentId) {
        if (parentId == null || DeptIdEnum.ROOT.getId().equals(parentId)) {
            return;
        }
        // 不能设置自己为父部门
        if (parentId.equals(id)) {
            throw exception(DEPT_PARENT_ERROR);
        }
        // 父岗位不存在
        DeptDO dept = deptMapper.selectById(parentId);
        if (dept == null) {
            throw exception(DEPT_PARENT_NOT_EXITS);
        }
        // 父部门被禁用
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
            throw exception(DEPT_NOT_ENABLE);
        }
        // 父部门不能是原来的子部门
        List<DeptDO> children = getDeptListByParentIdFromCache(id, true);
        if (children.stream().anyMatch(dept1 -> dept1.getId().equals(parentId))) {
            throw exception(DEPT_PARENT_IS_CHILD);
        }
    }

    private void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw exception(DEPT_NOT_FOUND);
        }
    }

    private void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO menu = deptMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
    }

    @Override
    public List<DeptDO> getDeptList(Collection<Long> ids) {
        return deptMapper.selectBatchIds(ids);
    }

    @Override
    public DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public DeptDO getDeptByName(String name) {
        return deptMapper.selectOne(DeptDO::getName, name);
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得科室信息
        Map<Long, DeptDO> deptMap = getDeptMap(ids);
        // 校验
        ids.forEach(id -> {
            DeptDO dept = deptMap.get(id);
            if (dept == null) {
                throw exception(DEPT_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
                throw exception(DEPT_NOT_ENABLE, dept.getName());
            }
        });
    }

    @Override
    public DeptDO getDeptByTenantIdAndParentId(Long tenantId, Long parentId) {
        return deptMapper.selectByTenantIdAndParentId(tenantId, parentId);
    }

    public DeptDO selectDept(String tenantId,String type) {
        return deptMapper.selectByTenantId(tenantId,type);
    }

    /** 根据部门状态修改用户状态 */
    @Override
    public void updateState(DeptUserUpdateReqVO reqVO) {
        deptMapper.updateState(reqVO);
    }


    public DeptDO selectByParent(String tenantId,Long type) {
        return deptMapper.selectByParent(tenantId,type);
    }
    @Override
    public int insertDept(DeptDO deptDO) {

        return deptMapper.insert(deptDO);
    }

    @Override
    public List<DeptDO> getDeptByParentId(Long parentId) {
        return deptMapper.selectDeptByParentId(parentId);
    }


    @Override
    public List<DeptDO> selectIsExist(String name, Integer status) {
        return deptMapper.selectIsExist(name,status);
    }

    @Override
    public List<DeptDO> selectByTaxNum(String taxNum) {
        return deptMapper.selectByTaxNum(taxNum);
    }

    @Override
    public List<DeptDO> selectByName(String name) {
        return deptMapper.selectByName(name);
    }

    @Override
    public DeptDO getDeptByUserId(Long userId) {
        return deptMapper.selectOne("leader_user_id ",userId);
    }

    @Override
    public DeptDO getPlatformDept() {
        AdminUserDO user = adminUserService.getUser(SecurityFrameworkUtils.getLoginUserId());
        DeptDO deptDO = this.getDept(user.getDeptId());
        return deptMapper.selectOne("parent_id", deptDO.getParentId(), "attr", 1);
    }
}
