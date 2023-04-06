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

@Schema(description = "管理后台 - 注册账号 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRegisterReqVO {

    @Schema(description = "身份证号", required = true, example = "513021")
//    @NotEmpty(message = "身份证号不能为空")
    private String idCard;

    @Schema(description = "身份证url", required = true, example = "")
    private String idCardUrl;

    @Schema(description = "姓名", required = true, example = "张三")
//    @NotEmpty(message = "姓名不能为空")
    private String name;

    @Schema(description = "手机号", required = true, example = "13888888888")
//    @NotEmpty(message = "手机号不能为空")
    private String phone;


    @Schema(description = "营业执照url", required = true, example = "")
    private String businessLicense;

    @Schema(description = "市场所在地(租户id)", required = true, example = "130")
//    @NotEmpty(message = "市场所在地不能为空")
    private String marketLocation;

    @Schema(description = "银行卡账号", required = true, example = "211")
//    @NotEmpty(message = "银行卡账号不能为空")
    private String bankNumber;


    @Schema(description = "密码", required = true, example = "buzhidao")
    @NotEmpty(message = "密码不能为空")
    private String password;


    @Schema(description = "商户名称", required = true, example = "XX车行")
    private String businessName;

    @Schema(description = "税号", required = true, example = "123456")
    private String taxNum;
}
