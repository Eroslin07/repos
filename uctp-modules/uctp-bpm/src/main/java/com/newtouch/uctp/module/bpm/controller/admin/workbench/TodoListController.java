package com.newtouch.uctp.module.bpm.controller.admin.workbench;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.BpmTaskApproveReqVO;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.BpmTaskRejectReqVO;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.BpmTaskUpdateAssigneeReqVO;
import com.newtouch.uctp.module.bpm.controller.admin.workbench.vo.*;
import com.newtouch.uctp.module.bpm.service.workbench.WorkbenchTowService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name =  "管理后台 - 流程任务实例")
@RestController
@RequestMapping("/bpm/workbench")
@Validated
public class TodoListController {

    @Resource
    private WorkbenchTowService workbenchTowService;

    @GetMapping("/toDoList-page")
    @Operation(summary = "获取 Todo 待办任务分页")
    @PreAuthorize("@ss.hasPermission('bpm:workbench:query')")
    public CommonResult<PageResult<TodoListPageItemRespVO>> getTodoListPage(@Valid TodoListPageReqVO pageVO) {
        return success(workbenchTowService.getTodoListPage(getLoginUserId(), pageVO));
    }

    @GetMapping("/finished-page")
    @Operation(summary = "获取 Done 已办任务分页")
    @PreAuthorize("@ss.hasPermission('bpm:workbench:query')")
    public CommonResult<PageResult<FinishedPageItemRespVO>> getFinishedPage(@Valid FinishedPageReqVO pageVO) {
        return success(workbenchTowService.getFinishedPage(getLoginUserId(), pageVO));
    }
    @GetMapping("/list-by-process-instance-id")
    @Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
    @PreAuthorize("@ss.hasPermission('bpm:workbench:query')")
    public CommonResult<List<WorkbenchRespVO>> getTaskListByProcessInstanceId(
            @RequestParam("processInstanceId") String processInstanceId) {
        return success(workbenchTowService.getTaskListByProcessInstanceId(processInstanceId));
    }

    @PutMapping("/approve")
    @Operation(summary = "通过任务")
    @PreAuthorize("@ss.hasPermission('bpm:workbench:update')")
    public CommonResult<Boolean> approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        workbenchTowService.approveTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
    @PreAuthorize("@ss.hasPermission('bpm:workbench:update')")
    public CommonResult<Boolean> rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        workbenchTowService.rejectTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-assignee")
    @Operation(summary = "更新任务的负责人", description = "用于【流程详情】的【转派】按钮")
    @PreAuthorize("@ss.hasPermission('bpm:workbench:update')")
    public CommonResult<Boolean> updateTaskAssignee(@Valid @RequestBody BpmTaskUpdateAssigneeReqVO reqVO) {
        workbenchTowService.updateTaskAssignee(getLoginUserId(), reqVO);
        return success(true);
    }
}