package com.newtouch.uctp.module.business.controller.admin.configuration.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.newtouch.uctp.framework.excel.core.annotations.DictFormat;
import com.newtouch.uctp.framework.excel.core.convert.DictConvert;
import com.newtouch.uctp.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName CostExcelVO
 * @Author: zhang
 * @Date 2023/5/13
 */
@Data
public class CostExcelVO {
    @ExcelProperty("序号")
    private Long id;

    @ExcelProperty(value = "费用类型")
    @DictFormat(DictTypeConstants.COST_TYPE)
    private String costType;

    @ExcelProperty("费用（元）")
    private Integer cost;

    @ExcelProperty("车辆类型")
    private String vehicleType;

    @ExcelProperty("税率生效日期始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime effectiveData;

    @ExcelProperty("税率生效日期止")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expirationData;

    @ExcelProperty("操作人")
    private String createdBy;

    @ExcelProperty("操作时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createdTime;

    @ExcelProperty("更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;
}
