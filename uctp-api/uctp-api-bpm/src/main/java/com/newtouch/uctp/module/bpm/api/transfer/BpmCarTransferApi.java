package com.newtouch.uctp.module.bpm.api.transfer;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.cloud.openfeign.FeignClient;

import com.newtouch.uctp.module.bpm.enums.ApiConstants;

/**
 * @author helong
 * @date 2023/4/26 15:04
 */
@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 部门")
public interface BpmCarTransferApi {

}
