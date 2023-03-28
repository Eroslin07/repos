package com.newtouch.uctp.module.system.api.social;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.system.api.social.dto.SocialUserBindReqDTO;
import com.newtouch.uctp.module.system.api.social.dto.SocialUserUnbindReqDTO;
import com.newtouch.uctp.module.system.service.social.SocialUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class SocialUserApiImpl implements SocialUserApi {

    @Resource
    private SocialUserService socialUserService;

    @Override
    public CommonResult<String> getAuthorizeUrl(Integer type, String redirectUri) {
        return success(socialUserService.getAuthorizeUrl(type, redirectUri));
    }

    @Override
    public CommonResult<Boolean> bindSocialUser(SocialUserBindReqDTO reqDTO) {
        socialUserService.bindSocialUser(reqDTO);
        return success(true);
    }

    @Override
    public CommonResult<Boolean> unbindSocialUser(SocialUserUnbindReqDTO reqDTO) {
        socialUserService.unbindSocialUser(reqDTO.getUserId(), reqDTO.getUserType(),
                reqDTO.getType(), reqDTO.getUnionId());
        return success(true);
    }

    @Override
    public CommonResult<Long> getBindUserId(Integer userType, Integer type, String code, String state) {
       return success(socialUserService.getBindUserId(userType, type, code, state));
    }

}
