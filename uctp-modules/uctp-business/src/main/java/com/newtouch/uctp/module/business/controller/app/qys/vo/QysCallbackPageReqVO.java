package com.newtouch.uctp.module.business.controller.app.qys.vo;

import com.newtouch.uctp.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.newtouch.uctp.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description ="管理后台 - 契约锁回调日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QysCallbackPageReqVO extends PageParam {

    @Schema(description = "乐观锁")
    private Integer revision;

    @Schema(description = "契约锁回调类型")
    private Byte type;

    @Schema(description = "契约锁回调内容")
    private String content;

    @Schema(description = "关联业务Id")
    private Long mainId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

}
