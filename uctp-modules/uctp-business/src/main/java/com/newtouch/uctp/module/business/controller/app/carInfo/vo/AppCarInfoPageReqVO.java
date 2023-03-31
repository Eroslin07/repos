package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import com.newtouch.uctp.framework.common.pojo.PageParam;

@Schema(description = "用户 APP - 车辆主表 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCarInfoPageReqVO extends PageParam {

    @Schema(description = "车架号", required = true, example = "666")
    private String vin;

    @Schema(description = "车辆品牌", required = true, example = "666")
    private String brand;

    @Schema(description = "车辆年份")
    private String year;

    @Schema(description = "车辆型号")
    private String style;

    @Schema(description = "发动机编号")
    private String engineNum;

    @Schema(description = "收车金额")
    private String vehicleReceiptAmount;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "乐观锁")
    private Integer revision;

    @Schema(description = "汽车明细id")
    private Long carDetailId;

    @Schema(description = "商户id")
    private Long businessId;

    @Schema(description = "创建时间")
    private Date[] createTime;

    @Schema(description = "更新时间")
    private Date[] updatedTime;

    @Schema(description = "售卖状态(0未出售 1出售中 2已出售)")
    private Integer salesStatus;

    @Schema(description = "检测状态(0未检测 1已检测)")
    private Integer checkStatus;

}
