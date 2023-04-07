package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppHomeCarInfoPageReqVO extends PageParam {
//    @Schema(description = "车架号")
//    private String vin;
//    @Schema(description = "车辆品牌")
//    private String brand;
//    @Schema(description = "甲方(卖方)")
//    private String firstParty;
//    @Schema(description = "乙方(买方)")
//    private String secondParty;
    @Schema(description = "车架号/车辆品牌/甲方(卖方)/乙方(买方)")
    private String searchValue;
    @Schema(description = "车辆品牌")
    private String brand;
    @Schema(description = "售卖状态")
    private Integer salesStatus;
    @Schema(description = "检测状态")
    private Integer checkStatus;
    @Schema(description = "收车时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] pickUpTime;
    @Schema(description = "售卖时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] salesTime;
}
