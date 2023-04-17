package com.newtouch.uctp.module.system.dal.mysql.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.framework.mybatis.core.query.QueryWrapperX;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.UserExtDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserExtMapper extends BaseMapperX<UserExtDO> {

    default int insertUserExt(UserExtDO user){
        return insert(user);
    }

    default int deleteByUserId(Long userId){
       return delete(new QueryWrapperX<UserExtDO>().eq("USER_ID",userId));
    }

    default List<UserExtDO> selectByIDCard(String IDCard) {
        return selectList(new LambdaQueryWrapperX<UserExtDO>()
                .likeIfPresent(UserExtDO::getIdCard,IDCard));
    }
}
