package com.newtouch.uctp.module.business.controller.app.account;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackReqVO;
import com.newtouch.uctp.module.business.controller.app.account.vo.CashBackRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.service.account.MerchantCashBackService;
import com.newtouch.uctp.module.business.service.cash.MerchantAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.ACC_MERCHANT_ACCOUNT_NOT_EXISTS;

@Tag(name = "商户待回填保证金")
@RestController
@RequestMapping("/uctp/account/cashback")
@Validated
@Slf4j
public class AccountCashBackController {
    @Resource
    private MerchantAccountService merchantAccountService;

    @Resource
    private MerchantCashBackService merchantCashBackService;

    @GetMapping("/list")
    @Operation(summary = "待回填明细查询")
    public CommonResult<PageResult<CashBackRespVO>> list(@Valid CashBackReqVO query) {
        String accountNo = query.getAccountNo();
        log.info("查询账户{}的待回填明细查询", accountNo);

        //this.checkAccount(accountNo);
        PageResult<CashBackRespVO> pr = merchantCashBackService.list(accountNo, query);

        return success(pr);
    }

    @GetMapping("/detail")
    @Operation(summary = "利润详情")
    public CommonResult<CashBackRespVO> detail(@RequestParam String accountNo, @RequestParam String id) {
        log.info("查询账户{}的{}利润详情", accountNo, id);

        //this.checkAccount(accountNo);
        Long idValue = null;
        if (id != null) {
            idValue = Long.valueOf(id);
        }
        CashBackRespVO respVO = merchantCashBackService.detail(accountNo, idValue);
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
