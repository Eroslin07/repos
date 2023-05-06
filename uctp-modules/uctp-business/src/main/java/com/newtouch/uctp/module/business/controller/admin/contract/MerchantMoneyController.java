package com.newtouch.uctp.module.business.controller.admin.contract;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.service.contract.MerchantMoneyService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * @author helong
 * @date 2023/5/6 13:41
 */
@Tag(name = "合同管理 - 合同档案管理")
@RestController
@RequestMapping("/uctp/merchant/money")
@Validated
public class MerchantMoneyController {

    @Resource
    private MerchantMoneyService merchantMoneyService;

    @PostMapping("/reserveCash")
    @Operation(summary = "收车时，预占保证金")
    @Parameter(name = "contractId", description = "（契约锁）收车合同ID", example = "3089090553137136296", required = true)
    public CommonResult<Boolean> reserveCash(@RequestParam("contractId") Long contractId) {
        return success(merchantMoneyService.reserveCash(contractId));
    }

    @PostMapping("/releaseCash")
    @Operation(summary = "收车中，释放保证金")
    @Parameter(name = "contractId", description = "（契约锁）收车合同ID", example = "3089090553137136296", required = true)
    public CommonResult<Boolean> releaseCash(@RequestParam("contractId") Long contractId) {
        return success(merchantMoneyService.releaseCash(contractId));
    }

    @PostMapping("/deductionCash")
    @Operation(summary = "收车时，实占保证金")
    @Parameter(name = "contractId", description = "（契约锁）收车合同ID", example = "3089090553137136296", required = true)
    public CommonResult<Boolean> deductionCash(@RequestParam("contractId") Long contractId) {
        return success(merchantMoneyService.deductionCash(contractId));
    }
}
