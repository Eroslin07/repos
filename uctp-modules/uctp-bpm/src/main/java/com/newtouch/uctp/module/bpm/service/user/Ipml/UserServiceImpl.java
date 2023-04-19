package com.newtouch.uctp.module.bpm.service.user.Ipml;



import com.newtouch.uctp.module.bpm.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.bpm.dal.mysql.dept.DeptMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.user.UserMapper;
import com.newtouch.uctp.module.bpm.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;



/**
 * 用户 Service 实现类
 *
 * @author hr
 */
@Service
@Validated
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private DeptMapper deptMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateUserStatus(Long id) {
        AdminUserDO adminUserDO = userMapper.selectById(id);
        adminUserDO.setStatus(0);
        userMapper.updateById(adminUserDO);
        userMapper.updateUserExtStatus(String.valueOf(adminUserDO.getId()));
        return "更新用户状态成功";
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        AdminUserDO adminUserDO = userMapper.selectById(id);
        deptMapper.deleteDept(adminUserDO.getDeptId());
        userMapper.deleteUserExt(id);
        userMapper.deleteUser(id);

    }
}
