package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantAccountRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosDevicesRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosNameReqVO;
import com.newtouch.uctp.module.business.service.account.MerchantAccountService;
import com.newtouch.uctp.module.business.service.account.MerchantPOSDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "商户账户")
@RestController
@RequestMapping("/uctp/account")
@Validated
public class AccountController {

    @Resource
    private MerchantAccountService merchantAccountService;

    @Resource
    private MerchantPOSDeviceService merchantPOSDeviceService;

    @GetMapping("/get")
    @Operation(summary = "查询商户虚拟账户资产详情")
    public CommonResult<MerchantAccountRespVO> get(@RequestParam long merchantId) {
        return success(merchantAccountService.get(merchantId));
    }

    @PostMapping("/posname")
    @Operation(summary = "POS机命名")
    public CommonResult<String> posName(PosNameReqVO reqVO) {
        String authCode = merchantPOSDeviceService.savePosName(reqVO);
        return success(authCode);
    }

    @GetMapping("/pos/list")
    @Operation(summary = "POS机列表")
    public CommonResult<List<PosDevicesRespVO>> getPosList(@RequestParam long merchantId) {
        return null;
    }
}
