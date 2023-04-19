package com.newtouch.uctp.module.system.controller.app.dict;

import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.util.collection.SetUtils;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.framework.security.config.SecurityProperties;
import com.newtouch.uctp.module.system.controller.admin.auth.vo.*;
import com.newtouch.uctp.module.system.convert.auth.AuthConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.dal.dataobject.permission.MenuDO;
import com.newtouch.uctp.module.system.dal.dataobject.permission.RoleDO;
import com.newtouch.uctp.module.system.dal.dataobject.tenant.TenantDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.enums.logger.LoginLogTypeEnum;
import com.newtouch.uctp.module.system.enums.permission.MenuTypeEnum;
import com.newtouch.uctp.module.system.service.auth.AdminAuthService;
import com.newtouch.uctp.module.system.service.dept.DeptService;
import com.newtouch.uctp.module.system.service.permission.PermissionService;
import com.newtouch.uctp.module.system.service.permission.RoleService;
import com.newtouch.uctp.module.system.service.tenant.TenantService;
import com.newtouch.uctp.module.system.service.user.AdminUserService;
import com.newtouch.uctp.module.system.util.collection.OCRUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils.obtainAuthorization;
import static java.util.Collections.singleton;

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


    @GetMapping("/get-permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        // 获得用户信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        DeptDO dept = deptService.getDept(user.getDeptId());
        TenantDO tenant = tenantService.getTenant(user.getTenantId());
        if (user == null) {
            return null;
        }
        // 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdsFromCache(getLoginUserId(), singleton(CommonStatusEnum.ENABLE.getStatus()));
        List<RoleDO> roleList = roleService.getRoleListFromCache(roleIds);
        // 获得菜单列表
        List<MenuDO> menuList = permissionService.getRoleMenuListFromCache(roleIds,
                SetUtils.asSet(MenuTypeEnum.DIR.getType(), MenuTypeEnum.MENU.getType(), MenuTypeEnum.BUTTON.getType()),
                singleton(CommonStatusEnum.ENABLE.getStatus())); // 只要开启的
        // 拼接结果返回
        return success(AuthConvert.INSTANCE.convert(user,dept,tenant, roleList, menuList));
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



}
