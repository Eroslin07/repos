package com.newtouch.uctp.module.business.api.contract;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.contract.ContractService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.module.system.enums.ApiConstants.VERSION;

/**
 * @author helong
 * @date 2023/5/9 19:29
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
@Validated
public class ContractApiImpl implements ContractApi {
    @Resource
    private ContractService contractService;

    @Override
    public CommonResult<Boolean> contractInvalid(Long id, String reason) {
        contractService.contractInvalid(id,reason);
        return success(true);
    }
}
