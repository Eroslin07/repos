package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class AppCarInvoiceVo {

    @Schema(description = "发票ID")
    private String invoiceId;

    @Schema(description = "发票类型(0反向二手车销售统一发票 1正向二手车销售统一发票 2二手车增值税发票)")
    private String invoiceType;

    @Schema(description = "商户")
    private String business;

    @Schema(description = "状态(0待开票 1已开票)")
    private String status;

    @Schema(description = "合同编号")
    private String contractNo;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "合同金额")
    private String amountMoney;

    @Schema(description = "税额")
    private String tax;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    private Date createdTime;

    @Schema(description = "更新人")
    private Long updatedBy;

    @Schema(description = "更新时间")
    private Date updatedTime;

    @Schema(description = "发票地址")
    private String url;
}
