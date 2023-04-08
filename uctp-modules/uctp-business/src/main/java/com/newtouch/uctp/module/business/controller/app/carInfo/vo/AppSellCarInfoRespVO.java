package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
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
    private BigDecimal estimatedCost;
    @Schema(description = "利润(单位：元)")
    private BigDecimal profit;
    @Schema(description = "卖车方式")
    private Integer sellType;
    @Schema(description = "卖车金额")
    private BigDecimal sellAmount;

}
