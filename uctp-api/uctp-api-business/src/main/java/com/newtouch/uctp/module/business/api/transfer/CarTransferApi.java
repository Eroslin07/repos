package com.newtouch.uctp.module.business.api.transfer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/4/24 14:34
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 车辆过户")
public interface CarTransferApi {
    String PREFIX = ApiConstants.PREFIX + "/uctp/car-info";

    @GetMapping(PREFIX + "/getTransferInfo")
    @Operation(summary = "根据车辆ID获取流程所需的过户信息")
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "1024")
    public CommonResult<JSONObject> getTransferInfo(@RequestParam("carId") Long carId, @RequestParam("procDefKey") String procDefKey);


    @GetMapping(PREFIX + "/getForwardInvoiceInfo")
    @Operation(summary = "根据合同ID获取流程所需的正向二手车发票信息")
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "32223")
    public CommonResult<JSONObject> getForwardInvoiceInfo(@RequestParam("contractId") Long contractId);


    @GetMapping(PREFIX + "/getForwardInvoiceInfo")
    @Operation(summary = "根据合同ID获取流程所需的反向二手车发票信息")
    @Parameter(name = "contractId", description = "合同ID", required = true, example = "22344553")
    public CommonResult<JSONObject> getReverseInvoiceInfo(@RequestParam("contractId") Long contractId);

}
