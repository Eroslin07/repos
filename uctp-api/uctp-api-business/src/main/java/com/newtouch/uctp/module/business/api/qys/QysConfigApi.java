package com.newtouch.uctp.module.business.api.qys;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.constraints.NotNull;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.enums.ApiConstants;

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
    CommonResult<Boolean> companyAuth(@RequestParam("userId") Long userId);

    /**
     * 个人认证
     */
    @GetMapping(PREFIX + "/userAuth")
    @Operation(summary = "个人认证")
    @Parameter(name = "userId", description = "注册用户Id", example = "1024", required = true)
    CommonResult<Boolean> userAuth(@RequestParam("userId") Long userId);

    @PostMapping(PREFIX + "/company/sign/{contractId}")
    @Operation(summary ="公司静默签章")
    @Parameter(name = "contractId", description = "合同ID", example = "1024", required = true)
    CommonResult<Boolean> companySign(@PathVariable("contractId") @NotNull Long contractId);
}
