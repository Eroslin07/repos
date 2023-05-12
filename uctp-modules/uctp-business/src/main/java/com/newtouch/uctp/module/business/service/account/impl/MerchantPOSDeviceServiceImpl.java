package com.newtouch.uctp.module.business.service.account.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.graph.Network;
import com.newland.pospp.iot.lib.exceptions.BusinessException;
import com.newland.pospp.iot.lib.exceptions.NetworkException;
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
import java.util.Objects;

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
        try {
            MerchantAccountDO account = merchantAccountService.getOne(
                    new LambdaQueryWrapperX<MerchantAccountDO>()
                            .eq(MerchantAccountDO::getMerchantId, reqVO.getMerchantId())
                            .eq(MerchantAccountDO::getDeleted, Boolean.FALSE)
            );

            Assert.notNull(account, "商户信息不存在或已删除!");

            authCode = posAPIService.getAuthCode(posName);

            Assert.isTrue(StringUtils.isNotBlank(authCode), "POS授权码获取失败!");

            save(MerchantPOSDeviceDO.builder()
                    .accountNo(accountNo)
                    .merchantId(reqVO.getMerchantId())
                    .posName(posName)
                    .authCode(authCode)
                    .deviceSn(StringUtils.EMPTY)
                    .state(MerchantPOSDeviceDO.DeviceState.UNBOUND.getCode()).build());

            return authCode;
        } catch (BusinessException e) {
            log.error("获取POS机授权码异常: [{}],{}", e.getCode(), e.getMessage());
            throw new ServiceException(errorCode, "获取POS机授权码业务异常!");
        } catch (NetworkException e) {
            log.error("获取POS机授权码网络异常!");
            throw new ServiceException(errorCode, "获取POS机授权码网络异常!");
        } catch (Exception e) {
            log.error("获取POS机授权码服务异常", e.getMessage());
            throw new ServiceException(errorCode, "获取POS机授权码服务异常!");
        }
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
