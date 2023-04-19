package com.newtouch.uctp.module.business.controller.app.qys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 契约锁回调日志 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class QysCallbackBaseVO {

    @Schema(description = "乐观锁")
    private Integer revision;

    @Schema(description = "契约锁回调类型")
    private Byte type;

    @Schema(description = "契约锁回调内容")
    private String content;

    @Schema(description = "关联业务Id")
    private Long mainId;

}
