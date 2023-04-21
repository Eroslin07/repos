package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantAccountRespVO;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "商户账户")
@RestController
@RequestMapping("/uctp/account")
@Validated
public class AccountController {

    @Resource
    private MerchantAccountService merchantAccountService;

    @PostMapping("/open")
    @Operation(summary = "商户账户开户")
    public CommonResult<String> accountOpen() {
        return success("OK");
    }

    @GetMapping("/get")
    @Operation(summary = "查询商户虚拟账户资产详情")
    public CommonResult<MerchantAccountRespVO> get() {
        return success(merchantAccountService.get(WebFrameworkUtils.getLoginUserId()));
    }

}
