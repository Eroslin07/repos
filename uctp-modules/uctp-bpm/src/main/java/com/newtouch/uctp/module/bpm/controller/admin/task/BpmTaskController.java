package com.newtouch.uctp.module.bpm.controller.admin.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.*;
import com.newtouch.uctp.module.bpm.service.task.BpmTaskService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name =  "管理后台 - 流程任务实例")
@RestController
@RequestMapping("/bpm/task")
@Validated
public class BpmTaskController {

    @Resource
    private BpmTaskService taskService;

    @GetMapping("todo-page")
    @Operation(summary = "获取 Todo 待办任务分页")
    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<PageResult<BpmTaskTodoPageItemRespVO>> getTodoTaskPage(@Valid BpmTaskTodoPageReqVO pageVO) {
        return success(taskService.getTodoTaskPage(getLoginUserId(), pageVO));
    }

    @GetMapping("/v2/todo-page")
    @Operation(summary = "获取 Todo 待办任务分页")
    public CommonResult<PageResult<BpmTaskTodoRespVO>> getTodoTaskPageV2(@Valid BpmTaskTodoReqVO pageVO) {
        return success(taskService.getTodoTaskPageV2(getLoginUserId(), pageVO));
    }

    @GetMapping("done-page")
    @Operation(summary = "获取 Done 已办任务分页")
    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<PageResult<BpmTaskDonePageItemRespVO>> getDoneTaskPage(@Valid BpmTaskDonePageReqVO pageVO) {
        return success(taskService.getDoneTaskPage(getLoginUserId(), pageVO));
    }

    @GetMapping("/v2/done-page")
    @Operation(summary = "获取 Done 已办任务分页")
    public CommonResult<PageResult<BpmTaskDoneRespVO>> getDoneTaskV2Page(@Valid BpmTaskDoneReqVO pageVO) {
        return success(taskService.getDoneTaskPageV2(getLoginUserId(), pageVO));
    }

    @GetMapping("/list-by-process-instance-id")
    @Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
    //@PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<List<BpmTaskRespVO>> getTaskListByProcessInstanceId(
        @RequestParam("processInstanceId") String processInstanceId) {
        return success(taskService.getTaskListByProcessInstanceId(processInstanceId));
    }

    @PutMapping("/approve")
    @Operation(summary = "通过任务")
    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/v2/approve")
    @Operation(summary = "通过任务")
    //@PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> approveTaskV2(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTaskV2(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-assignee")
    @Operation(summary = "更新任务的负责人", description = "用于【流程详情】的【转派】按钮")
    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> updateTaskAssignee(@Valid @RequestBody BpmTaskUpdateAssigneeReqVO reqVO) {
        taskService.updateTaskAssignee(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/invalid")
    @Operation(summary = "作废（终止）任务")
    public CommonResult<Boolean> invalidTask(@Valid @RequestBody BpmTaskInvalidReqVO reqVO) {
        taskService.invalidTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/getTaskFormInfo")
    @Operation(summary = "根据任务ID获取流程表单信息", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "taskId", description = "任务ID", required = true)
    public CommonResult<BpmTaskApproveFormRespVO> getTaskFormInfo(@RequestParam(name ="taskId", required = false) String taskId, @RequestParam("businessKey") String businessKey) {
        BpmTaskApproveFormRespVO bpmTaskApproveFormRespVO = taskService.getTaskFormInfo(taskId, businessKey);
        return success(bpmTaskApproveFormRespVO);
    }

    @PostMapping("/seataTest")
    public CommonResult<Boolean> seataTest() {
        taskService.testSeata();
        return success(true);
    }


}