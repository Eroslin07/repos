package com.newtouch.uctp.module.bpm.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSON;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.util.number.NumberUtils;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.newtouch.uctp.module.bpm.controller.admin.task.vo.instance.*;
import com.newtouch.uctp.module.bpm.convert.task.BpmProcessInstanceConvert;
import com.newtouch.uctp.module.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.form.BpmFormMainDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormMainMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import com.newtouch.uctp.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.newtouch.uctp.module.bpm.enums.task.BpmProcessInstanceStatusEnum;
import com.newtouch.uctp.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventPublisher;
import com.newtouch.uctp.module.bpm.service.definition.BpmProcessDefinitionService;
import com.newtouch.uctp.module.bpm.service.message.BpmMessageService;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.api.tenant.TenantApi;
import com.newtouch.uctp.module.system.api.tenant.dto.TenantRespDTO;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;
import com.newtouch.uctp.module.system.api.user.dto.AdminUserRespDTO;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.common.util.collection.CollectionUtils.convertList;
import static com.newtouch.uctp.module.bpm.enums.ErrorCodeConstants.*;

/**
 * 流程实例 Service 实现类
 *
 * ProcessDefinition & ProcessInstance & Execution & Task 的关系：
 *     1. <a href="https://blog.csdn.net/bobozai86/article/details/105210414" />
 *
 * HistoricProcessInstance & ProcessInstance 的关系：
 *     1. <a href=" https://my.oschina.net/843294669/blog/71902" />
 *
 * 简单来说，前者 = 历史 + 运行中的流程实例，后者仅是运行中的流程实例
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BpmProcessInstanceServiceImpl implements BpmProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private BpmProcessInstanceExtMapper processInstanceExtMapper;
    @Resource
    @Lazy // 解决循环依赖
    private BpmTaskService taskService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private HistoryService historyService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private BpmProcessInstanceResultEventPublisher processInstanceResultEventPublisher;
    @Resource
    private BpmMessageService messageService;
    @Resource
    private BpmFormDataService bpmFormDataService;
    @Resource
    private BpmFormMainMapper bpmFormMainMapper;
    @Resource
    private TenantApi tenantApi;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    public IdentityService identityService;
    @Resource
    private RedisTemplate redisTemplate;

    private final static Configuration freemarkerTemplateCfg = new Configuration(Configuration.VERSION_2_3_22);
    static {
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.ZHSQ.name(), "${marketName!}${merchantName!}账号申请流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.SGYZ.name(), "${marketName!}${merchantName!}收车价格超公允值审批流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.SCKP.name(), "${marketName!}${merchantName!}收车合同${contractCode!}开票流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.SCGH.name(), "${marketName!}${merchantName!}收车合同${contractCode!}过户流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.SKZH.name(), "${marketName!}${merchantName!}收车合同${contractCode!}收车款支付失败流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.MGYZ.name(), "${marketName!}${merchantName!}卖车价格超公允值审批流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.MCKP.name(), "${marketName!}${merchantName!}卖车合同${contractCode!}开票流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.MCGH.name(), "${marketName!}${merchantName!}卖车合同${contractCode!}过户流程");
        stringTemplateLoader.putTemplate(BpmDefTypeEnum.LRTX.name(), "${marketName!}${merchantName!}利润提现流程");
        freemarkerTemplateCfg.setTemplateLoader(stringTemplateLoader);
        freemarkerTemplateCfg.setClassicCompatible(true);
        freemarkerTemplateCfg.setDefaultEncoding("UTF-8");
    }


    @Override
    public ProcessInstance getProcessInstance(String id) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<ProcessInstance> getProcessInstances(Set<String> ids) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public PageResult<BpmProcessInstancePageItemRespVO> getMyProcessInstancePage(Long userId,
                                                                                 BpmProcessInstanceMyPageReqVO pageReqVO) {
        // 通过 BpmProcessInstanceExtDO 表，先查询到对应的分页
        PageResult<BpmProcessInstanceExtDO> pageResult = processInstanceExtMapper.selectPage(userId, pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return new PageResult<>(pageResult.getTotal());
        }

        // 获得流程 Task Map
        List<String> processInstanceIds = convertList(pageResult.getList(), BpmProcessInstanceExtDO::getProcessInstanceId);
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(processInstanceIds);
        // 转换返回
        return BpmProcessInstanceConvert.INSTANCE.convertPage(pageResult, taskMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqVO createReqVO) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());
        // 发起流程
        return createProcessInstance0(userId, definition, createReqVO.getVariables(), null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstanceV2(Long userId, @Valid BpmProcessInstanceCreateReqVO createReqVO) {
        // 获得流程业务ID
        String businessKey = StrUtil.toStringOrNull(createReqVO.getVariables().get("businessKey"));

        if (!StringUtils.hasText(businessKey)) {
            throw exception(PROCESS_BUSI_KEY_NOT_EXISTS);
        }
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectById(Long.valueOf(businessKey));
        if (ObjectUtil.isNotEmpty(bpmFormMainDO) && bpmFormMainDO.getStatus() != 0) {
            throw exception(PROCESS_BUSI_RUNING);
        }

        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());

        HashMap<String, Object> formDataObject = (HashMap<String, Object>) createReqVO.getVariables().get("formDataJson");
        String workFlowMainEntityAlias = this.bpmFormDataService.getObjectMainEntityAlias(formDataObject);
        Map<String, Object> formMainDataObject = this.bpmFormDataService.getObjectMainEntityMap(formDataObject);
        formMainDataObject.put("status", 1);
        formMainDataObject.put("procDefId", definition.getId());
        formMainDataObject.put("startUserId", userId);
        formMainDataObject.put("submitTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        formDataObject.put(workFlowMainEntityAlias, formMainDataObject);
        this.bpmFormDataService.saveDataObject(Long.valueOf(businessKey), formDataObject);

        // 发起流程
        String procInstId = createProcessInstance0(userId, definition, createReqVO.getVariables(), businessKey);
        BpmFormMainDO updateBpmFormMainDO = bpmFormMainMapper.selectById(Long.valueOf(businessKey));
        updateBpmFormMainDO.setProcInstId(procInstId);
        updateBpmFormMainDO.setDoneTime(updateBpmFormMainDO.getSubmitTime());
        bpmFormMainMapper.updateById(updateBpmFormMainDO);

        return procInstId;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstanceByKey(Long userId, String processDefinitionKey, Map<String, Object> variables) {
        log.info("-------------------- begin createProcessInstanceByKey --------------------");
        log.info("userId:" + userId + ",processDefinitionKey:" + processDefinitionKey);
        if (CollectionUtils.isEmpty(variables)) {
            variables = new HashMap<String, Object>();
        }
        log.info("variables:" + JSON.toJSONString(variables));
        Long businessKey = IdUtil.getSnowflakeNextId();
        if (ObjectUtil.isNotEmpty(variables.get("businessKey"))) {
            businessKey = Long.valueOf(StrUtil.toStringOrNull(variables.get("businessKey")));
        }
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectById(Long.valueOf(businessKey));
        if (ObjectUtil.isNotEmpty(bpmFormMainDO) && bpmFormMainDO.getStatus() != 0) {
            throw exception(PROCESS_BUSI_RUNING);
        }
        if (ObjectUtil.isNull(userId) && ObjectUtil.isNotNull(variables.get("startUserId"))) {
            userId = Long.valueOf(variables.get("startUserId").toString());
        }
        if (ObjectUtil.isNull(userId)) {
            throw  new ServiceException(1009003003, "流程发起人不能为空。");
        }
        Map<String, Object> userMap = bpmFormMainMapper.findUserByIdToMap(userId);
        if (CollectionUtils.isEmpty(userMap)) {
            throw  new ServiceException(1009003003, "流程发起人不能为空。");
        }
        variables.put("businessKey", businessKey);
        variables.put("startUserName", userMap.get("nickname"));
        variables.put("startUserMobile", userMap.get("mobile"));

        identityService.setAuthenticatedUserId(String.valueOf(userId));

        // 1.获得流程定义
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).active().orderByProcessDefinitionVersion().desc().list();
        if (CollectionUtils.isEmpty(processDefinitionList)) {
            throw  new ServiceException(1009003002, "流程定义不存在或未激活。");
        }
        ProcessDefinition processDefinition = CollUtil.get(processDefinitionList, 0);

        // 2.自动生成表单编号、单据标题等
        String serialNo = this.getFormSerialNo(processDefinitionKey);
        String title = this.getFormTitle(processDefinitionKey, variables);

        // 3.保存流程表单提交数据
        HashMap<String, Object> formDataJsonVariable = (HashMap<String, Object>) variables.getOrDefault("formDataJson", new HashMap<String, Object>());
        String workFlowMainEntityAlias = this.bpmFormDataService.getObjectMainEntityAlias(formDataJsonVariable);
        Map<String, Object> formMainDataObject = this.bpmFormDataService.getObjectMainEntityMap(formDataJsonVariable);
        formMainDataObject.put("id", businessKey);
        formMainDataObject.put("status", 1);
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "procDefId"), processDefinition.getId());
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "serialNo") , serialNo);
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "title"), title);
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "startUserId"), userId);
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "busiType"), processDefinitionKey);
        formMainDataObject.put(this.matchMapKey(formMainDataObject, "submitTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        HashMap<String, Object> submitFormDataJsonField = (HashMap<String, Object>) formMainDataObject.getOrDefault(this.matchMapKey(formMainDataObject, "formDataJson"), new HashMap<String, Object>());
        if (!CollectionUtils.isEmpty(submitFormDataJsonField)) {
            formMainDataObject.put(this.matchMapKey(formMainDataObject, "formDataJson"), JSON.toJSONString(submitFormDataJsonField).getBytes(StandardCharsets.UTF_8));
        } else {
            formMainDataObject.put(this.matchMapKey(formMainDataObject, "formDataJson"), null);
        }
        formDataJsonVariable.put(workFlowMainEntityAlias, formMainDataObject);
        log.info("variables:" + JSON.toJSONString(variables));
        log.info("formDataJsonVariable:" + JSON.toJSONString(formDataJsonVariable));
        this.bpmFormDataService.saveDataObject(businessKey, formDataJsonVariable);

        // 4.发起流程
        String procInstId = createProcessInstance0(userId, processDefinition, variables, String.valueOf(businessKey));
        BpmFormMainDO updateBpmFormMainDO = bpmFormMainMapper.selectById(Long.valueOf(businessKey));
        updateBpmFormMainDO.setProcInstId(procInstId);
        updateBpmFormMainDO.setDoneTime(updateBpmFormMainDO.getSubmitTime());
        bpmFormMainMapper.updateById(updateBpmFormMainDO);
        /*List<String> procInsts = new ArrayList<>();
        procInsts.add(procInstId);
        List<Task> taskList = taskService.getTasksByProcessInstanceIds(procInsts);
        if (!CollectionUtils.isEmpty(taskList)) {
            bpmEngTaskService.complete(taskList.get(0).getId());
        }*/

        return String.valueOf(businessKey);
    }

    private String matchMapKey(Map<String, Object> map, String key) {
        for (String k : map.keySet()) {
            if (k.toLowerCase().equals(key.toLowerCase())) {
                return k;
            }
        }

        return key;
    }

    private String getFormSerialNo(String busiTypeCode) {
        String serialNoPrefix = busiTypeCode.concat(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()));
        Long index = this.redisTemplate.opsForValue().increment(serialNoPrefix, 1L);
        return serialNoPrefix.concat(String.format("%05d", index.intValue()));
    }

    private String getFormTitle(String processDefinitionKey, Map<String, Object> variables) {
        try {
            Template template = freemarkerTemplateCfg.getTemplate(processDefinitionKey);
            StringWriter writer = new StringWriter();
            template.process(variables,writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO createReqDTO) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getActiveProcessDefinition(createReqDTO.getProcessDefinitionKey());
        // 发起流程
        return createProcessInstance0(userId, definition, createReqDTO.getVariables(), createReqDTO.getBusinessKey());
    }

    @Override
    public BpmProcessInstanceRespVO getProcessInstanceVO(String id) {
        // 获得流程实例
        HistoricProcessInstance processInstance = getHistoricProcessInstance(id);
        if (processInstance == null) {
            return null;
        }
        BpmProcessInstanceExtDO processInstanceExt = processInstanceExtMapper.selectByProcessInstanceId(id);
        Assert.notNull(processInstanceExt, "流程实例拓展({}) 不存在", id);

        // 获得流程定义
        ProcessDefinition processDefinition = processDefinitionService
                                                    .getProcessDefinition(processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinition, "流程定义({}) 不存在", processInstance.getProcessDefinitionId());
        BpmProcessDefinitionExtDO processDefinitionExt = processDefinitionService.getProcessDefinitionExt(
                processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinitionExt, "流程定义拓展({}) 不存在", id);
        String bpmnXml = processDefinitionService.getProcessDefinitionBpmnXML(processInstance.getProcessDefinitionId());

        // 获得 User
        AdminUserRespDTO startUser = adminUserApi.getUser(NumberUtils.parseLong(processInstance.getStartUserId())).getCheckedData();
        DeptRespDTO dept = null;
        if (startUser != null) {
            dept = deptApi.getDept(startUser.getDeptId()).getCheckedData();
        }

        // 拼接结果
        return BpmProcessInstanceConvert.INSTANCE.convert2(processInstance, processInstanceExt,
                processDefinition, processDefinitionExt, bpmnXml, startUser, dept);
    }

    @Override
    public void cancelProcessInstance(Long userId, @Valid BpmProcessInstanceCancelReqVO cancelReqVO) {
        // 校验流程实例存在
        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS);
        }
        // 只能取消自己的
        if (!Objects.equals(instance.getStartUserId(), String.valueOf(userId))) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF);
        }

        // 通过删除流程实例，实现流程实例的取消,
        // 删除流程实例，正则执行任务 ACT_RU_TASK. 任务会被删除。通过历史表查询
        deleteProcessInstance(cancelReqVO.getId(),
                BpmProcessInstanceDeleteReasonEnum.CANCEL_TASK.format(cancelReqVO.getReason()));
    }

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String id) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public void createProcessInstanceExt(ProcessInstance instance) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition2(instance.getProcessDefinitionId());
        // 插入 BpmProcessInstanceExtDO 对象
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(instance.getId())
                .setProcessDefinitionId(definition.getId())
                .setName(instance.getProcessDefinitionName())
                .setStartUserId(Long.valueOf(instance.getStartUserId()))
                .setCategory(definition.getCategory())
                .setStatus(BpmProcessInstanceStatusEnum.RUNNING.getStatus())
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());

        processInstanceExtMapper.insert(instanceExtDO);
    }

    @Override
    public void updateProcessInstanceExtCancel(FlowableCancelledEvent event) {
        // 判断是否为 Reject 不通过。如果是，则不进行更新.
        // 因为，updateProcessInstanceExtReject 方法，已经进行更新了
        if (BpmProcessInstanceDeleteReasonEnum.isRejectReason((String)event.getCause())) {
            return;
        }

        // 需要主动查询，因为 instance 只有 id 属性
        // 另外，此时如果去查询 ProcessInstance 的话，字段是不全的，所以去查询了 HistoricProcessInstance
        HistoricProcessInstance processInstance = getHistoricProcessInstance(event.getProcessInstanceId());
        // 更新拓展表
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(event.getProcessInstanceId())
                .setEndTime(LocalDateTime.now()) // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        // 发送流程实例的状态事件
        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    @Override
    public void updateProcessInstanceExtComplete(ProcessInstance instance) {
        // 需要主动查询，因为 instance 只有 id 属性
        // 另外，此时如果去查询 ProcessInstance 的话，字段是不全的，所以去查询了 HistoricProcessInstance
        HistoricProcessInstance processInstance = getHistoricProcessInstance(instance.getId());
        // 更新拓展表
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO()
                .setProcessInstanceId(instance.getProcessInstanceId())
                .setEndTime(LocalDateTime.now()) // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()); // 如果正常完全，说明审批通过
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectOne(BpmFormMainDO::getProcInstId, instance.getId());
        bpmFormMainDO.setStatus(2);
        bpmFormMainMapper.updateById(bpmFormMainDO);

        // 发送流程被通过的消息
        //messageService.sendMessageWhenProcessInstanceApprove(BpmProcessInstanceConvert.INSTANCE.convert2ApprovedReq(instance));

        // 发送流程实例的状态事件
        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessInstanceExtReject(String id, String reason) {
        // 需要主动查询，因为 instance 只有 id 属性
        ProcessInstance processInstance = getProcessInstance(id);
        // 删除流程实例，以实现驳回任务时，取消整个审批流程
        deleteProcessInstance(id, StrUtil.format(BpmProcessInstanceDeleteReasonEnum.REJECT_TASK.format(reason)));

        // 更新 status + result
        // 注意，不能和上面的逻辑更换位置。因为 deleteProcessInstance 会触发流程的取消，进而调用 updateProcessInstanceExtCancel 方法，
        // 设置 result 为 BpmProcessInstanceStatusEnum.CANCEL，显然和 result 不一定是一致的
        BpmProcessInstanceExtDO instanceExtDO = new BpmProcessInstanceExtDO().setProcessInstanceId(id)
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        // 发送流程被不通过的消息
        messageService.sendMessageWhenProcessInstanceReject(BpmProcessInstanceConvert.INSTANCE.convert2RejectReq(processInstance, reason));

        // 发送流程实例的状态事件
        processInstanceResultEventPublisher.sendProcessInstanceResultEvent(
                BpmProcessInstanceConvert.INSTANCE.convert(this, processInstance, instanceExtDO.getResult()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BpmCreateBaseInfoRespVO getCreateBaseInfoPre(BpmCreateBaseInfoReqVO reqVO) {
        BpmCreateBaseInfoRespVO respVO = new BpmCreateBaseInfoRespVO();
        Long tenantId = TenantContextHolder.getTenantId();
        TenantRespDTO tenantRespDTO = tenantApi.getTenant(tenantId).getData();
        String tenantName = tenantRespDTO.getName();
        if (StringUtils.hasText(reqVO.getMerchantName())) {
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(loginUserId).getCheckedData();
            DeptRespDTO deptRespDTO = deptApi.getDept(adminUserRespDTO.getDeptId()).getData();
            System.out.println(deptRespDTO);
        }



        respVO.setBusiType(reqVO.getBusiType());
        return respVO;
    }

    private void deleteProcessInstance(String id, String reason) {
        runtimeService.deleteProcessInstance(id, reason);
    }

    private String createProcessInstance0(Long userId, ProcessDefinition definition,
                                          Map<String, Object> variables, String businessKey) {
        // 校验流程定义
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }
        if (definition.isSuspended()) {
            throw exception(PROCESS_DEFINITION_IS_SUSPENDED);
        }
        // 创建流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId(), businessKey, variables);
        // 设置流程名字
        runtimeService.setProcessInstanceName(instance.getId(), definition.getName());

        // 补全流程实例的拓展表
        processInstanceExtMapper.updateByProcessInstanceId(new BpmProcessInstanceExtDO().setProcessInstanceId(instance.getId())
                .setFormVariables(new HashMap<>()));
        return instance.getId();
    }

}
