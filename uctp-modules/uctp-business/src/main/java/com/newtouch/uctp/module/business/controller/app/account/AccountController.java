package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantAccountRespVO;
import com.newtouch.uctp.module.business.service.account.AccountService;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "商户账户")
@RestController
@RequestMapping("/uctp/account")
@Validated
public class AccountController {

    @Resource
    private MerchantAccountService merchantAccountService;

    @GetMapping("/get")
    @Operation(summary = "查询商户虚拟账户资产详情")
    public CommonResult<MerchantAccountRespVO> get(@RequestParam long merchantId) {
        return success(merchantAccountService.get(merchantId));
    }

}
