package com.newtouch.uctp.module.bpm.framework.flowable.core.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.form.BpmFormMainDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormMainMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.user.UserMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.bpm.service.notice.NoticeService;
import com.newtouch.uctp.module.bpm.service.user.UserService;
import com.newtouch.uctp.module.business.api.account.AccountApi;
import com.newtouch.uctp.module.business.api.account.AccountProfitApi;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.api.account.dto.ProfitPresentAuditDTO;
import com.newtouch.uctp.module.business.api.file.notice.NoticeApi;
import com.newtouch.uctp.module.business.api.qys.QysConfigApi;
import com.newtouch.uctp.module.business.enums.CarStatus;

/**
 * 流程引擎全局业务处理器
 * @author helong
 * @date 2023/4/17 10:26
 */
@Component
public class BpmGlobalHandleListener {
    @Resource
    private BpmFormMainMapper bpmFormMainMapper;
    @Resource
    private NoticeApi noticeApi;
    @Resource
    private NoticeService noticeService;
    @Resource
    private UserService userService;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private QysConfigApi qysConfigApi;
    @Resource
    private AccountProfitApi accountProfitApi;
    @Resource
    private AccountApi accountApi;
    @Resource
    private UserMapper userMapper;

    /**
     * 流程创建时处理
     * @param event
     */
    public void processCreated(FlowableEngineEntityEvent event) {
        ProcessInstance processInstance = (ProcessInstance) event.getEntity();
        String businessKey = processInstance.getBusinessKey();
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectById(Long.valueOf(businessKey));
        bpmFormMainDO.setProcInstId(processInstance.getId());
        bpmFormMainMapper.updateById(bpmFormMainDO);
        BpmFormMainVO bpmFormMainVO = this.getBpmFormMainData(businessKey);
        if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SGYZ.name())) {
            //收车公允价值流程发起，修改车辆状态
            carInfoMapper.updateStatus(bpmFormMainVO.getThirdId(),CarStatus.COLLECT.value(),CarStatus.COLLECT_A_B.value(),CarStatus.COLLECT_A_B_A.value(),"已发起","");
        }else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MGYZ.name())) {
            //卖车公允价值流程发起，修改车辆状态
            carInfoMapper.updateStatus(bpmFormMainVO.getThirdId(),CarStatus.SELL.value(),CarStatus.SELL_B.value(),CarStatus.SELL_B_A.value(),"已发起","");
        }
        // TODO: 根据业务场景进行个性化处理
        System.out.println(bpmFormMainVO);
    }

    /**
     * 流程完成结束时处理
     * @param event
     */
    public void processCompleted(FlowableEngineEntityEvent event) {
        ProcessInstance processInstance = (ProcessInstance) event.getEntity();
        // 流程完成时，判断流程通过与不通过的标识（通过：pass    不通过：disagree）
        String approvalType = StrUtil.toStringOrNull(processInstance.getProcessVariables().get("approvalType"));
        // 流程完成时，通过与不通过的审批意见
        String reason = StrUtil.toStringOrNull(processInstance.getProcessVariables().get("reason"));
        String businessKey = processInstance.getBusinessKey();
        BpmFormMainVO bpmFormMainVO = this.getBpmFormMainData(businessKey);


        // TODO: 根据业务场景进行个性化处理
        if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.ZHSQ.name())) {
            if ("pass".equals(approvalType)) {
                // 更新用户状态
                JSONObject jsonObject = bpmFormMainVO.getFormDataJson();
                AdminUserDO adminUserDO = userMapper.selectOne("mobile", jsonObject.get("phone"));
                userService.updateUserStatus(adminUserDO.getId());
                // 公司认证
                Boolean flag = qysConfigApi.companyAuth(bpmFormMainVO.getStartUserId()).getCheckedData();
                //商户虚拟账户开户
                AccountDTO accountVO = new AccountDTO();
                accountVO.setIdCard(jsonObject.getString("idCard"));
                accountVO.setTenantId(adminUserDO.getTenantId());
                accountVO.setMerchantId(bpmFormMainVO.getMerchantId());
                accountVO.setBusinessName(jsonObject.getString("businessName"));
                accountVO.setLegalRepresentative(jsonObject.getString("legal_representative"));
                accountVO.setTaxNum(jsonObject.getString("taxNum"));
                accountVO.setBankName(jsonObject.getString("bankName"));
                accountVO.setBankNo(jsonObject.getString("bankNumber"));
                accountVO.setCashBankNo(jsonObject.getString("bondBankAccount"));
                accountApi.merchantAccountOpen(accountVO).getCheckedData();
                // 注册成功
                noticeService.saveTaskNotice("1", "12", reason, bpmFormMainVO);
            }else if ("disagree".equals(approvalType)){
                // 删除用户
                JSONObject jsonObject = bpmFormMainVO.getFormDataJson();
                AdminUserDO adminUserDO = userMapper.selectOne("phone", jsonObject.get("phone"));
                userService.deleteUser(adminUserDO.getId());
                // 注册失败
                noticeService.saveTaskNotice("1", "11", reason, bpmFormMainVO);
            }
        } else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SGYZ.name())) {
           //收车公允审批不通过
            if ("disagree".equals(approvalType)) {
                noticeService.saveTaskNotice("1", "21", reason, bpmFormMainVO);
                //修改车辆状态
                carInfoMapper.updateStatus(bpmFormMainVO.getThirdId(),CarStatus.COLLECT.value(),CarStatus.COLLECT_A.value(),CarStatus.COLLECT_A_A.value(),"退回",reason);
            } else if ("pass".equals(approvalType)) {
                noticeService.saveTaskNotice("0", "12", reason, bpmFormMainVO);
                //carinfo记录流程状态
                CarInfoDO carInfoDO = carInfoMapper.selectById(bpmFormMainVO.getThirdId());
                carInfoDO.setBpmStatus("通过");
                carInfoDO.setBpmReason(reason);
                carInfoMapper.updateById(carInfoDO);
            }
        } else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MGYZ.name())) {
            //卖车公允审批不通过
            if ("disagree".equals(approvalType)) {
                noticeService.saveTaskNotice("1", "31", reason, bpmFormMainVO);
                //修改车辆状态
                carInfoMapper.updateStatus(bpmFormMainVO.getThirdId(),CarStatus.SELL.value(),CarStatus.SELL_A.value(),CarStatus.SELL_A_A.value(),"退回",reason);
            } else if ("pass".equals(approvalType)) {
                noticeService.saveTaskNotice("0", "22", reason, bpmFormMainVO);
                //carinfo记录流程状态
                CarInfoDO carInfoDO = carInfoMapper.selectById(bpmFormMainVO.getThirdId());
                carInfoDO.setBpmStatus("通过");
                carInfoDO.setBpmReason(reason);
                carInfoMapper.updateById(carInfoDO);
            }
        }

        System.out.println(bpmFormMainVO);
    }

    /**
     * 流程作废或终止时处理
     * @param businessKey  流程业务ID
     */
    public void processCancelled(String businessKey) {

    }

    /**
     * 用户任务节点分配用户时处理（一般当任务分配给某个人时才会触发）
     * @param event
     */
    public void taskAssigned(FlowableEngineEntityEvent event) {
        System.out.println(event);
    }

    /**
     * 用户任务节点完成任务时处理
     * @param event
     */
    public void taskCompleted(FlowableEngineEntityEvent event) {
        /*TaskEntity taskEntity = (TaskEntity)event.getEntity();
        String processInstanceId = taskEntity.getProcessInstanceId();
        taskEntity.getProcessDefinitionId();


        if (true) {
            throw new IllegalStateException("Task is already completed");
        }*/



        String procDefKey = null; // 需要补充获取
        String taskNode = null; // 需要获得任务节点
        Task task = (Task) event.getEntity();
        if (BpmDefTypeEnum.LRTX.name().equals(procDefKey) && "提现审批".equals(taskNode)) {
            // 当前流程是“利润提现流程”且当前任务是“提现审批”

            // 获得“利润提现申请的业务ID”
            String businessKey = null; // 需要获得业务ID
            Long tenantId = SecurityFrameworkUtils.getLoginUser().getTenantId(); // 租户ID
            String token = null; // 需要获得token

            String approvalType = StrUtil.toStringOrNull(task.getTaskLocalVariables().get("approvalType")); // 审批结果

            if ("pass".equals(approvalType)) {
                // 审批通过
                ProfitPresentAuditDTO a = new ProfitPresentAuditDTO();
                a.setBusinessKey(businessKey);
                a.setAuditOpinion(1);

                accountProfitApi.presentAudit(tenantId, token, a);
            } else if ("disagree".equals(approvalType)) {
                // 审批不通过
                ProfitPresentAuditDTO a = new ProfitPresentAuditDTO();
                a.setBusinessKey(businessKey);
                a.setAuditOpinion(2);

                accountProfitApi.presentAudit(tenantId, token, a);
            }
        }

        System.out.println(event);
    }

    private BpmFormMainVO getBpmFormMainData(String businessKey) {
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectById(Long.valueOf(businessKey));

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
        bpmFormMainVO.setFormDataJson(ObjectUtil.isNull(bpmFormMainDO.getFormDataJson()) ? new JSONObject() : JSON.parseObject(new String(bpmFormMainDO.getFormDataJson())));

        return bpmFormMainVO;
    }

}
