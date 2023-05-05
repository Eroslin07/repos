package com.newtouch.uctp.module.business.controller.admin.contract.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName RecordExportReqVO
 * @Author: zhang
 * @Date 2023/4/23.
 */
@Schema(description = "合同管理 - 合同档案")
@Data
public class RecordExportReqVO {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "合同编号")
    private Integer number;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "甲方")
    private String first;

    @Schema(description = "乙方")
    private String second;

    @Schema(description = "商户")
    private String merchant;

    @Schema(description = "合同类型")
    private String type;

    @Schema(description = "签约金额")
    private String amount;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "发起时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime initiationTime;

    @Schema(description = "签约时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime signTime;
}
