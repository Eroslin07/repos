package com.newtouch.uctp.module.business.dal.mysql.user;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.business.dal.dataobject.user.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 我的费用 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapperX<AdminUserDO> {

    AdminUserDO selectFAUser(Long deptId);


}
