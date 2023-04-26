package com.newtouch.uctp.module.business.api.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 商户虚拟账户")
public interface AccountApi {

    String PREFIX = ApiConstants.PREFIX + "/account";

    @PostMapping(PREFIX + "/open")
    @Operation(summary = "商户虚拟账户开户")
    CommonResult<String> merchantAccountOpen(@Valid @RequestBody AccountDTO accountVO);
}
