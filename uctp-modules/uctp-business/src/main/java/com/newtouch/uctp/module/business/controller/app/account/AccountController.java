package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.MerchantAccountRespVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.PosBindReqVO;
import com.newtouch.uctp.module.business.service.account.MerchantAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.error;
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

    @PostMapping("/pos/bind")
    @Operation(summary = "POS机商户编号绑定")
    public CommonResult<String> posMrhNoBind(@RequestBody PosBindReqVO reqVO) {
        try {
            return success(merchantAccountService.bindPosMrhNo(reqVO));
        } catch (Exception e) {
            return error((ServiceException) e);
        }
    }

    @GetMapping("/pos/info")
    @Operation(summary = "查询商户绑定POS的商户编号")
    public CommonResult<String> posMrhNoInfo(@RequestParam long merchantId) {
        try {
            return success(merchantAccountService.getPosMrhNoInfo(merchantId));
        } catch (Exception e) {
            return error((ServiceException) e);
        }
    }

    @PostMapping("/pos/modify")
    @Operation(summary = "POS机商户编号修改")
    public CommonResult<String> posMrhNoModify(@RequestBody PosBindReqVO reqVO) {
        try {
            return success(merchantAccountService.posMrhNoModify(reqVO));
        } catch (Exception e) {
            return error((ServiceException) e);
        }
    }
}
