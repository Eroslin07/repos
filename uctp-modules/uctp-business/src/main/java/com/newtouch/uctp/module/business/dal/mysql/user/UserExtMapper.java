package com.newtouch.uctp.module.business.dal.mysql.user;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.QueryWrapperX;
import com.newtouch.uctp.module.business.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.business.dal.dataobject.user.UserExtDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 我的费用 Mapper
 */
@Mapper
public interface UserExtMapper extends BaseMapperX<UserExtDO> {


    default int deleteByUserId(Long userId){
        return delete(new QueryWrapperX<UserExtDO>().eq("USER_ID",userId));
    }


}
