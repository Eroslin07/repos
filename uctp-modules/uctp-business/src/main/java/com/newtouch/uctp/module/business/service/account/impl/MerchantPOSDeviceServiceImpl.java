package com.newtouch.uctp.module.business.service.account.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosDevicesRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosNameReqVO;
import com.newtouch.uctp.module.business.convert.app.POSDevicesConvert;
import com.newtouch.uctp.module.business.dal.dataobject.account.MerchantPOSDeviceDO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.mysql.MerchantPOSDeviceDOMapper;
import com.newtouch.uctp.module.business.service.account.MerchantAccountService;
import com.newtouch.uctp.module.business.service.account.MerchantPOSDeviceService;
import com.newtouch.uctp.module.business.service.bank.PosAPIService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class MerchantPOSDeviceServiceImpl extends ServiceImpl<MerchantPOSDeviceDOMapper, MerchantPOSDeviceDO> implements MerchantPOSDeviceService {

    @Resource
    private POSDevicesConvert posDevicesConvert;

    @Resource
    private PosAPIService posAPIService;

    @Resource
    private MerchantAccountService merchantAccountService;

    private static final int errorCode = GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode();


    @Override
    public String savePosName(PosNameReqVO reqVO) {
        String posName = reqVO.getPosName();
        String authCode = StringUtils.EMPTY;
        String accountNo = StringUtils.EMPTY;
        return authCode;
    }

    @Override
    public List<PosDevicesRespVO> getMerchantPOSDevices(Long merchantId) {
        List<MerchantPOSDeviceDO> list = list(new LambdaQueryWrapperX<MerchantPOSDeviceDO>()
                .eq(MerchantPOSDeviceDO::getMerchantId, merchantId)
                .eq(MerchantPOSDeviceDO::getDeleted, Boolean.FALSE)
                .eq(MerchantPOSDeviceDO::getState, MerchantPOSDeviceDO.DeviceState.BINDING.getCode()));

        return posDevicesConvert.convert(list);
    }
}
