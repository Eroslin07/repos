package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

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
    private LocalDateTime firstRegistDate;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "里程数,保留2位小数")
    private BigDecimal mileage;
    @Schema(description = "运营性质")
    private String natureOfOperat;
    @Schema(description = "车牌号")
    private String plateNum;
}
