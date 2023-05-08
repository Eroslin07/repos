package com.newtouch.uctp.module.bpm.service.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.util.date.DateUtils;
import com.newtouch.uctp.framework.common.util.number.NumberUtils;
import com.newtouch.uctp.framework.common.util.object.PageUtils;
import com.newtouch.uctp.framework.mybatis.core.util.MyBatisUtils;
import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.task.*;
import com.newtouch.uctp.module.bpm.convert.task.BpmTaskConvert;
import com.newtouch.uctp.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.form.BpmFormMainDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.newtouch.uctp.module.bpm.dal.mysql.definition.BpmProcessDefinitionExtMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormMainMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import com.newtouch.uctp.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.newtouch.uctp.module.bpm.service.message.BpmMessageService;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.api.logger.OperateLogApi;
import com.newtouch.uctp.module.system.api.logger.dto.OperateLogCreateReqDTO;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;
import com.newtouch.uctp.module.system.api.user.dto.AdminUserRespDTO;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.common.util.collection.CollectionUtils.convertMap;
import static com.newtouch.uctp.framework.common.util.collection.CollectionUtils.convertSet;
import static com.newtouch.uctp.module.bpm.enums.ErrorCodeConstants.*;

/**
 * 流程任务实例 Service 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Slf4j
@Service
public class BpmTaskServiceImpl implements BpmTaskService {

    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private BpmTaskExtMapper taskExtMapper;
    @Resource
    private BpmMessageService messageService;
    @Resource
    private BpmFormMainMapper bpmFormMainMapper;
    @Resource
    private BpmFormDataService bpmFormDataService;
    @Resource
    private BpmProcessDefinitionExtMapper bpmProcessDefinitionExtMapper;
    @Resource
    private OperateLogApi operateLogApi;

    @Override
    public PageResult<BpmTaskTodoPageItemRespVO> getTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO) {
        // 查询待办任务
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId)) // 分配给自己
            .orderByTaskCreateTime().desc(); // 创建时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (ArrayUtil.get(pageVO.getCreateTime(), 0) != null) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[0]));
        }
        if (ArrayUtil.get(pageVO.getCreateTime(), 1) != null) {
            taskQuery.taskCreatedBefore(DateUtils.of(pageVO.getCreateTime()[1]));
        }
        // 执行查询
        List<Task> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        // 获得 ProcessInstance Map
        Map<String, ProcessInstance> processInstanceMap =
            processInstanceService.getProcessInstanceMap(convertSet(tasks, Task::getProcessInstanceId));
        // 获得 User Map
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
            convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        // 拼接结果
        return new PageResult<>(BpmTaskConvert.INSTANCE.convertList1(tasks, processInstanceMap, userMap),
            taskQuery.count());
    }

    @Override
    public PageResult<BpmTaskDonePageItemRespVO> getDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO) {
        // 查询已办任务
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery().finished() // 已完成
            .taskAssignee(String.valueOf(userId)) // 分配给自己
            .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (pageVO.getBeginCreateTime() != null) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getBeginCreateTime()));
        }
        if (pageVO.getEndCreateTime() != null) {
            taskQuery.taskCreatedBefore(DateUtils.of(pageVO.getEndCreateTime()));
        }
        // 执行查询
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        // 获得 TaskExtDO Map
        List<BpmTaskExtDO> bpmTaskExtDOs =
            taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);
        // 获得 ProcessInstance Map
        Map<String, HistoricProcessInstance> historicProcessInstanceMap =
            processInstanceService.getHistoricProcessInstanceMap(
                convertSet(tasks, HistoricTaskInstance::getProcessInstanceId));
        // 获得 User Map
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
            convertSet(historicProcessInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        // 拼接结果
        return new PageResult<>(
            BpmTaskConvert.INSTANCE.convertList2(tasks, bpmTaskExtDOMap, historicProcessInstanceMap, userMap),
            taskQuery.count());
    }

    @Override
    public List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
    }

    @Override
    public List<BpmTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId) {
        // 获得任务列表
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        // 获得 TaskExtDO Map
        List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);
        // 获得 ProcessInstance Map
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User Map
        Set<Long> userIds = convertSet(tasks, task -> NumberUtils.parseLong(task.getAssignee()));
        userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        // 获得 Dept Map
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));

        // 拼接数据
        return BpmTaskConvert.INSTANCE.convertList3(tasks, bpmTaskExtDOMap, processInstance, userMap, deptMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }

        // 完成任务，审批通过
        taskService.complete(task.getId(), instance.getProcessVariables());

        // 更新任务拓展表为通过
        taskExtMapper.updateByTaskId(
            new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.APPROVE.getResult())
                .setReason(reqVO.getReason()));
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void approveTaskV2(Long userId, BpmTaskApproveReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectOne(BpmFormMainDO::getProcInstId, instance.getId());
        if (ObjectUtil.isNotEmpty(bpmFormMainDO) && bpmFormMainDO.getStatus() != 1) {
            throw exception(PROCESS_BUSI_APPROVALING);
        }
        Map<String, Object> variables = CollectionUtils.isEmpty(reqVO.getVariables()) ? new HashMap<String, Object>() : reqVO.getVariables();
        HashMap<String, Object> formDataJsonVariable = (HashMap<String, Object>) variables.getOrDefault("formDataJson", new HashMap<String, Object>());
        String workFlowMainEntityAlias = this.bpmFormDataService.getObjectMainEntityAlias(formDataJsonVariable);
        Map<String, Object> formMainDataObject = this.bpmFormDataService.getObjectMainEntityMap(formDataJsonVariable);
        formMainDataObject.put("status", 1);
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "procInstId"), task.getProcessInstanceId());
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "submitTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(bpmFormMainDO.getSubmitTime()));
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "doneTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        HashMap<String, Object> submitFormDataJsonField = (HashMap<String, Object>) formMainDataObject.getOrDefault(this.matchMapKey(formMainDataObject, "formDataJson"), new HashMap<String, Object>());
        if (!CollectionUtils.isEmpty(submitFormDataJsonField)) {
            formMainDataObject.put(this.matchMapKey(formMainDataObject, "formDataJson"), JSON.toJSONString(submitFormDataJsonField).getBytes(StandardCharsets.UTF_8));
        } else {
            formMainDataObject.put(this.matchMapKey(formMainDataObject, "formDataJson"), null);
        }

        formDataJsonVariable.put(workFlowMainEntityAlias, formMainDataObject);
        this.bpmFormDataService.saveDataObject(bpmFormMainDO.getId(), formDataJsonVariable);

        // 完成任务，审批通过
        variables.put("formDataJson", formDataJsonVariable);
        variables.put("reason", reqVO.getReason());
        taskService.complete(task.getId(), variables);
        // 更新任务拓展表为通过
        taskExtMapper.updateByTaskId(
                new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.APPROVE.getResult())
                        .setReason(reqVO.getReason()));
    }

    private String matchMapKey(Map<String, Object> map, String key) {
        for (String k : map.keySet()) {
            if (k.toLowerCase().equals(key.toLowerCase())) {
                return k;
            }
        }

        return key;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO) {
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }

        List<UserTask> userTaskList = repositoryService.getBpmnModel(instance.getProcessDefinitionId()).getMainProcess().findFlowElementsOfType(UserTask.class, true);

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveExecutionToActivityId(task.getExecutionId(), userTaskList.get(0).getId())
                .changeState();

        // 更新流程实例为不通过
        //processInstanceService.updateProcessInstanceExtReject(instance.getProcessInstanceId(), reqVO.getReason());

        // 更新任务拓展表为不通过
        taskExtMapper.updateByTaskId(
            new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.REJECT.getResult())
                    .setEndTime(LocalDateTime.now()).setReason(reqVO.getReason()));
    }

    @Override
    public void updateTaskAssignee(Long userId, BpmTaskUpdateAssigneeReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 更新负责人
        updateTaskAssignee(task.getId(), reqVO.getAssigneeUserId());
    }

    @Override
    public void updateTaskAssignee(String id, Long userId) {
        taskService.setAssignee(id, String.valueOf(userId));
    }

    /**
     * 校验任务是否存在， 并且是否是分配给自己的任务
     *
     * @param userId 用户 id
     * @param taskId task id
     */
    private Task checkTask(Long userId, String taskId) {
        Task task = getTask(taskId);
        if (task == null) {
            throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
        }
        if (!Objects.equals(userId, NumberUtils.parseLong(task.getAssignee()))) {
            throw exception(TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF);
        }
        return task;
    }

    @Override
    public void createTaskExt(Task task) {
        BpmTaskExtDO taskExtDO =
            BpmTaskConvert.INSTANCE.convert2TaskExt(task).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        taskExtMapper.insert(taskExtDO);
    }

    @Override
    public void updateTaskExtComplete(Task task) {
        BpmTaskExtDO taskExtDO = BpmTaskConvert.INSTANCE.convert2TaskExt(task)
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()) // 不设置也问题不大，因为 Complete 一般是审核通过，已经设置
                .setEndTime(LocalDateTime.now());
        taskExtMapper.updateByTaskId(taskExtDO);
    }

    @Override
    public void updateTaskExtCancel(String taskId) {
        // 需要在事务提交后，才进行查询。不然查询不到历史的原因
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                // 可能只是活动，不是任务，所以查询不到
                HistoricTaskInstance task = getHistoricTask(taskId);
                if (task == null) {
                    return;
                }

                // 如果任务拓展表已经是完成的状态，则跳过
                BpmTaskExtDO taskExt = taskExtMapper.selectByTaskId(taskId);
                if (taskExt == null) {
                    log.error("[updateTaskExtCancel][taskId({}) 查找不到对应的记录，可能存在问题]", taskId);
                    return;
                }
                // 如果已经是最终的结果，则跳过
                if (BpmProcessInstanceResultEnum.isEndResult(taskExt.getResult())) {
                    log.error("[updateTaskExtCancel][taskId({}) 处于结果({})，无需进行更新]", taskId, taskExt.getResult());
                    return;
                }

                // 更新任务
                taskExtMapper.updateById(new BpmTaskExtDO().setId(taskExt.getId()).setResult(BpmProcessInstanceResultEnum.CANCEL.getResult())
                        .setEndTime(LocalDateTime.now()).setReason(BpmProcessInstanceDeleteReasonEnum.translateReason(task.getDeleteReason())));
            }

        });
    }

    @Override
    public void updateTaskExtAssign(Task task) {
        BpmTaskExtDO taskExtDO =
            new BpmTaskExtDO().setAssigneeUserId(NumberUtils.parseLong(task.getAssignee())).setTaskId(task.getId());
        taskExtMapper.updateByTaskId(taskExtDO);
        // 发送通知。在事务提交时，批量执行操作，所以直接查询会无法查询到 ProcessInstance，所以这里是通过监听事务的提交来实现。
        /*TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ProcessInstance processInstance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
                //AdminUserRespDTO startUser = adminUserApi.getUser(Long.valueOf(processInstance.getStartUserId())).getCheckedData();
                *//*messageService.sendMessageWhenTaskAssigned(
                    BpmTaskConvert.INSTANCE.convert(processInstance, startUser, task));*//*
            }
        });*/
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void invalidTask(Long userId, BpmTaskInvalidReqVO reqVO) {
        Task task = getTask(reqVO.getTaskId());
        if (task == null) {
            throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
        }

        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectOne(BpmFormMainDO::getProcInstId, task.getProcessInstanceId());
        if (bpmFormMainDO.getStatus() != 1) {
            throw exception(PROCESS_INSTANCE_INVALIA_FAIL_NOT_SELF);
        }
        bpmFormMainDO.setStatus(BpmProcessInstanceResultEnum.INVALID.getResult());
        bpmFormMainMapper.updateById(bpmFormMainDO);

        // 作废/终止时，业务处理
        this.invalidTaskBizHandle(reqVO, task.getProcessInstanceId());

        // 更新任务拓展表为作废/终止
        taskExtMapper.updateByTaskId(new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.INVALID.getResult())
                        .setEndTime(LocalDateTime.now()).setReason(reqVO.getReason()));
    }

    private void invalidTaskBizHandle(BpmTaskInvalidReqVO reqVO, String processInstanceId) {
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectOne(BpmFormMainDO::getProcInstId, processInstanceId);

    }

    @Override
    public PageResult<BpmTaskTodoRespVO> getTodoTaskPageV2(Long loginUserId, BpmTaskTodoReqVO pageVO) {
        Page<BpmTaskTodoRespVO> page = MyBatisUtils.buildPage(pageVO);
        bpmFormMainMapper.getBpmTaskTodo(page, pageVO, loginUserId);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PageResult<BpmTaskDoneRespVO> getDoneTaskPageV2(Long loginUserId, BpmTaskDoneReqVO pageVO) {
        Page<BpmTaskDoneRespVO> page = MyBatisUtils.buildPage(pageVO);
        bpmFormMainMapper.getBpmTaskDone(page, pageVO, loginUserId);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public BpmTaskApproveFormRespVO getTaskFormInfo(String taskId, String businessKey) {
        BpmTaskApproveFormRespVO bpmTaskApproveFormRespVO = new BpmTaskApproveFormRespVO();
        if (!StringUtils.hasText(taskId) && !StringUtils.hasText(businessKey)) {
            throw  new ServiceException(1009003003, "请传递参数：任务ID或流程业务ID");
        }

        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectById(Long.valueOf(businessKey));
        if (ObjectUtil.isNull(bpmFormMainDO)) {
            throw  new ServiceException(1009003003, "业务流程不存在，查询审批表单信息失败。");
        }
        bpmTaskApproveFormRespVO.setTaskId(taskId);
        bpmTaskApproveFormRespVO.setProcDefId(bpmFormMainDO.getProcDefId());
        bpmTaskApproveFormRespVO.setProcInstId(bpmFormMainDO.getProcInstId());
        bpmTaskApproveFormRespVO.setBusinessKey(bpmFormMainDO.getId());
        bpmTaskApproveFormRespVO.setBusiType(bpmFormMainDO.getBusiType());
        bpmTaskApproveFormRespVO.setSerialNo(bpmFormMainDO.getSerialNo());
        bpmTaskApproveFormRespVO.setTitle(bpmFormMainDO.getTitle());
        if (StringUtils.hasText(taskId)) {
            Task task = getTask(taskId);
            if (task == null) {
                throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
            }
            FlowElement flowElement = repositoryService.getBpmnModel(task.getProcessDefinitionId()).getMainProcess().getFlowElement(task.getTaskDefinitionKey());
            bpmTaskApproveFormRespVO.setNodeId(task.getTaskDefinitionKey());
            bpmTaskApproveFormRespVO.setNodeName(flowElement.getName());
        }
        if (StringUtils.hasText(bpmFormMainDO.getProcDefId())) {
            BpmProcessDefinitionExtDO bpmProcessDefinitionExtDO = bpmProcessDefinitionExtMapper.selectByProcessDefinitionId(bpmFormMainDO.getProcDefId());
            bpmTaskApproveFormRespVO.setComponentAddress(bpmProcessDefinitionExtDO.getFormCustomCreatePath());
        }

        Map<String, Object> variablesNew = new HashMap<String, Object>();
        if (StringUtils.hasText(bpmFormMainDO.getProcInstId())) {
            List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(bpmFormMainDO.getProcInstId()).orderByVariableName().desc().list();
            if (!CollectionUtils.isEmpty(historicVariableInstanceList)) {
                for (HistoricVariableInstance historicVariableInstance : historicVariableInstanceList) {
                    System.out.println(historicVariableInstance.getValue());
                    if (historicVariableInstance.getVariableName().toLowerCase().equals("formDataJson".toLowerCase())) {
                        continue;
                    }
                    variablesNew.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
                }
            }
        }

        BpmFormMainVO bpmFormMainVO = new BpmFormMainVO();
        bpmFormMainVO.setId(bpmFormMainDO.getId());
        bpmFormMainVO.setStatus(bpmFormMainDO.getStatus());
        bpmFormMainVO.setProcDefId(bpmFormMainDO.getProcDefId());
        bpmFormMainVO.setProcInstId(bpmFormMainDO.getProcInstId());
        bpmFormMainVO.setSerialNo(bpmFormMainDO.getSerialNo());
        bpmFormMainVO.setTitle(bpmFormMainDO.getTitle());
        bpmFormMainVO.setStartUserId(bpmFormMainDO.getStartUserId());
        bpmFormMainVO.setMerchantId(bpmFormMainDO.getMerchantId());
        bpmFormMainVO.setBusiType(bpmFormMainDO.getBusiType());
        bpmFormMainVO.setRemark(bpmFormMainDO.getRemark());
        bpmFormMainVO.setThirdId(bpmFormMainDO.getThirdId());
        bpmFormMainVO.setSubmitTime(bpmFormMainDO.getSubmitTime());
        bpmFormMainVO.setDoneTime(bpmFormMainDO.getDoneTime());

        if (bpmFormMainDO.getFormDataJson() == null) {
            bpmFormMainVO.setFormDataJson(new JSONObject());
        }
        else {
            bpmFormMainVO.setFormDataJson(JSON.parseObject(new String(bpmFormMainDO.getFormDataJson())));
        }
        HashMap<String, Object> formDataJsonVariable = new HashMap<>();
        Map<String, Object> formMainMap = BeanUtil.beanToMap(bpmFormMainVO, false, false);
        //formMainMap.put("thirdId", String.valueOf(bpmFormMainDO.getThirdId()));
        formMainMap.put("formDataJson", bpmFormMainVO.getFormDataJson().getInnerMap());
        formDataJsonVariable.put("formMain", formMainMap);
        variablesNew.put("formDataJson", formDataJsonVariable);
        bpmTaskApproveFormRespVO.setVariables(variablesNew);

        return bpmTaskApproveFormRespVO;
    }

    private Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).singleResult();
    }

    private HistoricTaskInstance getHistoricTask(String id) {
        return historyService.createHistoricTaskInstanceQuery().taskId(id).singleResult();
    }


    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void testSeata() {
        BpmFormMainDO bpmFormMainDO = new BpmFormMainDO();
        bpmFormMainDO.setId(IdUtil.getSnowflakeNextId());
        bpmFormMainDO.setStatus(1);
        bpmFormMainDO.setRemark("分布式事务测试");
        bpmFormMainMapper.insert(bpmFormMainDO);

        OperateLogCreateReqDTO createReqDTO = new OperateLogCreateReqDTO();
        createReqDTO.setTraceId(UUID.randomUUID().toString());
        createReqDTO.setUserId(1L);
        createReqDTO.setUserType(11);
        createReqDTO.setModule("22");
        createReqDTO.setName("helong");
        createReqDTO.setType(22);
        createReqDTO.setContent("111");
        createReqDTO.setRequestMethod("/creat");
        createReqDTO.setRequestUrl("3333");
        createReqDTO.setUserIp("localhost");
        createReqDTO.setUserAgent("3");
        createReqDTO.setJavaMethod("com.newtouch.uctp.UserController.save(...)");
        createReqDTO.setJavaMethodArgs("333");
        createReqDTO.setStartTime(LocalDateTime.now());
        createReqDTO.setDuration(1);
        createReqDTO.setResultCode(33);
        operateLogApi.createOperateLog(createReqDTO);
        if (true) {
            throw new RuntimeException("11");
        }
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void payWaitingSubmitTask(Long businessKey, String submitType, String reason) {
        if (!(ObjectUtil.equals(submitType, "pass") || ObjectUtil.equals(submitType, "disagree"))) {
            throw new RuntimeException("提交类型不支持，提交失败。");
        }
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectBpmFormMainById(businessKey);
        if (ObjectUtil.isNull(bpmFormMainDO) || ObjectUtil.notEqual(bpmFormMainDO.getStatus(), 1)) {
            throw new RuntimeException("流程已不在审批中，提交失败。");
        }
        if (ObjectUtil.equals(BpmDefTypeEnum.LRTX.name(), bpmFormMainDO.getBusiType())
                || ObjectUtil.equals(BpmDefTypeEnum.SKZH.name(), bpmFormMainDO.getBusiType())) {
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(bpmFormMainDO.getProcInstId()).active().list();
            if (CollectionUtils.isEmpty(taskList)) {
                throw new RuntimeException("未查找到[支付等待完成]的任务节点任务，提交失败。");
            }
            Process process = repositoryService.getBpmnModel(bpmFormMainDO.getProcDefId()).getMainProcess();
            String payWaitingNodeTaskId = "";
            String assignee = "";
            String nodeId = "";
            for (int i = 0; i < taskList.size(); i++) {
                TaskEntity taskEntity = (TaskEntity) taskList.get(i);
                if (ObjectUtil.equals(this.getTaskNodeExtPropByKey(process, taskEntity.getTaskDefinitionKey(), "nodeSymbol"), "payWaiting")) {
                    payWaitingNodeTaskId = taskEntity.getId();
                    assignee = taskEntity.getAssignee();
                    nodeId = taskEntity.getTaskDefinitionKey();
                    break;
                }
            }
            if (!StringUtils.hasText(payWaitingNodeTaskId)) {
                throw new RuntimeException("未查找到[支付等待完成]的任务节点任务，提交失败。");
            }
            if (!StringUtils.hasText(assignee)) {
                throw new RuntimeException("[支付等待完成]的任务节点任务的办理人为空，提交失败。");
            }
            String finalAssignee = assignee;
            String finalPayWaitingNodeTaskId = payWaitingNodeTaskId;
            String finalNodeId = nodeId;
            TenantUtils.execute(bpmFormMainDO.getTenantId(), () -> {
                WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(finalAssignee));
                BpmTaskApproveFormRespVO bpmTaskApproveFormRespVO = this.getTaskFormInfo(finalPayWaitingNodeTaskId, String.valueOf(businessKey));
                Map<String, Object> variables = bpmTaskApproveFormRespVO.getVariables();
                variables.put("approvalType", ObjectUtil.equals(submitType, "pass") ? "pass" : "disagree");
                variables.put("reason", StringUtils.hasText(reason) ? reason : (ObjectUtil.equals(submitType, "pass") ? "支付成功" : "支付失败"));
                variables.put("nodeId", finalNodeId);
                BpmTaskApproveReqVO reqVO = new BpmTaskApproveReqVO();
                reqVO.setId(finalPayWaitingNodeTaskId);
                reqVO.setReason(reason);
                reqVO.setVariables(variables);
                this.approveTaskV2(Long.valueOf(finalAssignee), reqVO);
            });
        } else {
            throw new IllegalStateException("暂不支持该业务类型后台提交流程");
        }

        System.out.println("提交流程处理结束");
    }

    private String getTaskNodeExtPropByKey(Process process, String taskDefinitionKey, String extPropKey) {
        FlowElement flowElement = process.getFlowElement(taskDefinitionKey);
        Map<String, List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();
        if (CollectionUtils.isEmpty(extensionElements)) {
            return null;
        }
        List<ExtensionElement> properties = extensionElements.get("properties");
        if (CollectionUtils.isEmpty(properties)) {
            return null;
        }
        Map<String, List<ExtensionElement>> childElements = properties.get(0).getChildElements();
        if (CollectionUtils.isEmpty(childElements)) {
            return null;
        }
        List<ExtensionElement> property = childElements.get("property");

        for (ExtensionElement extElement:property) {
            Map<String, List<ExtensionAttribute>> attributes = extElement.getAttributes();
            if (CollectionUtils.isEmpty(attributes)) {
                continue;
            }
            if (ObjectUtil.equals(attributes.get("name").get(0).getValue(), extPropKey)) {
                return attributes.get("value").get(0).getValue();
            }
        }

        return null;
    }
}
