package com.newtouch.uctp.module.business.api.qys;

import java.io.FileNotFoundException;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class QysConfigApiImpl implements QysConfigApi{
    @Resource
    private QysConfigService qysConfigService;


    @Override
    public CommonResult<Boolean> companyAuth(Long userId) {
        try {
            qysConfigService.companyAuth(userId);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return success(Boolean.TRUE);
    }

    @Override
    public CommonResult<Boolean> userAuth(Long userId) {
        qysConfigService.userAuth(userId);
        return success(Boolean.TRUE);
    }

    @Override
    public CommonResult<Boolean> companySign(Long contractId) {
        qysConfigService.companySign(contractId);
        return success(Boolean.TRUE);
    }
}
