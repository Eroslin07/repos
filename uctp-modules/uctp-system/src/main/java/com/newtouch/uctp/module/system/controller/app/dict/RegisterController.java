package com.newtouch.uctp.module.system.controller.app.dict;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.framework.security.config.SecurityProperties;
import com.newtouch.uctp.module.system.controller.admin.auth.vo.*;
import com.newtouch.uctp.module.system.convert.auth.AuthConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.dal.dataobject.tenant.TenantDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.UserExtDO;
import com.newtouch.uctp.module.system.enums.logger.LoginLogTypeEnum;
import com.newtouch.uctp.module.system.service.auth.AdminAuthService;
import com.newtouch.uctp.module.system.service.dept.DeptService;
import com.newtouch.uctp.module.system.service.permission.PermissionService;
import com.newtouch.uctp.module.system.service.permission.RoleService;
import com.newtouch.uctp.module.system.service.tenant.TenantService;
import com.newtouch.uctp.module.system.service.user.AdminUserService;
import com.newtouch.uctp.module.system.service.user.UserExtService;
import com.newtouch.uctp.module.system.util.collection.OCRUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils.obtainAuthorization;

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

    @Resource
    private AdminUserService userService;

    @Resource
    private DeptService deptService;

    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private UserExtService userExtService;


    @PostMapping("/appLogin")
    @PermitAll
    @Operation(summary = "App登录 使用账号密码登录")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> appLogin(@RequestBody @Valid AuthLoginReqVO reqVO) {
        return success(authService.login(reqVO));
    }

    @PostMapping("/wxLogin")
    @PermitAll
    @Operation(summary = "微信登录 使用账号密码登录")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> wxLogin(@RequestBody @Valid AuthWxLoginReqVO reqVO) {
        return success(authService.wxLogin(reqVO));
    }

    @GetMapping("/wxToken")
    @Operation(summary = "获取微信token")
    public CommonResult<String> wxToken(@RequestParam("code") String code) {
        String authHost = "https://api.weixin.qq.com/cgi-bin/token?";
        String getPhone = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?";
        String appId="wx9decec45b7374b90";
        String secret="45323149c53d4340dfad4a304803eeaf";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credential"
                // 2. 官网获取的 API Key
                + "&appid=" + appId
                // 3. 官网获取的 Secret Key
                + "&secret=" + secret;
        String access_token="";
        String phoneNumber="";
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            access_token = jsonObject.getString("access_token");


            String getUserPhoneUrl = getPhone
                    // 1. grant_type为固定参数
                    + "&access_token=" + access_token;
            HashMap<String, Object> requestParam = new HashMap<>();
            // 手机号调用凭证
            requestParam.put("code", code);
            String jsonStr = JSONUtil.toJsonStr(requestParam);
            String result1 = HttpRequest.post(getUserPhoneUrl) .body(jsonStr).execute().body();

            /**
             * 返回结果示例
             */
            System.err.println("result:" + result1);
            JSONObject phoneJsonObject = JSON.parseObject(result1);
            String phone_info = phoneJsonObject.getString("phone_info");
            System.out.println(phone_info);
            JSONObject jsonObject1 = JSON.parseObject(phone_info);
            phoneNumber = jsonObject1.getString("phoneNumber");
            System.out.println(phoneNumber);
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return success(phoneNumber);
    }


    @GetMapping("/get-permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        // 获得用户信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        DeptDO dept = deptService.getDept(user.getDeptId());
        UserExtDO userExtDOS = userExtService.selectByUserId(user.getId()).get(0);
        TenantDO tenant = tenantService.getTenant(user.getTenantId());
        if (user == null) {
            return null;
        }
        // 拼接结果返回
        return success(AuthConvert.INSTANCE.convert(user,userExtDOS,dept,tenant));
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = obtainAuthorization(request, securityProperties.getTokenHeader());
        if (StrUtil.isNotBlank(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF.getType());
        }
        return success(true);
    }


    @PostMapping("/orcIdCard")
    @PermitAll
    @Operation(summary = "识别身份证号")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> orcIdCard(@RequestBody Map map) {
//        String ak="iV1qdyTyr8LG3KD7cZBYpxIh";
//        String sk="LdSEwbqTNmmCjkPbixW4YZP0xXonMGYv";
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


    @PostMapping("/orcVehicleRegistrationCertificate")
    @PermitAll
    @Operation(summary = "识别车辆登记证书")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<String> orcVehicleRegistrationCertificate(@RequestBody Map map) {
        String ak="dzb9KhZMmaTdGd3rbnmXnc0u";
        String sk="K65GO95WOMOyloXZ4ZV72MKEX9EreG5H";
        String vehicleRegistrationCertificate = String.valueOf(map.get("vehicleRegistrationCertificate"));
        String accessToken = OCRUtil.getAuth(ak, sk);//获取识别前的token
        String vehicleLicenseDetail = OCRUtil.vehicleRegistrationCertificate(vehicleRegistrationCertificate, accessToken);//调取百度识别营业执照sdk
        return success(vehicleLicenseDetail);
    }


    @PostMapping("/registerAccount")
    @PermitAll
    @Operation(summary = "注册账号")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Map> registerAccount(@RequestBody @Valid AuthRegisterReqVO reqVO) {
        Map map = authService.registerAccount(reqVO);
        return success(map);
    }


    @GetMapping("/getTenantlist")
    @Operation(summary = "获得租户下拉")
//    @PreAuthorize("@ss.hasPermission('system:tenant:getTenantlist')")
    public CommonResult<List> getTenantlist() {
        List list = tenantService.getTenantListWithType(1,1L);
        return success(list);
    }


    @GetMapping("/getUserInfo")
    @Operation(summary = "获取个人信息")
    @Parameter(name = "userId", description = "用户id", required = true, example = "1650085024672796674")
    public CommonResult<AuthUserInfoRespVO> getUserInfo(@RequestParam("userId") Long userId) {
        return success(authService.getUserInfo(userId));
    }



    @PostMapping("/addAccount")
    @PermitAll
    @Operation(summary = "新增&修改子账号")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Map> addAccount(@RequestBody @Valid AddAccountReqVO reqVO) {
        Map map = authService.addAccount(reqVO);
        return success(map);
    }


    @GetMapping("/deleteAccount")
    @Operation(summary = "删除子账号")
    @Parameter(name = "userId", description = "用户id", required = true, example = "1650085024672796674")
    public CommonResult<Integer> deleteAccount(@RequestParam("userId") Long userId) {
        return success(authService.deleteAccount(userId));
    }

    @GetMapping("/getAccountList")
    @Operation(summary = "获取子账号列表")
    @Parameter(name = "deptId", description = "商户id", required = true, example = "263")
    public CommonResult<List<AddAccountRespVO>> getAccountList(@RequestParam("deptId") Long deptId) {
        return success(authService.getAccountList(deptId));
    }
}
