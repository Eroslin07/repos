package com.newtouch.uctp.module.bpm.dal.mysql.test;

import org.apache.ibatis.annotations.Mapper;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.dal.dataobject.test.OrderDO;

/**
 * @author helong
 * @date 2023/4/23 13:53
 */
@Mapper
public interface OrderMapper extends BaseMapperX<OrderDO> {

}
