package com.newtouch.uctp.module.bpm.dal.mysql.car;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;

/**
 * 动态表单 Mapper
 *
 * @author 风里雾里
 */
@Mapper
public interface CarInfoMapper extends BaseMapperX<CarInfoDO> {
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

    /**
     * 流程修改车辆状态
     * @param id
     * @param salesStatus
     * @param status
     * @param statusThree
     * @param bpmStatus
     * @param bpmReason
     * @return
     */
    default int  updateStatus(Long id,Integer salesStatus,Integer status,Integer statusThree,String bpmStatus,String bpmReason){
        CarInfoDO carInfoDO = selectById(id);
        carInfoDO.setSalesStatus(salesStatus);//1级状态
        carInfoDO.setStatus(status);//2级状态
        carInfoDO.setStatusThree(statusThree);//3级状态
        carInfoDO.setBpmStatus(bpmStatus);//流程状态
        carInfoDO.setBpmReason(bpmReason);//意见
        return updateById(carInfoDO);
    };

    default CarInfoDO queryCarInfo(Long id){
        return selectById(id);
    }

    @InterceptorIgnore(tenantLine = "true")
    @ResultType(CarInfoDO.class)
    @Select("select * from uctp_car_info where id=#{id} and deleted = 0")
    CarInfoDO findCarInfoByCarId(@Param("id") Long id);

}
