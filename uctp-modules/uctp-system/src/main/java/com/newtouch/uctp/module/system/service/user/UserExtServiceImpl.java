package com.newtouch.uctp.module.system.service.user;
import com.newtouch.uctp.module.system.dal.dataobject.user.UserExtDO;

import com.newtouch.uctp.module.system.dal.mysql.user.UserExtMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 后台用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service("userExtService")
@Slf4j
public class UserExtServiceImpl implements UserExtService {
    @Resource
    private UserExtMapper userExtMapper;


    @Override
    public int insertUser(UserExtDO user) {

        return userExtMapper.insertUserExt(user);
    }
}
