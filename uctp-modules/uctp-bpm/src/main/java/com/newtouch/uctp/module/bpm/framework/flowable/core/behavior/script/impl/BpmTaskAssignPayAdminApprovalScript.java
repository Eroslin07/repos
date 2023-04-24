package com.newtouch.uctp.module.bpm.framework.flowable.core.behavior.script.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import com.newtouch.uctp.module.bpm.dal.mysql.definition.BpmTaskAssignRuleMapper;
import com.newtouch.uctp.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import com.newtouch.uctp.module.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;

/**
 * @author helong
 * @date 2023/4/24 11:11
 */
@Component
public class BpmTaskAssignPayAdminApprovalScript implements BpmTaskAssignScript {
    @Resource
    private BpmTaskAssignRuleMapper bpmTaskAssignRuleMapper;

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        Set<Long> result = new HashSet<Long>();
        List<String> userList = bpmTaskAssignRuleMapper.getTaskAssignRulesByRoleCode("pay_admin");
        for (String user : userList) {
            result.add(Long.valueOf(user));
        }

        return result;
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.PAY_ADMIN_APPROVAL;
    }
}
