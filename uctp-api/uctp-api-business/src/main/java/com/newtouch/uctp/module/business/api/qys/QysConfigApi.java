package com.newtouch.uctp.module.business.api.qys;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;

/**
 * 契约锁接口
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 契约锁")
public interface QysConfigApi {
    String PREFIX = ApiConstants.PREFIX + "/qys";
    /**
     * 公司认证
     */
    @GetMapping(PREFIX + "/get")
    @Operation(summary = "公司认证")
    @Parameter(name = "userId", description = "注册用户Id", example = "1024", required = true)
    CommonResult<Boolean> companyAuth(@RequestParam("userId") Long userId) throws FileNotFoundException;
}
