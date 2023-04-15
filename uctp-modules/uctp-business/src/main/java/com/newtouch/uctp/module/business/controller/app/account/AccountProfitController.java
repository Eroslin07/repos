package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.account.vo.*;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "商户利润")
@RestController
@RequestMapping("/uctp/account/profit")
@Validated
public class AccountProfitController {

    @Resource
    private AccountProfitService accountProfitService;

    @GetMapping("/summary")
    @Operation(summary = "查询利润概要信息")
    public CommonResult<ProfitSummaryRespVO> summary() {
        String accountNo = this.getAccountNoFromContext();

        MerchantAccountDO account = accountProfitService.queryByAccountNo(accountNo);
        ProfitSummaryRespVO respVO = new ProfitSummaryRespVO();
        respVO.setProfit(account.getProfit());
        respVO.setFreezeProfit(account.getFreezeProfit());
        respVO.setCashBack(null); // todo 账户表缺少待回填保证金

        return success(respVO);
    }

    @PostMapping("/present")
    @Operation(summary = "利润提取")
    public CommonResult<Long> profitPresent(@Valid @RequestBody ProfitPresentReqVO profitPresentReqVO) {
        String accountNo = this.getAccountNoFromContext();

        Long profitId = accountProfitService.profitPresent(accountNo, profitPresentReqVO.getMerchantBankId(),
                profitPresentReqVO.getAmount(), profitPresentReqVO.getInvoiceIds());

        return success(profitId);
    }

    @GetMapping("/list")
    @Operation(summary = "利润明细查询")
    public CommonResult<PageResult<ProfitRespVO>> profitList(@Valid ProfitQueryReqVO query) {
        String accountNo = this.getAccountNoFromContext();

        PageResult<ProfitRespVO> pr = accountProfitService.profitList(accountNo, query);

        return success(pr);
    }

    @GetMapping("/detail")
    @Operation(summary = "利润详情")
    public CommonResult<ProfitDetailRespVO> profitDetail(Long profitId) {
        String accountNo = this.getAccountNoFromContext();

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
