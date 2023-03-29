package com.newtouch.uctp.module.infra.api.logger;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import com.newtouch.uctp.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;
import com.newtouch.uctp.module.infra.service.logger.ApiAccessLogService;
import com.newtouch.uctp.module.infra.service.logger.ApiErrorLogService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class ApiErrorLogApiImpl implements ApiErrorLogApi {

    @Resource
    private ApiErrorLogService apiErrorLogService;

    @Override
    public CommonResult<Boolean> createApiErrorLog(ApiErrorLogCreateReqDTO createDTO) {
        apiErrorLogService.createApiErrorLog(createDTO);
        return success(true);
    }

}
