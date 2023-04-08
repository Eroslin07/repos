package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "用户 APP - 卖家信息创建 Request VO")
@Data
@ToString(callSuper = true)
public class AppSellerInfoReqVO {

    @Schema(description = "车辆明细表id")
    private Long id;

    @Schema(description = "卖家姓名")
    private String sellerName;

    @Schema(description = "卖家身份证号")
    private String sellerIdCard;

    @Schema(description = "卖家电话")
    private String sellerTel;

    @Schema(description = "是否第三方代收（1 是 0 否）")
    private String collection;

    @Schema(description = "卖家付款方式")
    private String payType;

    @Schema(description = "银行卡号")
    private String bankCard;

    @Schema(description = "第三方姓名")
    private String thirdSellerName;

    @Schema(description = "第三方银行卡号")
    private String thirdBankCard;
}