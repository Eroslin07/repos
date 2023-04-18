package com.newtouch.uctp.module.business.controller.app.qys.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 契约锁分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QysConfigPageReqVO extends PageParam {

    @Schema(description = "乐观锁")
    private Integer revision;

    @Schema(description = "访问地址")
    private String serverUrl;

    @Schema(description = "访问秘钥")
    private String accessKey;

    @Schema(description = "访问密匙")
    private String accessSecret;

    @Schema(description = "状态(0未启用 1启用)")
    private Integer status;

    @Schema(description = "商户名称")
    private String businessName;

    @Schema(description = "商户Id")
    private Long businessId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
