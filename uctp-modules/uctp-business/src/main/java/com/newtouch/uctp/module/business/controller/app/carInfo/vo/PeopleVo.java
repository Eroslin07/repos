package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class PeopleVo {

    @Schema(description = "卖家身份证号")
    private String sellerIdCard;
    @Schema(description = "卖家姓名")
    private String sellerName;
    @Schema(description = "卖家电话")
    private String sellerTel;
    @Schema(description = "是否第3方代收")
    private String collection;
    @Schema(description = "第3方卖家姓名")
    private String thirdSellerName;
    @Schema(description = "卖家收款方式")
    private String remitType;
    @Schema(description = "第3方卖家银行卡号")
    private String thirdBankCard;

    @Schema(description = "买家身份证号")
    private String buyerIdCard;
    @Schema(description = "买家姓名")
    private String buyerName;
    @Schema(description = "买家电话")
    private String buyerTel;



}
