package com.newtouch.uctp.module.bpm.api.openinvoice;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.service.openinvoice.BpmOpenInvoiceService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/4/26 16:07
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class BpmOpenInvoiceApiImpl implements BpmOpenInvoiceApi {
    @Resource
    private BpmOpenInvoiceService bpmOpenInvoiceService;

    @Override
    public CommonResult<String> createOpenInvoiceBpm(Long contractId, String procDefKey) {
        String businessKey = bpmOpenInvoiceService.createOpenInvoiceBpm(contractId, procDefKey);
        return success(businessKey);
    }
}
