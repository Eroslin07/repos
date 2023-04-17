package com.newtouch.uctp.module.bpm.dal.mysql.car;


import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 动态表单 Mapper
 *
 * @author 风里雾里
 */
@Mapper
public interface CarMapper extends BaseMapperX<CarInfoDO> {
    /**
     * 更新车辆状态
     * 具体状态看原型图
     * @param carId
     * @return
     */
//    @Update("update uctp_car_info set SALES_STATUS = #{statusOne},STATUS=#{statusTwo},STATUS_THREE=#{statusThree} where ID=#{carId};")
    int updateCarStatus(@Param("statusOne") Integer statusOne,
                        @Param("statusTwo")Integer statusTwo,
                        @Param("statusThree")Integer statusThree,
                        @Param("carId")Long carId);

}
