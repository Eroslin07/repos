package com.newtouch.uctp.module.business.api.qys;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.module.business.api.qys.dto.EmployeeCreateOrRemoveDTO;
import com.newtouch.uctp.module.business.api.qys.dto.QysConfigDTO;
import com.newtouch.uctp.module.business.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 契约锁接口
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 契约锁")
public interface QysConfigApi {
    String PREFIX = ApiConstants.PREFIX + "/qys";
    /**
     * 公司认证
     */
    @GetMapping(PREFIX + "/get")
    @Operation(summary = "公司认证")
    @Parameter(name = "userId", description = "注册用户Id", example = "1024", required = true)
    CommonResult<Boolean> companyAuth(@RequestParam("userId") Long userId);

    /**
     * 个人认证
     */
    @GetMapping(PREFIX + "/userAuth")
    @Operation(summary = "个人认证")
    @Parameter(name = "userId", description = "注册用户Id", example = "1024", required = true)
    CommonResult<Boolean> userAuth(@RequestParam("userId") Long userId);

    @PostMapping(PREFIX + "/company/sign/{contractId}")
    @Operation(summary ="公司静默签章")
    @Parameter(name = "contractId", description = "合同ID", example = "1024", required = true)
    CommonResult<Boolean> companySign(@PathVariable("contractId") @NotNull Long contractId);

    @PostMapping(PREFIX + "/send/{contractId}/{hasReserve}")
    @Operation(summary ="发起合同")
    @Parameter(name = "contractId", description = "收车委托合同ID", example = "1024", required = true)
    @Parameter(name = "hasReserve", description = "是否预占", example = "true", required = true)
    CommonResult<Boolean> send(@PathVariable("contractId") @NotNull Long contractId,
                               @PathVariable("hasReserve") @NotNull Boolean hasReserve);

    @PostMapping(PREFIX + "/get/{deptId}")
    @Operation(summary ="获取契约锁")
    @Parameter(name = "deptId", description = "部门ID", example = "1024", required = true)
    CommonResult<QysConfigDTO> getByDeptId(@PathVariable("deptId") @NotNull Long deptId);

    @GetMapping(PREFIX + "/createOrRemove")
    @Operation(summary ="添加/删除契约锁员工")
    CommonResult<Boolean> employeeCreateOrRemove(@Valid @RequestBody EmployeeCreateOrRemoveDTO employeeCreateOrRemoveDTO);
}
