package com.newtouch.uctp.module.business.convert.app;

import com.newtouch.uctp.module.business.controller.app.account.cash.vo.AppTransferRespVO;
import com.newtouch.uctp.module.business.convert.carInfo.CarInfoConvert;
import com.newtouch.uctp.module.business.service.bank.response.TechAddressesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppTransferAddressConvert {

    AppTransferAddressConvert INSTANCE = Mappers.getMapper(AppTransferAddressConvert.class);


    @Mappings({
            @Mapping(source = "addressCls", target = "iosAddress"),
            @Mapping(source = "addressCls1", target = "androidAddress"),
            @Mapping(source = "address", target = "urlAddress"),
    })
    AppTransferRespVO convert(TechAddressesResponse response);
}
