package com.newtouch.uctp.module.bpm.framework.flowable.core.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.CarInfoDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.car.ContractDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.form.BpmFormMainDO;
import com.newtouch.uctp.module.bpm.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.bpm.dal.mysql.car.CarInfoMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.car.ContractMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormMainMapper;
import com.newtouch.uctp.module.bpm.dal.mysql.user.UserMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.bpm.service.openinvoice.BpmOpenInvoiceService;
import com.newtouch.uctp.module.bpm.service.transfer.BpmCarTransferService;
import com.newtouch.uctp.module.bpm.service.user.UserService;
import com.newtouch.uctp.module.business.api.account.AccountApi;
import com.newtouch.uctp.module.business.api.account.AccountProfitApi;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.api.account.dto.ProfitPresentAuditDTO;
import com.newtouch.uctp.module.business.api.carInfo.CarInfoApi;
import com.newtouch.uctp.module.business.api.carinfodetails.CarInfoDetailsApi;
import com.newtouch.uctp.module.business.api.contract.MerchantMoneyApi;
import com.newtouch.uctp.module.business.api.file.notice.NoticeApi;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
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
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private MerchantMoneyApi merchantMoneyApi;
    @Resource
    private BpmOpenInvoiceService bpmOpenInvoiceService;
    @Resource
    private BpmCarTransferService bpmCarTransferService;
    @Resource
    private CarInfoDetailsApi carInfoDetailsApi;
    @Resource
    private CarInfoApi carInfoApi;
    @Resource 
    private NoticeApi noticeApi;

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
            // 收车公允价值流程发起，修改车辆状态
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.COLLECT.value(),CarStatus.COLLECT_B.value(),CarStatus.COLLECT_B_A.value(),"收车超公允价值已发起","");
            // 预占保证金（通过车辆ID查询车辆的收车草稿合同）     合同类型：1-收车委托合同   2-收车合同  3-卖车委托合同  4-卖车合同
            ContractDO contractDO = this.contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                    .eq(ContractDO::getCarId, bpmFormMainVO.getThirdId()).eq(ContractDO::getContractType, 2).eq(ContractDO::getInvalided, 0));
            if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getContractId())) {
                throw new RuntimeException("车辆ID[" + bpmFormMainVO.getThirdId() + "]预占保证金失败，原因：未获取到收车合同信息");
            }
            merchantMoneyApi.reserveCash(contractDO.getContractId());
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MGYZ.name())) {
            //卖车公允价值流程发起，修改车辆状态
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SELL.value(),CarStatus.SELL_B.value(),CarStatus.SELL_B_A.value(),"卖车超公允价值已发起","");
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SKZH.name())) {
            // 收车款支付失败流程发起时，修改车辆状态
            ContractDO contractDO = contractMapper.selectByContractIdAndContractType(bpmFormMainVO.getThirdId(), 2);
            if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getCarId())) {
                throw new RuntimeException("根据（契约锁）收车合同ID【" + bpmFormMainVO.getThirdId() + "】获取合同的基本信息失败。");
            }
            carInfoApi.updateCarStatus(contractDO.getCarId(),CarStatus.COLLECT.value(),CarStatus.COLLECT_C.value(),CarStatus.COLLECT_C_A.value(),"收车款支付失败已发起","");
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SCKP.name())) {
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SALE.value(),CarStatus.SALE_A.value(),CarStatus.SALE_A_A.value(),"收车开票已发起","");
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MCKP.name())) {
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SOLD.value(),CarStatus.SOLD_A.value(),CarStatus.SOLD_A_A.value(),"卖车开票已发起","");
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SCGH.name())) {
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SALE.value(),CarStatus.SALE_A.value(),CarStatus.SALE_A_B.value(),"收车过户已发起","");
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MCGH.name())) {
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SOLD.value(),CarStatus.SOLD_A.value(),CarStatus.SOLD_A_B.value(),"卖车过户已发起","");
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
        BpmFormResVO bpmFormResVO = getBpmFormResVOData(bpmFormMainVO);

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
                String openAccountResult = accountApi.merchantAccountOpen(accountVO).getCheckedData();
                if (StringUtils.hasText(openAccountResult) && openAccountResult.contains("开户成功")) {
                    // 注册成功
                    noticeApi.saveTaskNotice("1", "12", reason, bpmFormResVO);
                }
            }else if ("disagree".equals(approvalType)){
                // 删除用户
                JSONObject jsonObject = bpmFormMainVO.getFormDataJson();
                AdminUserDO adminUserDO = userMapper.selectOne("mobile", jsonObject.get("phone"));
                userService.deleteUser(adminUserDO.getId());
                // 注册失败
                noticeApi.saveTaskNotice("1", "11", reason, bpmFormResVO);
            }
        } else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SGYZ.name())) {
           //收车公允审批不通过
            if ("disagree".equals(approvalType)) {
                //修改车辆状态
                carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.COLLECT.value(),CarStatus.COLLECT_A.value(),CarStatus.COLLECT_A_A.value(),"退回",reason);
                // 释放保证金
                ContractDO contractDO = this.contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                        .eq(ContractDO::getCarId, bpmFormMainVO.getThirdId()).eq(ContractDO::getContractType, 2).eq(ContractDO::getInvalided, 0));
                if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getContractId())) {
                    throw new RuntimeException("车辆ID[" + bpmFormMainVO.getThirdId() + "]释放保证金失败，原因：未获取到收车合同信息");
                }
                merchantMoneyApi.releaseCash(contractDO.getContractId());
                // 保存消息
                noticeApi.saveTaskNotice("1", "21", reason, bpmFormResVO);
            } else if ("pass".equals(approvalType)) {
                //carinfo记录流程状态
                CarInfoDO carInfoDO = carInfoMapper.selectById(bpmFormMainVO.getThirdId());
                carInfoApi.updateBpmApproveInfo(bpmFormMainVO.getThirdId(), "通过", reason);
                // 委托合同自动签署   合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
                ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getCarId, carInfoDO.getId())
                        .eq(ContractDO::getContractType, 1).eq(ContractDO::getInvalided, 0));
                boolean isSend = qysConfigApi.send(contractDO.getContractId(), false).getCheckedData();
                if (!isSend) {
                    throw new RuntimeException("自动发起并签署委托合同异常");
                }
                noticeApi.saveTaskNotice("0", "12", reason, bpmFormResVO);
            }
        } else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MGYZ.name())) {
            //卖车公允审批不通过
            if ("disagree".equals(approvalType)) {
                //修改车辆状态
                carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SELL.value(),CarStatus.SELL_A.value(),CarStatus.SELL_A_A.value(),"退回",reason);
                noticeApi.saveTaskNotice("1", "31", reason, bpmFormResVO);
            } else if ("pass".equals(approvalType)) {
                //carinfo记录流程状态
                CarInfoDO carInfoDO = carInfoMapper.selectById(bpmFormMainVO.getThirdId());
                carInfoApi.updateBpmApproveInfo(bpmFormMainVO.getThirdId(), "通过", reason);
                // 委托合同自动签署   合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
                ContractDO contractDO = this.contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                        .eq(ContractDO::getCarId, carInfoDO.getId()).eq(ContractDO::getContractType, 3).eq(ContractDO::getInvalided, 0));
                boolean isSend = qysConfigApi.send(contractDO.getContractId(), false).getCheckedData();
                if (!isSend) {
                    throw new RuntimeException("自动发起并签署委托合同异常");
                }
                noticeApi.saveTaskNotice("0", "22", reason, bpmFormResVO);
            }
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SKZH.name())) {
            // 收车支付失败流程重新支付成功后，需要发起收车开票流程
            if ("pass".equals(approvalType)) {
                String formMainId = bpmOpenInvoiceService.createOpenInvoiceBpm(bpmFormMainVO.getThirdId(), BpmDefTypeEnum.SCKP.name());
                if (!StringUtils.hasText(formMainId)) {
                    throw new RuntimeException("收车款支付失败流程完成后，自动发起反向二手车统一发票待办开票审批流程失败");
                }
            }
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SCKP.name())) {
            // 收车开票完成后，自动发起收车过户流程
            if ("pass".equals(approvalType)) {
                // 写入转入地车辆管理所名称（收车）
                String transManageName = bpmFormMainVO.getFormDataJson().getJSONObject("carInvoiceDetailVO").getString("transManageName");
                carInfoDetailsApi.updateTransManage(bpmFormMainVO.getThirdId(), transManageName, null);
                // 默认发起过户流程
                String formMainId = bpmCarTransferService.createTransferBpm(bpmFormMainVO.getThirdId(), BpmDefTypeEnum.SCGH.name());
                if (!StringUtils.hasText(formMainId)) {
                    throw new RuntimeException("收车开票完成后，自动发起收车过户流程失败");
                }
            }
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MCKP.name())) {
            // 卖车开票完成后，自动发起收车过户流程
            if ("pass".equals(approvalType)) {
                // 写入转入地车辆管理所名称（卖车）
                String sellTransManageName = bpmFormMainVO.getFormDataJson().getJSONObject("carInvoiceDetailVO").getString("transManageName");
                carInfoDetailsApi.updateTransManage(bpmFormMainVO.getThirdId(), null, sellTransManageName);
                // 默认发起过户流程
                String formMainId = bpmCarTransferService.createTransferBpm(bpmFormMainVO.getThirdId(), BpmDefTypeEnum.MCGH.name());
                if (!StringUtils.hasText(formMainId)) {
                    throw new RuntimeException("卖车开票完成后，自动发起卖车过户流程失败");
                }
            }
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SCGH.name())) {
            // 1.收车过户成功，修改车辆状态为收车已过户
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SALE.value(),CarStatus.SALE_B.value(),CarStatus.SALE_B_A.value(),"收车过户成功",reason);
        }
        else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MCGH.name())) {
            // 1.卖车过户成功，修改车辆状态为分账
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SOLD.value(),CarStatus.SOLD_B.value(),CarStatus.SOLD_B_A.value(),"卖车过户待分账",reason);
            //TODO:调用分账接口
            merchantMoneyApi.recorded(bpmFormMainVO.getThirdId());
            carInfoApi.updateCarStatus(bpmFormMainVO.getThirdId(),CarStatus.SOLD.value(),CarStatus.SOLD_C.value(),CarStatus.SOLD_C_A.value(),"卖车过户已分账",reason);
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
        TaskEntity taskEntity = (TaskEntity)event.getEntity();
        // 流程实例ID
        String processInstanceId = taskEntity.getProcessInstanceId();
        // 流程定义ID
        String processDefinitionId = taskEntity.getProcessDefinitionId();
        // 当前完成节点ID
        String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        BpmFormMainDO bpmFormMainDO = bpmFormMainMapper.selectOne(BpmFormMainDO::getProcInstId, processInstanceId);
        // 流程时，判断流程通过与不通过的标识（通过：pass    不通过：disagree）
        String approvalType = StrUtil.toStringOrNull(taskEntity.getVariable("approvalType"));
        // 流程时，通过与不通过的审批意见
        String reason = StrUtil.toStringOrNull(taskEntity.getVariable("reason"));
        String businessKey = String.valueOf(bpmFormMainDO.getId());
        BpmFormMainVO bpmFormMainVO = this.getBpmFormMainData(businessKey);

        if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.LRTX.name())) {
            String nodeSymbol = this.getTaskNodeExtPropByKey(taskEntity, "nodeSymbol");

            // ”提现审批“节点操作
            if (ObjectUtil.equals(nodeSymbol, "withdrawApprove")) {
                Long tenantId = SecurityFrameworkUtils.getLoginUser().getTenantId(); // 租户ID

                if ("pass".equals(approvalType)) {
                    // 审批通过
                    ProfitPresentAuditDTO a = new ProfitPresentAuditDTO();
                    a.setBusinessKey(businessKey);
                    a.setAuditOpinion(1);

                    accountProfitApi.presentAudit(tenantId, a);
                } else if ("disagree".equals(approvalType)) {
                    // 审批不通过
                    ProfitPresentAuditDTO a = new ProfitPresentAuditDTO();
                    a.setBusinessKey(businessKey);
                    a.setAuditOpinion(2);

                    accountProfitApi.presentAudit(tenantId, a);
                }
            }
        }
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

    private BpmFormResVO getBpmFormResVOData(BpmFormMainVO bpmFormMainDO) {
        BpmFormResVO bpmFormResVO = new BpmFormResVO();
        bpmFormResVO.setId(bpmFormMainDO.getId());
        bpmFormResVO.setStatus(bpmFormMainDO.getStatus());
        bpmFormResVO.setProcDefId(bpmFormMainDO.getProcDefId());
        bpmFormResVO.setProcInstId(bpmFormMainDO.getProcInstId());
        bpmFormResVO.setSerialNo(bpmFormMainDO.getSerialNo());
        bpmFormResVO.setTitle(bpmFormMainDO.getTitle());
        bpmFormResVO.setStartUserId(bpmFormMainDO.getStartUserId());
        bpmFormResVO.setMerchantId(bpmFormMainDO.getMerchantId());
        bpmFormResVO.setBusiType(bpmFormMainDO.getBusiType());
        bpmFormResVO.setRemark(bpmFormMainDO.getRemark());
        bpmFormResVO.setThirdId(bpmFormMainDO.getThirdId());
        bpmFormResVO.setSubmitTime(bpmFormMainDO.getSubmitTime());
        bpmFormResVO.setDoneTime(bpmFormMainDO.getDoneTime());
        bpmFormResVO.setFormDataJson(bpmFormMainDO.getFormDataJson());

        return bpmFormResVO;
    }


    /**
     * 根据节点的扩展属性Key获取值
     * @param taskEntity
     * @param extPropKey
     * @return
     */
    private String getTaskNodeExtPropByKey(TaskEntity taskEntity, String extPropKey) {
        // 流程定义ID
        String processDefinitionId = taskEntity.getProcessDefinitionId();
        // 当前完成节点ID
        String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        FlowElement flowElement = repositoryService.getBpmnModel(processDefinitionId).getMainProcess().getFlowElement(taskDefinitionKey);
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
