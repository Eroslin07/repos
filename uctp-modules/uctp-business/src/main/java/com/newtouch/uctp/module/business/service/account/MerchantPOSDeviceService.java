package com.newtouch.uctp.module.business.service.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosDevicesRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosNameReqVO;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantPOSDeviceDO;

import java.util.List;

/**
 * 商户POS机设备管理
 */
public interface MerchantPOSDeviceService extends IService<MerchantPOSDeviceDO> {

    /**
     *
     * @param reqVO
     * @return
     */
    String savePosName(PosNameReqVO reqVO);

    /**
     *
     * @param merchantId
     * @return
     */
    List<PosDevicesRespVO> getMerchantPOSDevices(Long merchantId);
}
