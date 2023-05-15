package com.newtouch.uctp.module.business.api.carInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/5/10 11:56
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 车辆管理")
public interface CarInfoApi {
    String PREFIX = ApiConstants.PREFIX + "/uctp/car-info";

    @PutMapping(PREFIX + "/updateBpmApproveInfo")
    @Operation(summary = "记录车辆的流程审批状态、审批意见（提供bpm使用）")
    @Parameter(name = "carId", description = "车辆ID", required = true)
    @Parameter(name = "bpmStatus", description = "审批状态", required = true)
    @Parameter(name = "bpmReason", description = "审批意见", required = true)
    public CommonResult<Boolean> updateBpmApproveInfo(@RequestParam("carId") Long carId,
                                                      @RequestParam("bpmStatus") String bpmStatus,
                                                      @RequestParam("bpmReason") String bpmReason);

    @PutMapping(PREFIX + "/updateCarStatus")
    @Operation(summary = "修改车辆状态（提供bpm使用）")
    @Parameter(name = "carId", description = "车辆ID", required = true)
    @Parameter(name = "salesStatus", description = "一级状态", required = true)
    @Parameter(name = "status", description = "二级状态", required = true)
    @Parameter(name = "statusThree", description = "三级状态", required = true)
    @Parameter(name = "bpmStatus", description = "审批状态", required = true)
    @Parameter(name = "bpmReason", description = "审批意见", required = true)
    public CommonResult<Boolean> updateCarStatus(@RequestParam("carId") Long carId,
                                                 @RequestParam("salesStatus") Integer salesStatus,
                                                 @RequestParam("status") Integer status,
                                                 @RequestParam("statusThree") Integer statusThree,
                                                 @RequestParam("bpmStatus") String bpmStatus,
                                                 @RequestParam("bpmReason") String bpmReason);

    /**
     * 根据收车合同ID获取发起支付失败流程的信息
     * @param contractId   收车合同ID
     * @return
     */
    @GetMapping(PREFIX + "getPayFailedCreateBpmInfo")
    CommonResult<Map<String, Object>> getPayFailedCreateBpmInfo(@RequestParam("contractId") Long contractId);
}
