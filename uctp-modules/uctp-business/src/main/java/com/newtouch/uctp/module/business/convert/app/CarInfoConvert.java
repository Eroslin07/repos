package com.newtouch.uctp.module.business.convert.app;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoRespVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppCarInfoUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import org.mapstruct.Mapper;
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

//    List<AppCarInfoExcelVO> convertList02(List<CarInfoDO> list);
}
