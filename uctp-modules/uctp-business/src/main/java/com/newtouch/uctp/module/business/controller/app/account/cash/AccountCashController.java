package com.newtouch.uctp.module.business.controller.app.account.cash;

import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.*;
import com.newtouch.uctp.module.business.service.account.AccountCashService;
import com.newtouch.uctp.module.business.service.impl.ValidatedGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Tag(name = "商户保证金管理")
@RestController
@RequestMapping("/uctp/account/cash")
public class AccountCashController {

    @Resource
    private AccountCashService accountCashService;

    @GetMapping("/detail")
    @Operation(summary = "保证金详情-我的保证金")
    public CommonResult<AccountCashRespVO> detail(@RequestParam(name = "accountNo") String accountNo) {
        return CommonResult.success(accountCashService.detail(accountNo));
    }

    @PostMapping("/list")
    @Operation(summary = "保证金交易明细查询")
    public CommonResult<PageResult<CashDetailRespVO>> list(@Validated @RequestBody MerchantCashReqVO merchantCashReqVO) {
        return CommonResult.success(accountCashService.list(merchantCashReqVO));
    }

    @PostMapping("/recharge")
    @Operation(summary = "保证金充值-充值成功后返回我的保证金页面模型数据")
    public CommonResult<AccountCashRespVO> recharge(@Validated(ValidatedGroup.Recharge.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.recharge(transactionRecordReqVO));
    }

    @PostMapping("/withdraw")
    @Operation(summary = "保证金提取-提取成功后返回我的保证金页面模型数据")
    public CommonResult<AccountCashRespVO> withdraw(@Validated(ValidatedGroup.Withdraw.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.withdraw(transactionRecordReqVO));
    }

    @PostMapping("/reserve")
    @Operation(summary = "保证金预占")
    public CommonResult<Boolean> reserve(@Validated(ValidatedGroup.Reserve.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.reserve(transactionRecordReqVO));
    }

    @PostMapping("/deduction")
    @Operation(summary = "保证金实占")
    public CommonResult<Boolean> deduction(@Validated(ValidatedGroup.Deduction.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.deduction(transactionRecordReqVO));
    }

    @PostMapping("/difference")
    @Operation(summary = "待回填保证金")
    public CommonResult<Long> difference(@Validated(ValidatedGroup.Difference.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.difference(transactionRecordReqVO));
    }

    @PostMapping("/back")
    @Operation(summary = "保证金回填")
    public CommonResult<Boolean> back(@Validated(ValidatedGroup.Back.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.back(transactionRecordReqVO));
    }

    @PostMapping("/profit/back")
    @Operation(summary = "保证金回填-利润")
    public CommonResult<Boolean> profitBack(@Validated(ValidatedGroup.ProfitBack.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.profitBack(transactionRecordReqVO));
    }

    @PostMapping("/release")
    @Operation(summary = "保证金释放")
    public CommonResult<Boolean> release(@Validated(ValidatedGroup.Release.class) @RequestBody TransactionRecordReqVO transactionRecordReqVO) {
        return CommonResult.success(accountCashService.release(transactionRecordReqVO));
    }

    @GetMapping("/bankInfo")
    @Operation(summary = "商户银行信息查询")
    public CommonResult<MerchantBankRespVO> bankInfo(@RequestParam(name = "accountNo") String accountNo, @RequestParam String busType) {
        return CommonResult.success(accountCashService.bankInfo(accountNo, busType));
    }

    @PostMapping("/app/transfer")
    @Operation(summary = "商户保证金充值获取银行APP唤醒链接")
    public CommonResult<AppTransferRespVO> appTransfer(@RequestBody AppTransferReqVO appTransferReqVO) {
        try {
            return CommonResult.success(accountCashService.appTransfer(appTransferReqVO));
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                return CommonResult.error((ServiceException) e);
            }
            return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }
}
