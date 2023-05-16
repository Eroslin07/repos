package com.newtouch.uctp.module.business.api.contract;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.contract.MerchantMoneyService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/5/6 12:40
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class MerchantMoneyApiImpl implements MerchantMoneyApi {

    @Resource
    private MerchantMoneyService merchantMoneyService;

    @Override
    public CommonResult<Boolean> reserveCash(Long contractId) {
        return success(merchantMoneyService.reserveCash(contractId));
    }

    @Override
    public CommonResult<Boolean> releaseCash(Long contractId) {
        return success(merchantMoneyService.releaseCash(contractId));
    }

    @Override
    public CommonResult<Boolean> deductionCash(Long contractId) {
        return success(merchantMoneyService.deductionCash(contractId));
    }

    @Override
    public CommonResult<Boolean> recorded(Long carId) {
        return success(merchantMoneyService.recorded(carId));
    }
}
