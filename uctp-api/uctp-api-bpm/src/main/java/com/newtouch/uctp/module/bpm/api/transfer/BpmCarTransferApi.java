package com.newtouch.uctp.module.bpm.api.transfer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/4/26 15:04
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 发起过户流程")
public interface BpmCarTransferApi {
    String PREFIX = ApiConstants.PREFIX + "/bpm/car/transfer";

    @PostMapping(PREFIX + "/createTransferBpm")
    @Operation(summary = "根据车辆ID发起过户流程")
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "1024")
    @Parameter(name = "procDefKey", description = "收车/卖车过户流程标识", required = true, example = "SCGH")
    CommonResult<String> createTransferBpm(@RequestParam("carId") Long carId, @RequestParam("procDefKey") String procDefKey);
}
