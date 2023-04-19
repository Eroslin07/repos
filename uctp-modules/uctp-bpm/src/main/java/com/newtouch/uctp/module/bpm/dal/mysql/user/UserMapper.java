package com.newtouch.uctp.module.bpm.dal.mysql.user;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.dal.dataobject.notice.NoticeInfoDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.user.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 我的费用 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapperX<AdminUserDO> {


    void updateUserExtStatus(String userId);

    void deleteUserExt(Long userId);

    void deleteUser(Long userId);


}
