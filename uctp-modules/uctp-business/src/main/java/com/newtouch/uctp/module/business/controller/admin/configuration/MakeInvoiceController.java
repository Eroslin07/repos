package com.newtouch.uctp.module.business.controller.admin.configuration;

import com.newtouch.uctp.framework.common.pojo.CommonResult;

import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceRespVO;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.MakeInvoiceUpdateReqVO;
import com.newtouch.uctp.module.business.service.configuration.MakeInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

/**
 * @ClassName MakeInvoiceController
 * @Author: zhang
 * @Date 2023/4/24.
 */
@Tag(name = "配置管理 - 开票信息配置")
@RestController
@RequestMapping("/uctp/configuration/makeinvoice")
@Validated
public class MakeInvoiceController {

@Resource
private MakeInvoiceService makeService;

    @PostMapping("/create")
    @Operation(summary = "创建配置信息")
    public CommonResult<Boolean> createPost(@Valid @RequestBody MakeInvoiceRespVO reqVO) {
        makeService.createMakeInvoice(reqVO);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "编辑配置信息")
    public CommonResult<Boolean> updatePost(@Valid @RequestBody MakeInvoiceUpdateReqVO reqVO) {
        makeService.updateMakeInvoice(reqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取配置信息")
    @Parameter(name = "tenantId")
    public CommonResult<MakeInvoiceRespVO> getMakeInvoice(@RequestParam("tenantId") Long tenantId) {
        return success(makeService.getMakeInvoice(tenantId));
    }
}
