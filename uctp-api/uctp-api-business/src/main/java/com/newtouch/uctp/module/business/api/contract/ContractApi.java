package com.newtouch.uctp.module.business.api.contract;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/5/9 19:27
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 合同管理")
public interface ContractApi {
    String PREFIX = ApiConstants.PREFIX + "/uctp/contract";

    @PostMapping(PREFIX + "/contract/invalid")
    @Operation(summary = "合同作废")
    @Parameter(name = "id", description = "合同id", required = true, example = "1024")
    @Parameter(name = "reason", description = "合同作废原因", required = true, example = "合同作废")
    public CommonResult<Boolean> contractInvalid(@RequestParam Long id,
                                               @RequestParam String reason);
}
