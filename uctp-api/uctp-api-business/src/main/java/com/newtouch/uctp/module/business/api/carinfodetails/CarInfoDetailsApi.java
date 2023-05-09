package com.newtouch.uctp.module.business.api.carinfodetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/5/9 17:00
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 车辆明细")
public interface CarInfoDetailsApi {
    String PREFIX = ApiConstants.PREFIX + "/uctp/car-info-details";

    @PutMapping(PREFIX + "/updateTransManage")
    @Operation(summary = "更新车辆开票的转入地")
    public CommonResult<Boolean> updateTransManage(@RequestParam(name = "carId") Long carId,
                                                   @RequestParam(name = "transManageName", required = false) String transManageName,
                                                   @RequestParam(name = "sellTransManageName", required = false) String sellTransManageName);
}
