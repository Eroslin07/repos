package com.newtouch.uctp.module.business.controller.admin.configuration.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName MakeInvoiceRespVO
 * @Author: zhang
 * @Date 2023/4/24.
 */
@Schema(description = "配置管理 - 开票信息配置 Response VO")
@Data
public class MakeInvoiceRespVO {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "纳税人识别号")
    private String taxNum;

    @Schema(description = "法定代表人")
    private String represent;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "电话")
    private String tel;

    @Schema(description = "开户行账号")
    private String opening;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "开户银行")
    private String bank;

    @Schema(description = "银行账号")
    private String bankAccount;

    @Schema(description = "租户编号")
    private Integer tenantId;
}
