package com.newtouch.uctp.module.business.controller.app.contact;

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
import com.newtouch.uctp.module.business.service.contract.ContractService;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "App管理 - 合同管理")
@RestController
@RequestMapping("/uctp/contract")
@Validated
public class ContractController {
    @Resource
    private ContractService contractService;

    @PostMapping("/contract/invalid")
    @Operation(summary = "合同作废")
    @Parameter(name = "id", description = "合同id", required = true, example = "1024")
    @Parameter(name = "reason", description = "合同作废原因", required = true, example = "合同作废")
    public CommonResult<Boolean> contractInvalid(@RequestParam Long id,
                                               @RequestParam String reason){
        contractService.contractInvalid(id,reason);
        return success(true);
    }

    @PostMapping("/contract/draft")
    @Operation(summary = "合同发起草稿")
    @Parameter(name = "carId", description = "车辆id", required = true, example = "1024")
    public CommonResult<Boolean> draft(@RequestParam Long carId){
        contractService.draft(carId);
        return success(true);
    }

}
