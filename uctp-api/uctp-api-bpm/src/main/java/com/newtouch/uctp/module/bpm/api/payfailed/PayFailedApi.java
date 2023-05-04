package com.newtouch.uctp.module.bpm.api.payfailed;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.cloud.openfeign.FeignClient;

import com.newtouch.uctp.module.bpm.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/4/28 18:34
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 支付失败流程")
public interface PayFailedApi {

}
