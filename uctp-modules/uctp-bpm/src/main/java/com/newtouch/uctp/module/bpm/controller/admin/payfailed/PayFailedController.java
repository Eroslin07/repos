package com.newtouch.uctp.module.bpm.controller.admin.payfailed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.payfailed.dto.PayFailedCreateBpmDTO;
import com.newtouch.uctp.module.bpm.service.payfailed.PayFailedService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * @author helong
 * @date 2023/4/28 18:23
 */
@Tag(name =  "管理后台 - 二手车支付失败流程操作")
@RestController
@RequestMapping("/bpm/payfailed")
@Validated
public class PayFailedController {
    @Resource
    private PayFailedService payFailedService;

    @PostMapping("/v3/create")
    @Operation(summary = "根据流程定义标识（业务类型）新建流程")
    public CommonResult<String> createProcessInstanceByKey(@Valid @RequestBody PayFailedCreateBpmDTO createReqVO) {
        return success(payFailedService.createBpm(createReqVO.getContractId(), "SKZH", createReqVO.getVariables()));
    }
}
