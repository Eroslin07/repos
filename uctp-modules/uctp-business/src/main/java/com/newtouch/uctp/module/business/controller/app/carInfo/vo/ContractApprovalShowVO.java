package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author helong
 * @date 2023/4/24 18:18
 */
@Data
@ToString(callSuper = true)
public class ContractApprovalShowVO {
    /**
     * 合同ID
     */
    private Long contractId;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
     */
    private Integer contractType;
    /**
     * 合同文件ID
     */
    private String contractFileId;
    /**
     * 合同文件路径
     */
    private String contractFilePath;
    /**
     * 合同文件URl地址
     */
    private String contractFileUrl;

}
