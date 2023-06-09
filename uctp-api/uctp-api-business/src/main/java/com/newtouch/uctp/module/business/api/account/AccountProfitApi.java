package com.newtouch.uctp.module.business.api.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.account.dto.ProfitPresentAuditDTO;
import com.newtouch.uctp.module.business.enums.ApiConstants;

@FeignClient(name = ApiConstants.NAME)
@Tag(name =  "RPC 服务 - 账户利润")
public interface AccountProfitApi {

    /**
     * 利润提现审核处理接口，当“接收任务”、“审核通过”、“审核不通过”时回调该接口改变利润提现状态
     * @param tenantId 租户id
     * @param audit 提现审核意见
     * @return
     */
    @PostMapping("/uctp/account/profit/presentAudit")
    CommonResult<String> presentAudit(@RequestHeader("tenant-id") Long tenantId,
                                @RequestBody ProfitPresentAuditDTO audit);

    @PutMapping("/release/{businessKey}")
    @Operation(summary = "提现利润释放")
    public CommonResult<String> profitRelease(@PathVariable("businessKey") String businessKey);
}
