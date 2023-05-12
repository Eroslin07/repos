package com.newtouch.uctp.module.business.convert.app;

import com.newtouch.uctp.module.business.controller.app.account.vo.PosDevicesRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantPOSDeviceDO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface POSDevicesConvert {

    List<PosDevicesRespVO> convert(List<MerchantPOSDeviceDO> list);
}
