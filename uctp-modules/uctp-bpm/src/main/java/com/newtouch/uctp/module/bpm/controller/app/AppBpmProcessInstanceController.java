package com.newtouch.uctp.module.bpm.controller.app;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceByKeyReqVO;
import com.newtouch.uctp.module.bpm.service.task.BpmProcessInstanceService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * @author helong
 * @date 2023/4/13 15:21
 */
@Tag(name =  "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/bpm/app/process-instance")
@Validated
public class AppBpmProcessInstanceController {
    @Resource
    private BpmProcessInstanceService processInstanceService;

    @PostMapping("/v3/create")
    @Operation(summary = "根据流程定义标识（业务类型）新建流程")
    public CommonResult<String> createProcessInstanceByKey(@Valid @RequestBody BpmProcessInstanceByKeyReqVO createReqVO) {
        return success(processInstanceService.createProcessInstanceByKey(null, createReqVO.getProcDefKey(), createReqVO.getVariables()));
    }
}
