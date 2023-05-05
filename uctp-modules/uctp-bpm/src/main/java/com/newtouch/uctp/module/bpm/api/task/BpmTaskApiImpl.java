package com.newtouch.uctp.module.bpm.api.task;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.service.task.BpmTaskService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/5/5 11:45
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class BpmTaskApiImpl implements BpmTaskApi {
    @Resource
    private BpmTaskService taskService;

    @Override
    public CommonResult<Boolean> payWaitingSubmitTask(Long businessKey, String submitType, String reason) {
        taskService.payWaitingSubmitTask(businessKey, submitType, reason);
        return success(true);
    }
}
