package com.newtouch.uctp.module.bpm.controller.admin.openinvoice;

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
import com.newtouch.uctp.module.bpm.service.openinvoice.BpmOpenInvoiceService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * @author helong
 * @date 2023/4/26 11:03
 */
@Tag(name =  "管理后台 - 二手车开票流程操作")
@RestController
@RequestMapping("/bpm/car/openinvoice")
@Validated
public class BpmOpenInvoiceController {
    @Resource
    private BpmOpenInvoiceService bpmOpenInvoiceService;

    @PostMapping("/createOpenInvoiceBpm")
    @Operation(summary = "根据车辆ID发起过户流程")
    @Parameter(name = "contractId", description = "合同号ID", required = true, example = "3088109266591097072")
    @Parameter(name = "procDefKey", description = "收车/卖车开票流程标识", required = true, example = "SCGH")
    public CommonResult<String> createOpenInvoiceBpm(@RequestParam("contractId") Long contractId, @RequestParam("procDefKey") String procDefKey){
        String businessKey = bpmOpenInvoiceService.createOpenInvoiceBpm(contractId, procDefKey);
        return success(businessKey);
    }
}
