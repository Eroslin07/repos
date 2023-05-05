package com.newtouch.uctp.module.business.controller.admin.contract.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @ClassName RecordReqVO
 * @Author: zhang
 * @Date 2023/4/21.
 */
@Schema(description = "合同管理 - 合同档案分页项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecordReqVO extends PageParam {
    @Schema(description = "序号")
    private Long id;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "商户")
    private String merchant;

    @Schema(description = "合同类型")
    private Integer type;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "发起时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime initiationTime;
}
