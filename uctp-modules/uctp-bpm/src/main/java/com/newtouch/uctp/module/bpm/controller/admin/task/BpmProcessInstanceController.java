package com.newtouch.uctp.module.bpm.controller.admin.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.instance.*;
import com.newtouch.uctp.module.bpm.service.task.BpmProcessInstanceService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name =  "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/bpm/process-instance")
@Validated
public class BpmProcessInstanceController {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @GetMapping("/my-page")
    @Operation(summary = "获得我的实例分页列表", description = "在【我的流程】菜单中，进行调用")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<PageResult<BpmProcessInstancePageItemRespVO>> getMyProcessInstancePage(
            @Valid BpmProcessInstanceMyPageReqVO pageReqVO) {
        return success(processInstanceService.getMyProcessInstancePage(getLoginUserId(), pageReqVO));
    }

    @PostMapping("/create")
    @Operation(summary = "新建流程实例")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<String> createProcessInstance(@Valid @RequestBody BpmProcessInstanceCreateReqVO createReqVO) {
        return success(processInstanceService.createProcessInstance(getLoginUserId(), createReqVO));
    }

    @PostMapping("/v2/create")
    @Operation(summary = "新建流程实例V2")
    //@PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<String> createProcessInstanceV2(@Valid @RequestBody BpmProcessInstanceCreateReqVO createReqVO) {
        return success(processInstanceService.createProcessInstanceV2(getLoginUserId(), createReqVO));
    }

    @PostMapping("/v3/create")
    @Operation(summary = "根据流程定义标识（业务类型）新建流程")
    public CommonResult<String> createProcessInstanceByKey(@Valid @RequestBody BpmProcessInstanceByKeyReqVO createReqVO) {
        return success(processInstanceService.createProcessInstanceByKey(getLoginUserId(), createReqVO.getProcDefKey(), createReqVO.getVariables()));
    }

    @GetMapping("/get")
    @Operation(summary = "获得指定流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    //@PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<BpmProcessInstanceRespVO> getProcessInstance(@RequestParam("id") String id) {
        return success(processInstanceService.getProcessInstanceVO(id));
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "取消流程实例", description = "撤回发起的流程")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:cancel')")
    public CommonResult<Boolean> cancelProcessInstance(@Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        processInstanceService.cancelProcessInstance(getLoginUserId(), cancelReqVO);
        return success(true);
    }

    @PostMapping("/create-base-info")
    @Operation(summary = "获取流程发起前，需要提交的基本信息")
    public CommonResult<BpmCreateBaseInfoRespVO> getCreateBaseInfoPre(@Valid @RequestBody BpmCreateBaseInfoReqVO reqVO) {
        BpmCreateBaseInfoRespVO respVO = processInstanceService.getCreateBaseInfoPre(reqVO);
        return success(respVO);
    }


    @GetMapping("/get-bpm-busi-variables")
    @Operation(summary = "根据业务ID获取流程业务变量", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "businessKey", description = "业务ID", required = true)
    public CommonResult<Map<String, Object>> getBpmBusinessVariables(@RequestParam("businessKey") String businessKey) {
        return null;
    }

}