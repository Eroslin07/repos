package com.newtouch.uctp.module.business.controller.app.notice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * 消息 VO
 */
@Data
@ToString(callSuper = true)
public class NoticeVO {
    @Schema(description = "消息ID")
    private String id;
}
