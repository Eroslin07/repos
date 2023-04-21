package com.newtouch.uctp.module.business.service;


import com.newtouch.uctp.module.business.dal.dataobject.user.AdminUserDO;

/**
 * 我的费用部分信息 Service 接口
 *
 * @author lc
 */
public interface DeptService {


    /**
     * 获得部门信息
     *
     * @param id
     * @return 部门实体
     */
    AdminUserDO getDept(Long id);



}
