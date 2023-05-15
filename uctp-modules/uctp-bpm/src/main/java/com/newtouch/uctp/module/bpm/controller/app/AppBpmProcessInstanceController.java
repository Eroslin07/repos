package com.newtouch.uctp.module.bpm.controller.app;

import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.business.api.file.notice.NoticeApi;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceByKeyReqDTO;
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
    @Resource
    private NoticeApi noticeApi;

    @PostMapping("/v3/create")
    @Operation(summary = "根据流程定义标识（业务类型）新建流程")
    public CommonResult<String> createProcessInstanceByKey(@Valid @RequestBody BpmProcessInstanceByKeyReqDTO createReqVO) {
        return success(processInstanceService.createProcessInstanceByKey(null, createReqVO.getProcDefKey(), createReqVO.getVariables()));
    }

    @PostMapping("/saveTaskNotice")
    @Operation(summary = "保存消息")
    public CommonResult saveTaskNotice(@RequestParam String type, @RequestParam String contentType, @RequestParam String reason, @RequestBody BpmFormResVO infoVO){
        return noticeApi.saveTaskNotice(type,contentType,reason,infoVO);
    }
}
