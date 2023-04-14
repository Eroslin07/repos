package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "用户 APP - 卖家信息创建 Request VO")
@Data
@ToString(callSuper = true)
public class AppSellerInfoReqVO {

    @Schema(description = "车辆明细表id")
    private Long id;

    @Schema(description = "收车金额")
    private BigDecimal vehicleReceiptAmount;

    @Schema(description = "付款方式")
    private String payType;

    @Schema(description = "转入地车辆管理所名称")
    private String transManageName;

    @Schema(description = "是否第三方代收")
    private String collection;

    @Schema(description = "卖家身份证号")
    private String sellerIdCard;

    @Schema(description = "卖家身份证url")
    private List<String> idCardUrl;

    @Schema(description = "卖家姓名")
    private String sellerName;

    @Schema(description = "卖家地址")
    private String sellerAdder;


    @Schema(description = "卖家电话")
    private String sellerTel;

    @Schema(description = "收款方式")
    private String remitType;

    @Schema(description = "银行卡号")
    private String bankCard;

    @Schema(description = "第三方姓名")
    private String thirdSellerName;

    @Schema(description = "开户行")
    private String bankName;

    @Schema(description = "第三方银行卡号")
    private String thirdBankCard;
}
