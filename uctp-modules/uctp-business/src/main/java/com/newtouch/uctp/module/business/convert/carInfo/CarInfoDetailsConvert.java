package com.newtouch.uctp.module.business.convert.carInfo;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppSellCarInfoReqVO;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsRespVO;
import com.newtouch.uctp.module.business.controller.app.carinfodetails.vo.CarInfoDetailsUpdateReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CarInfoDetailsConvert {
    CarInfoDetailsConvert INSTANCE = Mappers.getMapper(CarInfoDetailsConvert.class);

    CarInfoDetailsDO convert(CarInfoDetailsCreateReqVO bean);

    CarInfoDetailsDO convert(CarInfoDetailsUpdateReqVO bean);
    CarInfoDetailsDO convert(AppSellCarInfoReqVO reqVO);

    CarInfoDetailsRespVO convert(CarInfoDetailsDO bean);


    List<CarInfoDetailsRespVO> convertList(List<CarInfoDetailsDO> list);

    PageResult<CarInfoDetailsRespVO> convertPage(PageResult<CarInfoDetailsDO> page);

}
