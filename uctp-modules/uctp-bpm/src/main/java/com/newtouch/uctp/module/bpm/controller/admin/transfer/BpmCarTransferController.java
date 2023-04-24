package com.newtouch.uctp.module.bpm.controller.admin.transfer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.service.transfer.BpmCarTransferService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * @author helong
 * @date 2023/4/24 14:58
 */
@Tag(name =  "管理后台 - 二手车过户流程操作")
@RestController
@RequestMapping("/bpm/car/transfer")
@Validated
public class BpmCarTransferController {

    @Resource
    private BpmCarTransferService bpmCarTransferService;

    @PostMapping("/createTransferBpm")
    @Operation(summary = "根据车辆ID发起过户流程")
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "1024")
    @Parameter(name = "procDefKey", description = "收车/卖车过户流程标识", required = true, example = "SCGH")
    public CommonResult<String> createTransferBpm(@RequestParam("carId") Long carId, @RequestParam("procDefKey") String procDefKey){
        String businessKey = bpmCarTransferService.createTransferBpm(carId, procDefKey);
        return success(businessKey);
    }
}
