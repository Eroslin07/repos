package com.newtouch.uctp.module.bpm.dal.mysql.dept;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;

import com.newtouch.uctp.module.bpm.dal.dataobject.dept.DeptDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptMapper extends BaseMapperX<DeptDO> {
    //物理上的删除
    void deleteDept(Long userId);

}
