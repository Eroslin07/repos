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
 * @date 2023/4/18 15:05
 */
@Component
public class BpmTaskAssignGeneralApprovalUserScript implements BpmTaskAssignScript {
    @Resource
    private BpmTaskAssignRuleMapper bpmTaskAssignRuleMapper;

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        Set<Long> result = new HashSet<Long>();
        List<String> userList = bpmTaskAssignRuleMapper.getTaskAssignRulesByRoleCode("general_approval");
        for (String user : userList) {
            result.add(Long.valueOf(user));
        }

        return result;
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.GENERAL_APPROVAL;
    }
}
