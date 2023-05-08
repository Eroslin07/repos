package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.newtouch.uctp.module.business.dal.dataobject.FeesAndCommitments;
import com.newtouch.uctp.module.business.dal.dataobject.ProceduresAndSpareSell;
import com.newtouch.uctp.module.business.dal.dataobject.VehicleProblem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "用户 APP - 车辆售卖详情 Response VO")
@Data
@ToString(callSuper = true)
public class AppSellCarInfoReqVO {
    @Schema(description = "车辆主表id")
    @NotNull(message = "id不能为空")
    private Long id;
    @Schema(description = "其他信息")
    private String remarks;
    @Schema(description = "卖车金额（单位:元）")
//    @NotNull(message = "卖车金额不能为空")
//    @DecimalMin(value = "0", inclusive = false, message = "卖车金额必须大于零")
    private BigDecimal sellAmount;
    @Schema(description = "买家身份证")
//    @NotNull(message = "买家身份证不能为空")
//    @Pattern(regexp = "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/", message = "请输入正确的身份证")
    private String buyerIdCard;
    @Schema(description = "买家姓名")
//    @NotNull(message = "买家姓名不能为空")
    private String buyerName;
    @Schema(description = "买家电话")
//    @NotNull(message = "买家电话不能为空")
//    @Pattern(regexp = "/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$/", message = "请输入正确的电话号码")
    private String buyerTel;
    @Schema(description = "买家地址")
//    @NotNull(message = "买家地址不能为空")
    private String buyerAdder;
    @Schema(description = "收款方式")
//    @NotNull(message = "收款方式不能为空")
    private Integer sellType;
    @Schema(description = "定金")
    private BigDecimal deposit;
    @Schema(description = "尾款")
    private BigDecimal balancePayment;
    @Schema(description = "身份证正反面图片id")
    private List<Long> IdCardIds;
    @Schema(description = "转入地车辆管理所名称")
    private String transManageName;
    @Schema(description = "其他费用及约定")
//    @NotNull(message = "其他费用及约定不能为空")
    private FeesAndCommitments feesAndCommitments;
    @Schema(description = "车况相关")
//    @NotNull(message = "车况相关不能为空")
    private VehicleProblem vehicleProblem;
    @Schema(description = "车辆手续及备件(卖车)")
//    @NotNull(message = "车辆手续及备件不能为空")
    private ProceduresAndSpareSell proceduresAndSpareSell;
    /**
     * 其他
     */
    @Schema(description = "车辆手续及备件(卖车)")
    private String other;

    @Schema(description = "卖车公允价值")
    private String sellCarFair;
}
