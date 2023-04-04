package com.newtouch.uctp.module.business.dal.mysql;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.mapper.BaseMapperX;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsPageReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarInfoDetailsMapper extends BaseMapperX<CarInfoDetailsDO> {
    default PageResult<CarInfoDetailsDO> selectPage(CarInfoDetailsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CarInfoDetailsDO>()
                .eqIfPresent(CarInfoDetailsDO::getRevision, reqVO.getRevision())
                .eqIfPresent(CarInfoDetailsDO::getCarId, reqVO.getCarId())
                .eqIfPresent(CarInfoDetailsDO::getMileage, reqVO.getMileage())
                .eqIfPresent(CarInfoDetailsDO::getAccidentVehicle, reqVO.getAccidentVehicle())
                .eqIfPresent(CarInfoDetailsDO::getSoakingCar, reqVO.getSoakingCar())
                .eqIfPresent(CarInfoDetailsDO::getBurnCar, reqVO.getBurnCar())
                .betweenIfPresent(CarInfoDetailsDO::getFirstRegistDate, reqVO.getFirstRegistDate())
                .eqIfPresent(CarInfoDetailsDO::getDrivingLicense, reqVO.getDrivingLicense())
                .eqIfPresent(CarInfoDetailsDO::getCertificateNo, reqVO.getCertificateNo())
                .eqIfPresent(CarInfoDetailsDO::getEnergyType, reqVO.getEnergyType())
                .betweenIfPresent(CarInfoDetailsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CarInfoDetailsDO::getId));
    }
}
