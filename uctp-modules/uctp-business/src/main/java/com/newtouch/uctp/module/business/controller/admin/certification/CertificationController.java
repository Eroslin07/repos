package com.newtouch.uctp.module.business.controller.admin.certification;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.io.FileNotFoundException;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * @ClassName CertificationController
 * @Author: zhang
 * @Date 2023/5/11.
 */

@Tag(name =  "管理后台 - 认证管理")
@RestController
@RequestMapping("/uctp/certification")
@Validated
public class CertificationController {
    @Resource
    private QysConfigService qysConfigService;

    @PostMapping("/enterprise")
    @PermitAll
    @Operation(summary ="企业认证")
    @PreAuthorize("@ss.hasPermission('uctp:certification:enterprise')")
    @Parameter(name = "userId", description = "用户id", required = true, example = "1024")
    public CommonResult<Boolean> companyAuth(@RequestParam("userId") Long userId) throws FileNotFoundException {
        qysConfigService.companyAuth(userId);
        return success(true);
    }

    @PostMapping("/individual")
    @PermitAll
    @Operation(summary ="个人认证")
    @PreAuthorize("@ss.hasPermission('uctp:certification:individual')")
    @Parameter(name = "userId", description = "用户id", required = true, example = "1024")
    public CommonResult<Boolean> userAuth(@RequestParam("userId") Long userId){
        qysConfigService.userAuth(userId);
        return success(true);
    }
}
