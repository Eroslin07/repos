package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.account.vo.*;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.ACC_MERCHANT_ACCOUNT_NOT_EXISTS;

@Tag(name = "商户利润")
@RestController
@RequestMapping("/uctp/account/profit")
@Validated
@Slf4j
public class AccountProfitController {

    @Resource
    private AccountProfitService accountProfitService;

    @Resource
    private MerchantAccountService merchantAccountService;

    @GetMapping("/summary")
    @Operation(summary = "查询利润概要信息")
    public CommonResult<ProfitSummaryRespVO> summary(@RequestParam String accountNo) {
        log.info("查询账户{}的利润概要信息", accountNo);

        this.checkAccount(accountNo);
        ProfitSummaryRespVO summary = accountProfitService.summary(accountNo);

        return success(summary);
    }

    @PostMapping("/present")
    @Operation(summary = "利润提取")
    public CommonResult<String> profitPresent(@Valid @RequestBody ProfitPresentReqVO profitPresentReqVO) {
        String accountNo = profitPresentReqVO.getAccountNo();
        log.info("账户{}提出利润", accountNo);

        this.checkAccount(accountNo);
        Long profitId = accountProfitService.profitPresent(accountNo, profitPresentReqVO.getMerchantBankId(),
                profitPresentReqVO.getAmount(), profitPresentReqVO.getInvoiceIds());

        String profitIdStr = "";
        if (profitId != null) {
            profitIdStr = profitId.toString();
        }

        return success(profitIdStr);
    }

    @GetMapping("/list")
    @Operation(summary = "利润明细查询")
    public CommonResult<PageResult<ProfitRespVO>> profitList(@Valid ProfitQueryReqVO query) {
        String accountNo = query.getAccountNo();
        log.info("查询账户{}的利润明细", accountNo);

        this.checkAccount(accountNo);
        PageResult<ProfitRespVO> pr = accountProfitService.profitList(accountNo, query);

        return success(pr);
    }

    @GetMapping("/detail")
    @Operation(summary = "利润详情")
    public CommonResult<ProfitDetailRespVO> profitDetail(@RequestParam String accountNo, @RequestParam String profitId) {
        log.info("查询账户{}的{}利润详情", accountNo, profitId);

        this.checkAccount(accountNo);
        Long id = null;
        if (profitId != null) {
            id = Long.valueOf(profitId);
        }
        ProfitDetailRespVO respVO = accountProfitService.profitDetail(accountNo, id);
        return success(respVO);
    }

    /**
     * 判断当前商户账户是不是当前登录用户名下的
     * @param accountNo
     */
    private void checkAccount(String accountNo) {
        Long userId = WebFrameworkUtils.getLoginUserId();
        if (userId == null) {
            throw exception(ACC_MERCHANT_ACCOUNT_NOT_EXISTS);
        }
        MerchantAccountDO account = merchantAccountService.queryByAccountNo(accountNo);
        if (StringUtils.isBlank(accountNo)) {
            // 账户不存在
            throw exception(ACC_MERCHANT_ACCOUNT_NOT_EXISTS);
        }

        if (!userId.equals(account.getMerchantId())) {
            // 传入的账户号不是当前用户名下的
            throw exception(ACC_MERCHANT_ACCOUNT_NOT_EXISTS);
        }
    }
}
