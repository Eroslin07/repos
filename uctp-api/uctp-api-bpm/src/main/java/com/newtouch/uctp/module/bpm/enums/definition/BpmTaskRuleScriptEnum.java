package com.newtouch.uctp.module.bpm.enums.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 任务规则的脚本枚举
 * 目前暂时通过 TODO 芋艿：硬编码，未来可以考虑 Groovy 动态脚本的方式
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BpmTaskRuleScriptEnum {

    START_USER(10L, "流程发起人"),

    LEADER_X1(20L, "流程发起人的一级领导"),
    LEADER_X2(21L, "流程发起人的二级领导"),
    GENERAL_APPROVAL(22L, "一般审批人员"),
    FINANCIAL_APPROVAL(23L, "财务审批员"),
    PAY_ADMIN_APPROVAL(24L, "支付管理员");;

    /**
     * 脚本编号
     */
    private final Long id;
    /**
     * 脚本描述
     */
    private final String desc;

}
