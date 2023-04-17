package com.newtouch.uctp.module.bpm.enums.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程定义类型
 * @author helong
 * @date 2023/4/17 15:42
 */
@Getter
@AllArgsConstructor
public enum BpmDefTypeEnum {
    ZHSQ("账号申请流程"),
    SGYZ("收车价格超公允值审批流程"),
    SCKP("反向二手车统一发票待办开票审批流程"),
    SCGH("车辆是否已过户至平台流程"),
    SKZH("收车款支付失败流程"),
    MGYZ("卖车价格超公允值审批流程"),
    MCKP("正向二手车统一发票和增值税发票待办开票流程"),
    MCGH("车辆是否已过户至买家审批流程"),
    LRTX("利润提取待办审批流程");

    /**
     * 描述
     */
    private final String desc;
}
