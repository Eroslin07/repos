package com.newtouch.uctp.module.system.dal.mysql.dept;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;

@Mapper
public interface DeptMapper extends BaseMapperX<DeptDO> {

    default List<DeptDO> selectList(DeptListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeptDO>()
                .likeIfPresent(DeptDO::getName, reqVO.getName())
                .eqIfPresent(DeptDO::getStatus, reqVO.getStatus()));
    }

    default DeptDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(DeptDO::getParentId, parentId, DeptDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(DeptDO::getParentId, parentId);
    }

    default DeptDO selectByTenantIdAndParentId(Long tenantId, Long parentId) {
        return selectOne((new LambdaQueryWrapperX<DeptDO>().eq(DeptDO::getParentId, parentId).eq(DeptDO::getTenantId, tenantId)));
    }

    default DeptDO selectByTenantId(String tenantId,String type){
        return selectOne(DeptDO::getTenantId,tenantId,DeptDO::getAttr,type);
    }

    default List<DeptDO> selectDeptByParentId(Long parentId) {
        return selectList(DeptDO::getParentId, parentId);
    }
}
