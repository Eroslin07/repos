package com.newtouch.uctp.module.bpm.controller.admin.payfailed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.bpm.api.payfailed.dto.PayFailedCreateBpmDTO;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
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

    @PostMapping("/create")
    @Operation(summary = "根据流程定义标识（业务类型）新建流程")
    public CommonResult<String> createBpm(@Valid @RequestBody PayFailedCreateBpmDTO createReqVO) {
        return success(payFailedService.createBpm(createReqVO.getContractId(), BpmDefTypeEnum.SKZH.name(), createReqVO.getVariables()));
    }

    @PostMapping("/createBpm")
    @Operation(summary = "根据（契约锁）收车合同ID、支付失败原因发起支付失败流程")
    public CommonResult<String> createPayFailedBpm(@RequestParam("contractId") Long contractId,
                                          @RequestParam(name = "failReason", required = false, defaultValue = "支付侧支付失败") String failReason) {
        return success(payFailedService.createBpm(contractId, failReason));
    }
}
