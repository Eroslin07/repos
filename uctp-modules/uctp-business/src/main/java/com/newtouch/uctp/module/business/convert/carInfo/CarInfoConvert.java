package com.newtouch.uctp.module.business.convert.carInfo;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.*;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface  CarInfoConvert {
    CarInfoConvert INSTANCE = Mappers.getMapper(CarInfoConvert.class);

    CarInfoDO convert(AppCarInfoCreateReqVO bean);

    CarInfoDO convert(AppCarInfoUpdateReqVO bean);

    AppCarInfoRespVO convert(CarInfoDO bean);

    List<AppCarInfoRespVO> convertList(List<CarInfoDO> list);

    PageResult<AppCarInfoRespVO> convertPage(PageResult<CarInfoDO> page);



    CarInfoDO convert(AppSellCarInfoReqVO reqVO);
    @Mappings({
            @Mapping(source = "carInfo.id",target = "id"),
            @Mapping(source = "carInfoDetailsDO.mileage",target = "mileage"),
            @Mapping(source = "carInfoDetailsDO.firstRegistDate",target = "firstRegistDate")
    })
    AppSellCarInfoRespVO convertSell(CarInfoDO carInfo, List<String> carPicList, List<String> drivingPicList, List<String> registerPicList,List<AppSimpleFileVO> idCardsPicList, CarInfoDetailsDO carInfoDetailsDO);

}
