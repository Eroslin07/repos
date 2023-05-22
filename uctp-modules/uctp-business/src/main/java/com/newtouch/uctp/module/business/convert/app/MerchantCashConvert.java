package com.newtouch.uctp.module.business.convert.app;

import com.newtouch.uctp.module.business.controller.app.account.cash.vo.CashDetailRespVO;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantCashRespVo;
import com.newtouch.uctp.module.business.convert.carInfo.CarInfoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MerchantCashConvert {
    MerchantCashConvert INSTANCE = Mappers.getMapper(MerchantCashConvert.class);

     CashDetailRespVO convert(MerchantCashRespVo bean);
}
