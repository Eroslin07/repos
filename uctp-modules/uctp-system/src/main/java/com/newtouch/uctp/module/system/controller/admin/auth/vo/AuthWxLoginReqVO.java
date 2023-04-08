package com.newtouch.uctp.module.system.controller.admin.auth.vo;

import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.common.validation.InEnum;
import com.newtouch.uctp.module.system.enums.social.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Schema(description = "管理后台 -微信登录")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthWxLoginReqVO {

    @Schema(description = "账号", required = true, example = "uctpyuanma")
    @NotEmpty(message = "登录账号不能为空")
    private String username;


}
