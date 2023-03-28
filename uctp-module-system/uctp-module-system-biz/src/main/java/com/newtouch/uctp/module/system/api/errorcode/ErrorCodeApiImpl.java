package com.newtouch.uctp.module.system.api.errorcode;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.system.api.errorcode.dto.ErrorCodeAutoGenerateReqDTO;
import com.newtouch.uctp.module.system.api.errorcode.dto.ErrorCodeRespDTO;
import com.newtouch.uctp.module.system.service.errorcode.ErrorCodeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class ErrorCodeApiImpl implements ErrorCodeApi {

    @Resource
    private ErrorCodeService errorCodeService;

    @Override
    public CommonResult<Boolean> autoGenerateErrorCodeList(List<ErrorCodeAutoGenerateReqDTO> autoGenerateDTOs) {
        errorCodeService.autoGenerateErrorCodes(autoGenerateDTOs);
        return success(true);
    }

    @Override
    public CommonResult<List<ErrorCodeRespDTO>> getErrorCodeList(String applicationName, LocalDateTime minUpdateTime) {
        return success(errorCodeService.getErrorCodeList(applicationName, minUpdateTime));
    }
}
