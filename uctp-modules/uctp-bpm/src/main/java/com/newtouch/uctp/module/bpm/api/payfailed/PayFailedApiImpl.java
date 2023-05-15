package com.newtouch.uctp.module.bpm.api.payfailed;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.payfailed.dto.PayFailedCreateBpmDTO;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.bpm.service.payfailed.PayFailedService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/5/5 11:21
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class PayFailedApiImpl implements PayFailedApi {
    @Resource
    private PayFailedService payFailedService;

    @Override
    public CommonResult<String> createBpm(PayFailedCreateBpmDTO createReqVO) {
        return success(payFailedService.createBpm(createReqVO.getContractId(), BpmDefTypeEnum.SKZH.name(), createReqVO.getVariables()));
    }

    @Override
    public CommonResult<String> createPayFailedBpm(Long contractId, String failReason) {
        return success(payFailedService.createBpm(contractId, failReason));
    }
}
