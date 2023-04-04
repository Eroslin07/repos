package com.newtouch.uctp.module.business.controller.app.carinfodetails.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 车辆明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarInfoDetailsPageReqVO extends PageParam {
    @Schema(description = "乐观锁")
    private Integer revision;

    @Schema(description = "车辆主表id")
    private String carId;

    @Schema(description = "里程数,保留2位小数")
    private BigDecimal mileage;

    @Schema(description = "是/非事故车(1是 0非)")
    private Integer accidentVehicle;

    @Schema(description = "是/非泡水车(1是 0非)")
    private Integer soakingCar;

    @Schema(description = "是/非火烧车(1是 0非)")
    private Integer burnCar;

    @Schema(description = "首次/初次登记日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] firstRegistDate;

    @Schema(description = "行驶证编号")
    private String drivingLicense;

    @Schema(description = "登记证书编号")
    private String certificateNo;

    @Schema(description = "能源类型(1燃油 2新能源 3混动)")
    private Integer energyType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}
