package com.newtouch.uctp.module.bpm.api.openinvoice;

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
 * @date 2023/4/26 16:04
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 发起开票流程")
public interface BpmOpenInvoiceApi {
    String PREFIX = ApiConstants.PREFIX + "/car/openinvoice";

    @PostMapping(PREFIX + "/createOpenInvoiceBpm")
    @Operation(summary = "收车或卖车支付成功时，发起【开票流程】")
    @Parameter(name = "contractId", description = "合同号ID", required = true, example = "3088109266591097072")
    @Parameter(name = "procDefKey", description = "开票流程标识(收车开票-SCKP  卖车开票-MCKP)", required = true, example = "SCGH")
    public CommonResult<String> createOpenInvoiceBpm(@RequestParam("contractId") Long contractId, @RequestParam("procDefKey") String procDefKey);
}
