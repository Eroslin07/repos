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
 * @date 2023/5/6 12:39
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 根据合同处理商户的保证金")
public interface MerchantMoneyApi {

    String PREFIX = ApiConstants.PREFIX + "/merchant/money";

    @PostMapping(PREFIX + "/reserveCash")
    @Operation(summary = "收车时，预占保证金")
    @Parameter(name = "contractId", description = "（契约锁）收车合同ID", example = "3089090553137136296", required = true)
    public CommonResult<Boolean> reserveCash(@RequestParam("contractId") Long contractId);

    @PostMapping(PREFIX + "/releaseCash")
    @Operation(summary = "收车中，释放保证金")
    @Parameter(name = "contractId", description = "（契约锁）收车合同ID", example = "3089090553137136296", required = true)
    public CommonResult<Boolean> releaseCash(@RequestParam("contractId") Long contractId);

    @PostMapping(PREFIX + "/deductionCash")
    @Operation(summary = "收车时，实占保证金")
    @Parameter(name = "contractId", description = "（契约锁）收车合同ID", example = "3089090553137136296", required = true)
    public CommonResult<Boolean> deductionCash(@RequestParam("contractId") Long contractId);

    @PostMapping(PREFIX + "/recorded")
    @Operation(summary = "卖车过户完成时，分账")
    @Parameter(name = "carId", description = "车辆ID", example = "3089090553137136296", required = true)
    public CommonResult<Boolean> recorded(@RequestParam("carId") Long carId);
}
