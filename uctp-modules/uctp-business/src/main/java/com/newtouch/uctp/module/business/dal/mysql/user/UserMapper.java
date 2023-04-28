package com.newtouch.uctp.module.business.dal.mysql.user;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.tenant.core.aop.TenantIgnore;
import com.newtouch.uctp.module.business.dal.dataobject.user.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * 我的费用 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapperX<AdminUserDO> {

    @TenantIgnore
    @Select("select * from system_users where id = #{id} AND DELETED = 0")
    AdminUserDO findById(Long id);
}
