package com.newtouch.uctp.module.business.controller.admin.contract.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName TempExcelVO
 * @Author: zhang
 * @Date 2023/4/20.
 */
@Data
public class TempExcelVO {
    @ExcelProperty("序号")
    private Long id;

    @ExcelProperty("模板编号")
    private String number;

    @ExcelProperty("模板名称")
    private String name;

    @ExcelProperty("类型名")
    private String type;

    @ExcelProperty("说明")
    private String remark;

    @ExcelProperty("状态名")
    private String status;

    @ExcelProperty("操作人")
    private String createdBy;

    @ExcelProperty("创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createdTime;
}
