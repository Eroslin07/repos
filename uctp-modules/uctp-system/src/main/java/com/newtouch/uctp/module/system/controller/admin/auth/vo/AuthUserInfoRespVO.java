package com.newtouch.uctp.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Schema(description = "我的 个人信息vo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserInfoRespVO {


    @Schema(description = "姓名")
    private String nickname;
    @Schema(description = "身份证号码")
    private String idCard;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "营业执照号")
    private String taxNum;
    @Schema(description = "商户名称")
    private String deptName;
    @Schema(description = "法定代表人")
    private String legalRepresentative;
    @Schema(description = "市场所在地")
    private String tenantName;
    @Schema(description = "开户行")
    private String bankName;
    @Schema(description = "对公银行账号")
    private String bankAccount;
    @Schema(description = "保证金开户行")
    private String bondBankName;
    @Schema(description = "保证金充值卡号")
    private String bondBankAccount;
    @Schema(description = "联系地址")
    private String address;
    @Schema(description = "交易方式")
    private Integer paymentType;



}
