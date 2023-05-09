package com.newtouch.uctp.module.system.service.auth;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.annotations.VisibleForTesting;
import com.newtouch.uctp.framework.common.enums.CommonStatusEnum;
import com.newtouch.uctp.framework.common.enums.UserTypeEnum;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.util.monitor.TracerUtils;
import com.newtouch.uctp.framework.common.util.servlet.ServletUtils;
import com.newtouch.uctp.framework.common.util.validation.ValidationUtils;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.module.business.api.file.BusinessFileApi;
import com.newtouch.uctp.module.business.api.file.dto.FileInsertReqDTO;
import com.newtouch.uctp.module.business.api.qys.QysConfigApi;
import com.newtouch.uctp.module.business.api.qys.dto.QysConfigDTO;
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
import com.newtouch.uctp.module.system.dal.mysql.user.UserExtMapper;
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
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Resource
    private BusinessFileApi businessFileApi;
    @Resource
    private UserExtMapper userExtMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    @Lazy
    private QysConfigApi qysConfigApi;
    @Resource
    @Lazy
    private QiyuesuoClientFactory qiyuesuoClientFactory;

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
        AdminUserDO user = userService.selectByMobileAndStatus(reqVO.getUsername(),0);
        //查询该手机号是否注册
        if(null==user){
            throw exception(AUTH_MOBILE_NOT_EXIST);
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME);
    }

    @Override
    public AuthUserInfoRespVO getUserInfo(Long userId) {
        AuthUserInfoRespVO infoRespVO = new AuthUserInfoRespVO();
        UserExtDO userExtDO=new UserExtDO();
        AdminUserDO user = userService.getUser(userId);
        DeptDO dept = deptService.getDept(user.getDeptId());
        //拿到父级
        DeptDO parentDept = deptService.selectByParent(String.valueOf(dept.getTenantId()), 0L);
        List<UserExtDO> userExtDOS = userExtMapper.selectByUserId(userId);
        if(userExtDOS.size()>0){
            userExtDO = userExtDOS.get(0);
        }
        infoRespVO.setNickname(user.getNickname());
        infoRespVO.setIdCard(userExtDO.getIdCard());
        infoRespVO.setPhone(user.getMobile());
        infoRespVO.setTaxNum(dept.getTaxNum());
        infoRespVO.setDeptName(dept.getName());
        infoRespVO.setLegalRepresentative(dept.getLegalRepresentative());
        infoRespVO.setTenantName(parentDept.getName());
        infoRespVO.setBankName(userExtDO.getBankName());
        infoRespVO.setBankAccount(userExtDO.getBankAccount());
        infoRespVO.setBondBankAccount(userExtDO.getBondBankAccount());
        infoRespVO.setAddress(dept.getAddress());
        return infoRespVO;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Map registerAccount(AuthRegisterReqVO reqVO) {
        //查询是否有未注册数据，有则删除
        List<AdminUserDO> userDOS = userService.selectIsExist(reqVO.getPhone(), 2);
        if(userDOS.size()>0){
            for (AdminUserDO user:userDOS) {
                userService.deleteUser(user.getId());
                userExtService.deleteByUserId(user.getId());
            }
        }
        List<DeptDO> deptDOS = deptService.selectIsExist(reqVO.getBusinessName(), 2);
        if (deptDOS.size()>0){
            for (DeptDO dept:deptDOS) {
                deptService.deleteDept(dept.getId());
            }
        }

        //查询该手机号是否注册
        if(userService.getUserByMobile(reqVO.getPhone())!=null){
            throw exception(AUTH_MOBILE_IS_EXIST);
        }
        //查询身份证是否注册
        if(userExtService.selectByIDCard(reqVO.getIdCard()).size()>0){
            throw exception(AUTH_IDCARD_IS_EXIST);
        }
        //查询营业执照号是否注册
        if(deptService.selectByTaxNum(reqVO.getTaxNum()).size()>0){
            throw exception(AUTH_TAXNUM_IS_EXIST);
        }

        //查询商户名称是否注册
        if(deptService.selectByName(reqVO.getBusinessName()).size()>0){
            throw exception(AUTH_NAME_IS_EXIST);
        }

//        String decrypt = RASClientUtil.jsencryptDecryptByPrivateKeyLong(reqVO.getPassword());
        HashMap<Object, Object> map = new HashMap<>();
        try {
            //拿到父级
            DeptDO parentDept = deptService.selectByParent(reqVO.getMarketLocation(), 0L);//2商户方  1市场方 0 父级

            //根据租户id查询商户的父级id
            DeptDO dept = deptService.selectDept(reqVO.getMarketLocation(), "2");//2商户方  1市场方


            //插入商户信息
            DeptDO deptDO = new DeptDO();
            deptDO.setName(reqVO.getBusinessName());
            deptDO.setParentId(dept.getId());//父级id
            deptDO.setTaxNum(reqVO.getTaxNum());
            deptDO.setAddress(reqVO.getAddress());
            deptDO.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            deptDO.setLegalRepresentative(reqVO.getLegal_representative());
//            deptDO.setBusiness_license_url(reqVO.getBusinessLicense());//营业执照url
            deptDO.setSort(2);
            deptDO.setStatus(2);//未激活
            deptService.insertDept(deptDO);

            //用户主表
            AdminUserDO userDO = new AdminUserDO();
            userDO.setUsername(reqVO.getPhone());
            userDO.setMobile(reqVO.getPhone());
//            userDO.setPassword(encodePassword(decrypt)); // 加密密码
            userDO.setNickname(reqVO.getName());
            userDO.setDeptId(deptDO.getId());//商户id
            userDO.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            userDO.setStatus(1);
            userService.insertUser(userDO);

            //用户扩展表
            UserExtDO extDO = new UserExtDO();
            extDO.setUserId(userDO.getId());//用户主表id
            extDO.setIdCard(reqVO.getIdCard());
            extDO.setBankName(reqVO.getBankName());//开户行
            extDO.setBankAccount(reqVO.getBankNumber());//对公银行账号
            extDO.setBondBankAccount(reqVO.getBondBankAccount());//保证金充值账号
            extDO.setStaffType("1");//主账号
            extDO.setStatus(1);//未激活
            extDO.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            userExtService.insertUser(extDO);

            FileInsertReqDTO reqDTO = new FileInsertReqDTO();
            reqDTO.setMainId(extDO.getId());//用户扩展表id
            reqDTO.setUrl(reqVO.getIdCardUrl());
            reqDTO.setType("8");//商户身份证
            reqDTO.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            CommonResult<String> result = businessFileApi.saveToBusinessFile(reqDTO);
            System.out.println(result);

            FileInsertReqDTO reqDTO1 = new FileInsertReqDTO();
            reqDTO1.setMainId(deptDO.getId());//用户扩展表id
            reqDTO1.setUrl(reqVO.getBusinessLicense());
            reqDTO1.setType("9");//营业执照
            reqDTO1.setTenantId(Long.valueOf(reqVO.getMarketLocation()));
            CommonResult<String> result2 = businessFileApi.saveToBusinessFile(reqDTO1);
            System.out.println(result2);


            map.put("thirdId",deptDO.getId());
            map.put("marketName",parentDept.getName());
            map.put("merchantName",deptDO.getName());
            map.put("startUserId",userDO.getId());
        }catch (Exception e){
            throw exception(AUTH_REGISTER_ERROR);
        }
        return map;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Map addAccount(AddAccountReqVO reqVO) {
        HashMap<Object, Object> map = new HashMap<>();
        List<AdminUserDO> userDOS = userService.selectByMobileAndBusiness(reqVO.getPhone(), reqVO.getDeptId());
        if(userDOS.size()>0){
            throw exception(AUTH_MOBILE_IS_EXIST);
        }
        AdminUserDO userDO = new AdminUserDO();
        UserExtDO userExtDO = new UserExtDO();
        if(null==reqVO.getId()){
            try {
                userDO.setUsername(reqVO.getPhone());
                userDO.setMobile(reqVO.getPhone());
                userDO.setNickname(reqVO.getName());
                userDO.setStatus(reqVO.getStatus());
                userDO.setDeptId(reqVO.getDeptId());
                userDO.setTenantId(reqVO.getTenantId());
                userService.insertUser(userDO);

                userExtDO.setUserId(userDO.getId());
                userExtDO.setStaffType("2");
                userExtDO.setStatus(1);
                userExtDO.setBusinessId(reqVO.getDeptId());
                userExtDO.setTenantId(reqVO.getTenantId());
                userExtDO.setIdCard(reqVO.getIdCard());
                userExtService.insertUser(userExtDO);
                map.put("success","0");
                CommonResult<Boolean> result = qysConfigApi.userAuth(userDO.getId());
            }catch (Exception e){
                throw exception(AUTH_ADDACCOUNT_ERROR);
            }
        }else{
            try {
                AdminUserDO user = userService.getUser(reqVO.getId());
                UserExtDO userExtDOS = userExtService.selectByUserId(reqVO.getId()).get(0);
                user.setNickname(reqVO.getName());
                user.setStatus(Integer.valueOf(reqVO.getStatus()));
                //如果身份证&身份证变更，修改为未激活（未认证）
                if(userExtDOS.getIdCard().equals(reqVO.getIdCard()) &&user.getMobile().equals(reqVO.getPhone())){
                    adminUserMapper.updateById(user);
                    userExtMapper.updateById(userExtDOS);
                }else{
                    user.setStatus(reqVO.getStatus());
                    user.setUsername(reqVO.getPhone());
                    user.setMobile(reqVO.getPhone());

                    userExtDOS.setIdCard(reqVO.getIdCard());
                    userExtDOS.setStatus(1);
                    adminUserMapper.updateById(user);
                    userExtMapper.updateById(userExtDOS);
                    CommonResult<Boolean> booleanCommonResult = qysConfigApi.userAuth(userDO.getId());
                }

            }catch (Exception e){
                throw exception(AUTH_UPDATEACCOUNT_ERROR);
            }
        }
        return map;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public int deleteAccount(Long id) {
        AdminUserDO adminUserDO = adminUserMapper.selectById(id);
        adminUserMapper.deleteById(id);
        int delete = userExtMapper.deleteByUserId(id);
        if (delete >= 1) {
            QysConfigDTO configDTO = qysConfigApi.getByDeptId(adminUserDO.getDeptId()).getCheckedData();
            QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(configDTO.getBusinessId());
            client.defaultEmployeeRemove(adminUserDO.getUsername(), adminUserDO.getMobile()).getCheckedData();
        }
        return delete;
    }

    @Override
    public List<AddAccountRespVO> getAccountList(Long deptId) {

        return userExtMapper.getAccountList(deptId);
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
