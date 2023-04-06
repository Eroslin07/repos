package com.newtouch.uctp.module.system.dal.mysql.user;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.system.dal.dataobject.user.UserExtDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserExtMapper extends BaseMapperX<UserExtDO> {

    default int insertUserExt(UserExtDO user){
        return insert(user);
    }

}
