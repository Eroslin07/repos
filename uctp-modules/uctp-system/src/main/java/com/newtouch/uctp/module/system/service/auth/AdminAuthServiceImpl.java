package com.newtouch.uctp.module.system.service.auth;

import cn.hutool.core.util.ObjectUtil;
import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.common.enums.UserTypeEnum;
import com.newtouch.uctp.framework.common.util.monitor.TracerUtils;
import com.newtouch.uctp.framework.common.util.servlet.ServletUtils;
import com.newtouch.uctp.framework.common.util.validation.ValidationUtils;
import com.newtouch.uctp.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.newtouch.uctp.module.system.api.sms.SmsCodeApi;
import com.newtouch.uctp.module.system.api.social.dto.SocialUserBindReqDTO;
import com.newtouch.uctp.module.system.controller.admin.auth.vo.*;
import com.newtouch.uctp.module.system.convert.auth.AuthConvert;
import com.newtouch.uctp.module.system.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.system.dal.dataobject.user.UserExtDO;
import com.newtouch.uctp.module.system.dal.mysql.user.AdminUserMapper;
import com.newtouch.uctp.module.system.enums.logger.LoginLogTypeEnum;
import com.newtouch.uctp.module.system.enums.logger.LoginResultEnum;
import com.newtouch.uctp.module.system.enums.oauth2.OAuth2ClientConstants;
import com.newtouch.uctp.module.system.enums.sms.SmsSceneEnum;
import com.newtouch.uctp.module.system.service.dept.DeptService;
import com.newtouch.uctp.module.system.service.logger.LoginLogService;
import com.newtouch.uctp.module.system.service.member.MemberService;
import com.newtouch.uctp.module.system.service.oauth2.OAuth2TokenService;
import com.newtouch.uctp.module.system.service.social.SocialUserService;
import com.newtouch.uctp.module.system.service.user.AdminUserService;
import com.newtouch.uctp.module.system.service.user.UserExtService;
import com.newtouch.uctp.module.system.util.collection.RASClientUtil;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.newtouch.uctp.module.system.enums.ErrorCodeConstants.*;

/**
 * Auth Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class AdminAuthServiceImpl implements AdminAuthService {

    @Resource
    private AdminUserService userService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private SocialUserService socialUserService;
    @Resource
    private MemberService memberService;
    @Resource
    private Validator validator;
    @Resource
    private CaptchaService captchaService;
    @Resource
    private SmsCodeApi smsCodeApi;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private DeptService deptService;
    @Resource
    private UserExtService userExtService;

    /**
     * 验证码的开关，默认为 true
     */
    @Value("${uctp.captcha.enable:true}")
    private Boolean captchaEnable;

    @Override
    public AdminUserDO authenticate(String username, String password) {
        final LoginLogTypeEnum logTypeEnum = LoginLogTypeEnum.LOGIN_USERNAME;
        // 校验账号是否存在
        AdminUserDO user = userService.getUserByUsername(username);
        if (user == null) {
            createLoginLog(null, username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if (ObjectUtil.notEqual(user.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        return user;
    }

    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {
        // 校验验证码
        validateCaptcha(reqVO);

        // 使用账号密码，进行登录
        AdminUserDO user = authenticate(reqVO.getUsername(), reqVO.getPassword());

        // 如果 socialType 非空，说明需要绑定社交用户
        if (reqVO.getSocialType() != null) {
            socialUserService.bindSocialUser(new SocialUserBindReqDTO(user.getId(), getUserType().getValue(),
                    reqVO.getSocialType(), reqVO.getSocialCode(), reqVO.getSocialState()));
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    @Override
    public AuthLoginRespVO wxLogin(AuthWxLoginReqVO reqVO) {
        AdminUserDO user = userService.getUserByMobile(reqVO.getUsername());
        //查询该手机号是否注册
        if(null==user){
            throw exception(AUTH_MOBILE_NOT_EXIST);
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    @Override
    @Transactional
    public String registerAccount(AuthRegisterReqVO reqVO) {
        //查询该手机号是否注册
        if(userService.getUserByMobile(reqVO.getPhone())!=null){
            throw exception(AUTH_MOBILE_IS_EXIST);
        }
        String decrypt = RASClientUtil.jsencryptDecryptByPrivateKeyLong(reqVO.getPassword());
        try {
            //根据租户id查询商户的父级id
            DeptDO dept = deptService.selectDept(reqVO.getMarketLocation(), "商户方");

            //用户主表
            AdminUserDO userDO = new AdminUserDO();
            userDO.setUsername(reqVO.getPhone());
            userDO.setMobile(reqVO.getPhone());
            userDO.setPassword(encodePassword(decrypt)); // 加密密码
            userDO.setNickname(reqVO.getName());
            userDO.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            userDO.setStatus(1);
            userService.insertUser(userDO);
            //用户扩展表插入数据
            UserExtDO extDO = new UserExtDO();
            extDO.setIdCard(reqVO.getIdCard());
            extDO.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            extDO.setUserId(userDO.getId());
            extDO.setBankAccount(reqVO.getBankNumber());
            extDO.setStaffType("1");
            userExtService.insertUser(extDO);

            //插入商户信息
            DeptDO deptDO = new DeptDO();
            deptDO.setName(reqVO.getBusinessName());
            deptDO.setParentId(dept.getId());
            deptDO.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            deptDO.setSort(2);
            deptDO.setStatus(0);
            deptService.insertDept(deptDO);

//            //保存图片到中间表
//            List<String> carUrl = reqVO.getBusinessLicense();
//            for(int a=0;a<carUrl.size();a++){//车辆图片
//                UctpBusinessFileDO businessFileDO = new UctpBusinessFileDO();
//                businessFileDO.setId(Long.valueOf(carUrl.get(a)));
//                businessFileDO.setMainId(infoDO.getId());
//                businessFileDO.setFileType("1");
//                businessFileService.insert(businessFileDO);
//            }
        }catch (Exception e){
            throw exception(AUTH_REGISTER_ERROR);
        }
        return "";
    }

    @Override
    public void sendSmsCode(AuthSmsSendReqVO reqVO) {
        // 登录场景，验证是否存在
        if (userService.getUserByMobile(reqVO.getMobile()) == null) {
            throw exception(AUTH_MOBILE_NOT_EXISTS);
        }
        // 发送验证码
        smsCodeApi.sendSmsCode(AuthConvert.INSTANCE.convert(reqVO).setCreateIp(getClientIP()));
    }

    @Override
    public AuthLoginRespVO smsLogin(AuthSmsLoginReqVO reqVO) {
        // 校验验证码
        smsCodeApi.useSmsCode(AuthConvert.INSTANCE.convert(reqVO, SmsSceneEnum.ADMIN_MEMBER_LOGIN.getScene(), getClientIP()));

        // 获得用户信息
        AdminUserDO user = userService.getUserByMobile(reqVO.getMobile());
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getMobile(), LoginLogTypeEnum.LOGIN_MOBILE);
    }

    private void createLoginLog(Long userId, String username,
                                LoginLogTypeEnum logTypeEnum, LoginResultEnum loginResult) {
        // 插入登录日志
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logTypeEnum.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(username);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogService.createLoginLog(reqDTO);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            userService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }

    @Override
    public AuthLoginRespVO socialLogin(AuthSocialLoginReqVO reqVO) {
        // 使用 code 授权码，进行登录。然后，获得到绑定的用户编号
        Long userId = socialUserService.getBindUserId(UserTypeEnum.ADMIN.getValue(), reqVO.getType(),
                reqVO.getCode(), reqVO.getState());
        if (userId == null) {
            throw exception(AUTH_THIRD_LOGIN_NOT_BIND);
        }

        // 获得用户
        AdminUserDO user = userService.getUser(userId);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), user.getUsername(), LoginLogTypeEnum.LOGIN_SOCIAL);
    }

    @VisibleForTesting
    void validateCaptcha(AuthLoginReqVO reqVO) {
        // 如果验证码关闭，则不进行校验
        if (!captchaEnable) {
            return;
        }
        // 校验验证码
        ValidationUtils.validate(validator, reqVO, AuthLoginReqVO.CodeEnableGroup.class);
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(reqVO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        // 验证不通过
        if (!response.isSuccess()) {
            // 创建登录失败日志（验证码不正确)
            createLoginLog(null, reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.CAPTCHA_CODE_ERROR);
            throw exception(AUTH_LOGIN_CAPTCHA_CODE_ERROR, response.getRepMsg());
        }
    }

    private AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String username, LoginLogTypeEnum logType) {
        // 插入登陆日志
        createLoginLog(userId, username, logType, LoginResultEnum.SUCCESS);
        // 创建访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, null);
        // 构建返回结果
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public AuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public void logout(String token, Integer logType) {
        // 删除访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(token);
        if (accessTokenDO == null) {
            return;
        }
        // 删除成功，则记录登出日志
        createLogoutLog(accessTokenDO.getUserId(), accessTokenDO.getUserType(), logType);
    }

    private void createLogoutLog(Long userId, Integer userType, Integer logType) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logType);
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(userType);
        if (ObjectUtil.equal(getUserType().getValue(), userType)) {
            reqDTO.setUsername(getUsername(userId));
        } else {
            reqDTO.setUsername(memberService.getMemberUserMobile(userId));
        }
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(LoginResultEnum.SUCCESS.getResult());
        loginLogService.createLoginLog(reqDTO);
    }

    private String getUsername(Long userId) {
        if (userId == null) {
            return null;
        }
        AdminUserDO user = userService.getUser(userId);
        return user != null ? user.getUsername() : null;
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.ADMIN;
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}
