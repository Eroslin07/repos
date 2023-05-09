package com.newtouch.uctp.module.bpm.dal.mysql.car;

import org.apache.ibatis.annotations.*;

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

    @ResultType(CarInfoDetailsDO.class)
    @Select("select t.id,t.car_id,t.trans_manage_name,t.sell_trans_manage_name from uctp_car_info_details t where t.car_id = #{carId}")
    CarInfoDetailsDO selectByCarId(@Param("carId") Long carId);
}
