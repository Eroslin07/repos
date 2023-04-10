package com.newtouch.uctp.module.business.controller.app.carInfo.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppHomeCarInfoPageReqVO extends PageParam {
    @Schema(description = "车架号/车辆品牌/甲方(卖方)/乙方(买方)")
    private String searchValue;
    @Schema(description = "车辆品牌")
    private String brand;
    @Schema(description = "售卖状态")
    private Integer salesStatus;
    @Schema(description = "检测状态")
    private Integer status;
    @Schema(description = "收车时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] pickUpTime;
    @Schema(description = "售卖时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] salesTime;

    /**
     * 前端传递的时间为 年月，日固定为 1 ，eg:2023-03-01 00:00:00
     * 这里是一个 between 的范围查询
     */
    public void formatLocalDateTime(){
        if (this.pickUpTime != null && this.pickUpTime.length == 2) {
            LocalDateTime pickUpTime0 = this.pickUpTime[0];
            LocalDateTime pickUpTime1 = this.pickUpTime[1];
            this.pickUpTime[0] = pickUpTime0.with(TemporalAdjusters.firstDayOfMonth());
//            this.pickUpTime[1] = pickUpTime1.with(TemporalAdjusters.lastDayOfMonth());
            this.pickUpTime[1] = pickUpTime1.with(TemporalAdjusters.firstDayOfNextMonth());
        }
        if (this.salesTime != null && this.salesTime.length == 2) {
            LocalDateTime salesTime0 = this.salesTime[0];
            LocalDateTime salesTime1 = this.salesTime[1];
            this.salesTime[0] = salesTime0.with(TemporalAdjusters.firstDayOfMonth());
            this.salesTime[1] = salesTime1.with(TemporalAdjusters.firstDayOfNextMonth());
        }
    }
}
