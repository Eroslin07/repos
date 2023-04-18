package com.newtouch.uctp.module.bpm.framework.flowable.core.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.service.notice.NoticeService;
import com.newtouch.uctp.module.business.api.file.notice.NoticeApi;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.module.bpm.controller.admin.form.vo.BpmFormMainVO;
import com.newtouch.uctp.module.bpm.dal.dataobject.form.BpmFormMainDO;
import com.newtouch.uctp.module.bpm.dal.mysql.form.BpmFormMainMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.business.api.file.notice.NoticeApi;
import com.newtouch.uctp.module.business.api.file.notice.vo.BpmFormResVO;

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
        bpmFormMainVO.setProcInstId(processInstance.getId());

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
        String reason = "";
        String businessKey = processInstance.getBusinessKey();
        BpmFormMainVO bpmFormMainVO = this.getBpmFormMainData(businessKey);


        BpmFormResVO bpmFormResVO=new BpmFormResVO();
        bpmFormResVO.setId(bpmFormMainVO.getId());
        bpmFormResVO.setStatus(bpmFormMainVO.getStatus());
        bpmFormResVO.setProcDefId(bpmFormMainVO.getProcDefId());
        bpmFormResVO.setProcInstId(bpmFormMainVO.getProcInstId());
        bpmFormResVO.setSerialNo(bpmFormMainVO.getSerialNo());
        bpmFormResVO.setTitle(bpmFormMainVO.getTitle());
        bpmFormResVO.setStartUserId(bpmFormMainVO.getStartUserId());
        bpmFormResVO.setMerchantId(bpmFormMainVO.getMerchantId());
        bpmFormResVO.setBusiType(bpmFormMainVO.getBusiType());
        bpmFormResVO.setRemark(bpmFormMainVO.getRemark());
        bpmFormResVO.setThirdId(bpmFormMainVO.getThirdId());
        bpmFormResVO.setSubmitTime(bpmFormMainVO.getSubmitTime());
        bpmFormResVO.setDoneTime(bpmFormMainVO.getDoneTime());
        bpmFormResVO.setFormDataJson(bpmFormMainVO.getFormDataJson());
        // TODO: 根据业务场景进行个性化处理
        if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.ZHSQ.name())) {
            if ("pass".equals(approvalType)) {
                //注册成功
                //noticeApi.saveTaskNotice("1", "12", reason, bpmFormResVO);
                noticeService.saveTaskNotice("1", "12", reason, bpmFormResVO);
            }else if ("disagree".equals(approvalType)){
                //注册失败
                //noticeApi.saveTaskNotice("1", "11", reason, bpmFormResVO);
                noticeService.saveTaskNotice("1", "11", reason, bpmFormResVO);
            }
        } else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.SGYZ.name())) {
           //收车公允审批不通过
            if ("disagree".equals(approvalType)) {
                //noticeApi.saveTaskNotice("1", "21", reason, bpmFormResVO);
                noticeService.saveTaskNotice("1", "21", reason, bpmFormResVO);
            } else if ("pass".equals(approvalType)) {
                //noticeApi.saveTaskNotice("0", "12", reason, bpmFormResVO);
                noticeService.saveTaskNotice("0", "12", reason, bpmFormResVO);
            }
        } else if (ObjectUtil.equals(bpmFormMainVO.getBusiType(), BpmDefTypeEnum.MGYZ.name())) {
            //卖车公允审批不通过
            if ("disagree".equals(approvalType)) {
                //noticeApi.saveTaskNotice("1", "31", reason, bpmFormResVO);
                noticeService.saveTaskNotice("1", "31", reason, bpmFormResVO);
            } else if ("pass".equals(approvalType)) {
                //noticeApi.saveTaskNotice("0", "22", reason, bpmFormResVO);
                noticeService.saveTaskNotice("0", "22", reason, bpmFormResVO);
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
