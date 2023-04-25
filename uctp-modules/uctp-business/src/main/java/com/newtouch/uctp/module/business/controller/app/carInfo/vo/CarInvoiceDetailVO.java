package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.google.common.collect.Lists;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 流程需要的发票明细
 * @author qjj
 * @date 2023/4/25 11:00
 */
@Schema(description = "用户 APP - 流程发票明细 VO")
@Data
@ToString(callSuper = true)
public class CarInvoiceDetailVO implements Serializable {

    @Schema(description = "买车单位/个人")
    private String buyerName;
    @Schema(description = "单位代码/身份证号码")
    private String buyerIdCard;
    @Schema(description = "买车单位/个人住址")
    private String buyerAddress;
    @Schema(description = "电话")
    private String buyerTel;
    @Schema(description = "卖车单位/个人")
    private String sellerName;
    @Schema(description = "单位代码/身份证号码")
    private String sellerIdCard;
    @Schema(description = "卖车单位/个人住址")
    private String sellerAddress;
    @Schema(description = "电话")
    private String sellerTel;
    @Schema(description = "车牌号")
    private String plateNum;
    @Schema(description = "登记证书")
    private String certificateNo;
    @Schema(description = "车辆类型")
    private String carType;
    @Schema(description = "车架号/车辆识别码")
    private String vin;
    @Schema(description = "厂牌型号")
    private String model;
    @Schema(description = "转入地车辆管理所名称")
    private String transManageName;
    @Schema(description = "车辆价款")
    private BigDecimal sellAmount;
    @Schema(description = "厂牌型二手市场号")
    private String marketName;
    @Schema(description = "纳税人识别号")
    private String taxNum;
    @Schema(description = "地址")
    private String marketAddress;
    @Schema(description = "开户行账号")
    private String marketBankNum;
    @Schema(description = "电话")
    private String marketTel;





}
