package com.newtouch.uctp.module.business.controller.admin.settlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.newtouch.uctp.framework.common.pojo.PageParam;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author helong
 * @date 2023/4/11 16:03
 */
@Schema(description = "结束中心 - 发票管理分页项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InvoiceReqVO extends PageParam {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "发票类型")
    private Integer type;

    @Schema(description = "商户")
    private String merchant;

    @Schema(description = "合同编号")
    private String number;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "合同金额")
    private String amount;

    @Schema(description = "税额")
    private String tax;

    @Schema(description = "发票状态")
    private Integer status;


    @Schema(description = "创建人")
    private String founder;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime creationTime;
}
