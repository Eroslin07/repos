package com.newtouch.uctp.module.business.api.account;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.account.dto.ProfitPresentAuditDTO;
import com.newtouch.uctp.module.business.service.account.AccountProfitService;
import com.newtouch.uctp.module.business.service.account.ProfitPressentAuditOpinion;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.ACC_PRESENT_ERROR;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
@Slf4j
public class AccountProfitApiImpl implements AccountProfitApi {

    @Resource
    private AccountProfitService accountProfitService;

    @Override
    public CommonResult<String> presentAudit(@RequestHeader("tenant-id") Long tenantId,
                                             @RequestBody ProfitPresentAuditDTO audit) {
        String businessKey = audit.getBusinessKey();
        log.info("审核利润提取{}，意见{}", businessKey, audit.getAuditOpinion());
        ProfitPressentAuditOpinion auditOpinion = null;
        if (audit.getAuditOpinion() == 1) {
            auditOpinion = ProfitPressentAuditOpinion.AUDIT_APPROVED;
        } else if (audit.getAuditOpinion() == 2) {
            auditOpinion = ProfitPressentAuditOpinion.AUDIT_REJECT;
        } else {
            log.error("利润提现审核意见有误");
            throw exception(ACC_PRESENT_ERROR);
        }

        accountProfitService.auditProfitPressent(businessKey, auditOpinion);

        return success("提交成功");
    }

    @Override
    public CommonResult<String> profitRelease(String businessKey) {
        accountProfitService.profitRelease(businessKey);
        return success("释放成功");
    }
}
