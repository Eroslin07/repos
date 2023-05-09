package com.newtouch.uctp.module.bpm.dal.mysql.car;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDetailsDO;

/**
 * @author helong
 * @date 2023/4/21 17:13
 */
@Mapper
public interface CarInfoDetailsMapper extends BaseMapperX<CarInfoDetailsDO> {

    @Update("update uctp_car_info_details set trans_manage_name = #{transManageName},sell_trans_manage_name=#{sellTransManageName} where id=#{id}")
    int updateTransManageName(@Param("id") Long id, @Param("transManageName") String transManageName, @Param("sellTransManageName") String sellTransManageName);
}
