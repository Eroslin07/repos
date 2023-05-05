package com.newtouch.uctp.module.business.controller.admin.contract.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName RecordExport
 * @Author: zhang
 * @Date 2023/4/23.
 */
@Data
public class RecordExport {
    @ExcelProperty("序号")
    private Long id;

    @ExcelProperty("合同编号")
    private Integer number;

    @ExcelProperty("合同名称")
    private String name;

    @ExcelProperty("甲方")
    private String first;

    @ExcelProperty("乙方")
    private String second;

    @ExcelProperty("商户")
    private String merchant;

    @ExcelProperty("合同类型")
    private String type;

    @ExcelProperty("签约金额")
    private String amount;

    @ExcelProperty("状态")
    private String status;

    @ExcelProperty("发起时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime initiationTime;

    @ExcelProperty("签约时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime signTime;
}
