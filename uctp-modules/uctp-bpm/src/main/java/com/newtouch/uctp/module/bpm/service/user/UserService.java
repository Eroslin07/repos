package com.newtouch.uctp.module.bpm.service.user;




/**
 * 用户 Service 接口
 *
 * @author hr
 */
public interface UserService {


    /**
     * 注册成功更新用户状态为正常
     *
     */
    String updateUserStatus(Long id);

    /**
     * 注册不通过直接删除用户数据
     *
     */
    void deleteUser(Long id);

}
