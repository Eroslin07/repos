package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.google.common.collect.Lists;
import com.newtouch.uctp.module.business.dal.dataobject.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 车辆售卖详情 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppSellCarInfoRespVO extends AppCarInfoBaseVO {
    @Schema(description = "车辆主表id")
    private String id;
    @Schema(description = "车辆图片列表")
    private List<String> carPicList;
    @Schema(description = "行驶证列表")
    private List<String> drivingPicList;
    @Schema(description = "买家身份证图片(卖车时)")
    private List<AppSimpleFileVO> idCardsPicList = Lists.newArrayList();
    @Schema(description = "机动车登记证书列表")
    private List<String> registerPicList;
    @Schema(description = "预计费用(单位：元)")
    private BigDecimal total = new BigDecimal("0");
    @Schema(description = "利润(单位：元)")
    private BigDecimal profit = new BigDecimal("0");
    @Schema(description = "卖车方式")
    private Integer sellType;
    @Schema(description = "卖车金额")
    private BigDecimal sellAmount;
    @Schema(description = "创建时间")
    private String firstRegistDate;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "里程数,保留2位小数")
    private BigDecimal mileage;
    @Schema(description = "运营性质")
    private String natureOfOperat;
    @Schema(description = "车牌号")
    private String plateNum;
    @Schema(description = "颜色")
    private String colour;
    @Schema(description = "使用年限至")
    private LocalDateTime scrapDate;
    @Schema(description = "年检签证有效期至")
    private LocalDateTime annualInspectionDate;
    @Schema(description = "保险险种")
    private String insurance;
    @Schema(description = "保险期至")
    private String insuranceEndData;
    @Schema(description = "其他")
    private String other;
    @Schema(description = "收车车辆手续及备件")
    private ProceduresAndSpareParts proceduresAndSpareParts;
    @Schema(description = "卖车车辆手续及备件")
    private ProceduresAndSpareSell proceduresAndSpareSell;
    @Schema(description = "其他费用及约定")
    private FeesAndCommitments feesAndCommitments;
    @Schema(description = "车况相关")
    private VehicleProblem vehicleProblem;
    @Schema(description = "转入地车辆管理所名称")
    private String transManageName;
    @Schema(description = "车辆类型")
    private String carType;
    @Schema(description = "车辆型号")
    private String model;
    @Schema(description = "登记证书编号")
    private String certificateNo;
    @Schema(description = "品牌型号")
    private String brandType;
    @Schema(description = "车型id")
    private String modelId;
    @Schema(description = "买家电话")
    private String buyerTel;
    @Schema(description = "买家身份证")
    private String buyerIdCard;
    @Schema(description = "买家姓名")
    private String buyerName;
    @Schema(description = "买家地址")
    private String buyerAdder;
    @Schema(description = "定金")
    private String deposit;
    @Schema(description = "尾款")
    private String balancePayment;
    @Schema(description = "pos机信息")
    private PosDO posDO;

}
