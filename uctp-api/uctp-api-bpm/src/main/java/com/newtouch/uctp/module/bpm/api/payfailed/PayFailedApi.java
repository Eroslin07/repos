package com.newtouch.uctp.module.bpm.api.payfailed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.payfailed.dto.PayFailedCreateBpmDTO;
import com.newtouch.uctp.module.bpm.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/4/28 18:34
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 支付失败流程")
public interface PayFailedApi {
    String PREFIX = ApiConstants.PREFIX + "/payfailed";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "收车支付失败时，后台调用自动创建[收车款支付失败流程]")
    public CommonResult<String> createBpm(@Valid @RequestBody PayFailedCreateBpmDTO createReqVO);

    @PostMapping(PREFIX + "/createBpm")
    @Operation(summary = "根据（契约锁）收车合同ID、支付失败原因发起支付失败流程")
    public CommonResult<String> createPayFailedBpm(@RequestParam("contractId") Long contractId,
                                          @RequestParam(name = "failReason", required = false, defaultValue = "支付侧支付失败") String failReason);
}
