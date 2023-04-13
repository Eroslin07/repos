package com.newtouch.uctp.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.newtouch.uctp.framework.common.util.collection.CollectionUtils;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;

/**
 * 部门 Service 接口
 *
 * @author 芋道源码
 */
public interface DeptService {

    /**
     * 初始化部门的本地缓存
     */
    void initLocalCache();

    /**
     * 创建部门
     *
     * @param reqVO 部门信息
     * @return 部门编号
     */
    Long createDept(DeptCreateReqVO reqVO);

    /**
     * 创建租户时，创建部门
     *
     * @param deptDO 部门信息
     * @return 部门编号
     */
    Long createTenantDept(DeptDO deptDO);

    /**
     * 更新部门
     *
     * @param reqVO 部门信息
     */
    void updateDept(DeptUpdateReqVO reqVO);

    /**
     * 更新租户时，修改部门
     *
     * @param deptDO 部门信息
     * @return 部门编号
     */
    void updateTenantDept(DeptDO deptDO);

    /**
     * 删除部门
     *
     * @param id 部门编号
     */
    void deleteDept(Long id);

    /**
     * 筛选部门列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 部门列表
     */
    List<DeptDO> getDeptList(DeptListReqVO reqVO);

    /**
     * 获得所有子部门，从缓存中
     *
     * @param parentId 部门编号
     * @param recursive 是否递归获取所有
     * @return 子部门列表
     */
    List<DeptDO> getDeptListByParentIdFromCache(Long parentId, boolean recursive);

    /**
     * 获得部门信息数组
     *
     * @param ids 部门编号数组
     * @return 部门信息数组
     */
    List<DeptDO> getDeptList(Collection<Long> ids);

    /**
     * 获得指定编号的部门 Map
     *
     * @param ids 部门编号数组
     * @return 部门 Map
     */
    default Map<Long, DeptDO> getDeptMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DeptDO> list = getDeptList(ids);
        return CollectionUtils.convertMap(list, DeptDO::getId);
    }

    /**
     * 获得部门信息
     *
     * @param id 部门编号
     * @return 部门信息
     */
    DeptDO getDept(Long id);

    DeptDO getDeptByName(String name);

    /**
     * 校验部门们是否有效。如下情况，视为无效：
     * 1. 部门编号不存在
     * 2. 部门被禁用
     *
     * @param ids 角色编号数组
     */
    void validateDeptList(Collection<Long> ids);

    /**
     * 根据租户ID和上级ID查找单条数据
     * @param tenantId
     * @param parentId
     * @return
     */
    DeptDO getDeptByTenantIdAndParentId(Long tenantId, Long parentId);

    /**
     * 根据tenantID
     * @param tenantId
     * @param type
     * @return
     */
    DeptDO selectDept(String tenantId,String type);


    DeptDO selectByParent(String tenantId,Long type);

    int insertDept(DeptDO deptDO);

    List<DeptDO> getDeptByParentId(Long parentId);
}
