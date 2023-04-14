package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.newtouch.uctp.module.business.dal.dataobject.FeesAndCommitments;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "用户 APP - 车辆售卖详情 Response VO")
@Data
@ToString(callSuper = true)
public class AppSellCarInfoReqVO {
    @Schema(description = "车辆主表id")
    private Long id;
    @Schema(description = "其他信息")
    private String remarks;
    @Schema(description = "卖车金额")
    private BigDecimal sellAmount;
    @Schema(description = "买家身份证")
    private String buyerIdCard;
    @Schema(description = "买家姓名")
    private String buyerName;
    @Schema(description = "买家电话")
    private String buyerTel;
    @Schema(description = "买家地址")
    private String buyerAdder;
    @Schema(description = "收款方式")
    private Integer sellType;
    @Schema(description = "身份证正反面图片id")
    private List<Long> IdCardIds;
    @Schema(description = "转入地车辆管理所名称")
    private String transManageName;
    @Schema(description = "车况相关")
    private FeesAndCommitments feesAndCommitments;

}
