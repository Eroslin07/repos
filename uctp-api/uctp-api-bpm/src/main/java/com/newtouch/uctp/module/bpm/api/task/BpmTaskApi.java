package com.newtouch.uctp.module.bpm.api.task;

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
 * @date 2023/5/5 11:40
 */
@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 流程任务实例")
public interface BpmTaskApi {
    String PREFIX = ApiConstants.PREFIX + "/task";


    @PostMapping(PREFIX + "/payWaitingSubmitTask")
    @Operation(summary = "根据流程业务ID提交支付等待任务节点的任务", description = "涉及2个支付流程")
    @Parameter(name = "businessKey", description = "流程业务ID", required = true)
    @Parameter(name = "submitType", description = "提交类型（通过：pass    不通过：disagree）", required = true)
    @Parameter(name = "reason", description = "提交的审批意见，可为空", required = false)
    public CommonResult<Boolean> payWaitingSubmitTask(@RequestParam("businessKey") Long businessKey,
                                                      @RequestParam("submitType") String submitType,
                                                      @RequestParam(name = "reason", required = false) String reason);
}
