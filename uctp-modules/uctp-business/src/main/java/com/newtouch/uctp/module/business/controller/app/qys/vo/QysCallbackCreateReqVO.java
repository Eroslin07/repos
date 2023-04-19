package com.newtouch.uctp.module.business.controller.app.qys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description ="管理后台 - 契约锁回调日志创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QysCallbackCreateReqVO extends QysCallbackBaseVO {

}
