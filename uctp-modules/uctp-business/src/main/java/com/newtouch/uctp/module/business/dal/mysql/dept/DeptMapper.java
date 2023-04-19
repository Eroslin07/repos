package com.newtouch.uctp.module.business.dal.mysql.dept;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.dal.dataobject.dept.DeptDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper extends BaseMapperX<DeptDO> {


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

    default DeptDO selectByParent(String tenantId,Long type){
        return selectOne(DeptDO::getTenantId,tenantId,DeptDO::getParentId,type);
    }

    default List<DeptDO> selectDeptByParentId(Long parentId) {
        return selectList(DeptDO::getParentId, parentId);
    }

    default List<DeptDO> selectIsExist(String name, Integer status){
        return selectList(new LambdaQueryWrapperX<DeptDO>()
                .likeIfPresent(DeptDO::getName,name)
                .eqIfPresent(DeptDO::getStatus, status));
    }

    default List<DeptDO> selectByTaxNum(String taxNum) {
        return selectList(new LambdaQueryWrapperX<DeptDO>()
                .eqIfPresent(DeptDO::getTax_num,taxNum));
    }

    default List<DeptDO> selectByName(String name) {
        return selectList(new LambdaQueryWrapperX<DeptDO>()
                .eqIfPresent(DeptDO::getName,name));
    }
}
