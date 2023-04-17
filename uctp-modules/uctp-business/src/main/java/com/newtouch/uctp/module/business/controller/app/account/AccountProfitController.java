package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.*;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "商户利润")
@RestController
@RequestMapping("/uctp/account/profit")
@Validated
@Slf4j
public class AccountProfitController {

    @Resource
    private AccountProfitService accountProfitService;

    @GetMapping("/summary")
    @Operation(summary = "查询利润概要信息")
    public CommonResult<ProfitSummaryRespVO> summary() {
        String accountNo = this.getAccountNoFromContext();
        log.info("查询账户{}的利润概要信息", accountNo);

        ProfitSummaryRespVO summary = accountProfitService.summary(accountNo);

        return success(summary);
    }

    @PostMapping("/present")
    @Operation(summary = "利润提取")
    public CommonResult<Long> profitPresent(@Valid @RequestBody ProfitPresentReqVO profitPresentReqVO) {
        String accountNo = this.getAccountNoFromContext();
        log.info("账户{}提出利润", accountNo);

        Long profitId = accountProfitService.profitPresent(accountNo, profitPresentReqVO.getMerchantBankId(),
                profitPresentReqVO.getAmount(), profitPresentReqVO.getInvoiceIds());

        return success(profitId);
    }

    @GetMapping("/list")
    @Operation(summary = "利润明细查询")
    public CommonResult<PageResult<ProfitRespVO>> profitList(@Valid ProfitQueryReqVO query) {
        String accountNo = this.getAccountNoFromContext();
        log.info("查询账户{}的利润明细", accountNo);

        PageResult<ProfitRespVO> pr = accountProfitService.profitList(accountNo, query);

        return success(pr);
    }

    @GetMapping("/detail")
    @Operation(summary = "利润详情")
    public CommonResult<ProfitDetailRespVO> profitDetail(Long profitId) {
        String accountNo = this.getAccountNoFromContext();
        log.info("查询账户{}的{}利润详情", accountNo, profitId);

        ProfitDetailRespVO respVO = accountProfitService.profitDetail(accountNo, profitId);
        return success(respVO);
    }

    /**
     * 从会话上下文中获得商户账号
     * @return
     */
    private String getAccountNoFromContext() {
        // todo 通过什么方式能获取到当前商户（用户）
        String accountNo = null; // todo 待修改
        return accountNo;
    }
}
