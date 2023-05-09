package com.newtouch.uctp.module.business.api.account;

import com.newtouch.uctp.framework.common.exception.BankException;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.account.dto.AccountDTO;
import com.newtouch.uctp.module.business.dal.dataobject.TransactionLogDO;
import com.newtouch.uctp.module.business.service.account.AccountService;
import com.newtouch.uctp.module.business.service.bank.TransactionLogService;
import com.newtouch.uctp.module.business.service.bank.TransactionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.error;
import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class AccountApiImpl implements AccountApi {

    @Resource
    private AccountService accountService;

    @Resource
    private TransactionLogService transactionLogService;

    @Override
    public CommonResult<String> merchantAccountOpen(AccountDTO accountVO) {
        LocalDateTime now = LocalDateTime.now();
        try {
            accountService.accountGenerate(accountVO);
            return success("商户虚拟账户开户成功!");
        } catch (Exception e) {
            if (e instanceof BankException) {
                transactionLogService.save(TransactionLogDO.builder()
                        .tranBeginTime(now)
                        .tranEndTime(LocalDateTime.now())
                        .tranRequest(((BankException) e).getRequest())
                        .tranResponse(e.getMessage())
                        .build());
            }
            if (e instanceof ServiceException) {
                return error(((ServiceException) e).getCode(), e.getMessage());
            }
            return error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), "商户虚拟账户开户失败");
        }
    }
}
