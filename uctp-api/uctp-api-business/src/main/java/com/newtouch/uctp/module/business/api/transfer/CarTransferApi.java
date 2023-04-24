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
}
