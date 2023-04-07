package com.newtouch.uctp.module.system.controller.app.dict;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.system.controller.admin.auth.vo.*;
import com.newtouch.uctp.module.system.service.auth.AdminAuthService;
import com.newtouch.uctp.module.system.service.tenant.TenantService;
import com.newtouch.uctp.module.system.util.collection.OCRUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "管理后台 - 认证")
@RestController
@RequestMapping("/system/appAuth")
@Validated
@Slf4j
public class RegisterController {

    @Resource
    private AdminAuthService authService;

    @Resource
    private TenantService tenantService;


    @PostMapping("/appLogin")
    @PermitAll
    @Operation(summary = "App登录 使用账号密码登录")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> appLogin(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }


    @PostMapping("/orcIdCard")
    @PermitAll
    @Operation(summary = "识别身份证号")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> orcIdCard(@RequestBody Map map) {
        String ak="dzb9KhZMmaTdGd3rbnmXnc0u";
        String sk="K65GO95WOMOyloXZ4ZV72MKEX9EreG5H";
        String idCardUrl = String.valueOf(map.get("IDCardUrl"));
        String accessToken = OCRUtil.getAuth(ak, sk);//获取识别前的token
        String idcard = OCRUtil.idcard(idCardUrl, accessToken);//调取百度识别身份证sdk
        return success(idcard);
    }


    @PostMapping("/orcBusinessLicense")
    @PermitAll
    @Operation(summary = "识别营业执照")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> orcBusinessLicense(@RequestBody Map map) {
        String ak="dzb9KhZMmaTdGd3rbnmXnc0u";
        String sk="K65GO95WOMOyloXZ4ZV72MKEX9EreG5H";
        String businessLicense = String.valueOf(map.get("businessLicense"));
        String accessToken = OCRUtil.getAuth(ak, sk);//获取识别前的token
        String businessLicenseDetail = OCRUtil.businessLicense(businessLicense, accessToken);//调取百度识别营业执照sdk
        return success(businessLicenseDetail);
    }

    @PostMapping("/orcVehicleLicense")
    @PermitAll
    @Operation(summary = "识别行驶证")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> orcVehicleLicense(@RequestBody Map map) {
        String ak="dzb9KhZMmaTdGd3rbnmXnc0u";
        String sk="K65GO95WOMOyloXZ4ZV72MKEX9EreG5H";
        String vehicleLicense = String.valueOf(map.get("vehicleLicense"));
        String accessToken = OCRUtil.getAuth(ak, sk);//获取识别前的token
        String vehicleLicenseDetail = OCRUtil.vehicleLicense(vehicleLicense, accessToken);//调取百度识别营业执照sdk
        return success(vehicleLicenseDetail);
    }


    @PostMapping("/registerAccount")
    @PermitAll
    @Operation(summary = "注册账号")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> registerAccount(@RequestBody @Valid AuthRegisterReqVO reqVO) {
        authService.registerAccount(reqVO);
        return success("提交成功");
    }



    @GetMapping("/getTenantlist")
    @Operation(summary = "获得租户下拉")
//    @PreAuthorize("@ss.hasPermission('system:tenant:getTenantlist')")
    public CommonResult<List> getTenantlist() {
        List list = tenantService.getTenantList();
        return success(list);
    }



}
