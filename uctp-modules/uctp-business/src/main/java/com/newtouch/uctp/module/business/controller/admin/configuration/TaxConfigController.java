package com.newtouch.uctp.module.business.controller.admin.configuration;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.excel.core.util.ExcelUtils;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;
import com.newtouch.uctp.module.business.service.configuration.TaxConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

/**
 * @ClassName TaxConfigController
 * @Author: zhang
 * @Date 2023/5/6
 */
@Tag(name = "配置管理 - 税率配置")
@RestController
@RequestMapping("/uctp/configuration/tax-config")
@Validated
public class TaxConfigController {
    @Resource
    private TaxConfigService taxConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建税率配置信息")
    public CommonResult<Boolean> createPost(@Valid @RequestBody TaxRespVO reqVO) {
        taxConfigService.createTax(reqVO);
        return success(true);
    }

    @GetMapping("/acquire")
    @Operation(summary = "获取税率配置信息")
    @Parameter(name = "type")
    public CommonResult<List<TaxRespVO>> getAcquire(@RequestParam("type") String type) {
        List<TaxRespVO> acquire = taxConfigService.getAcquire(type);
        return success(acquire);
    }

    @GetMapping("/page")
    @Operation(summary = "获取税率配置分页")
    public CommonResult<PageResult<TaxRespVO>> getTax(TaxReqVO pageVO) {
        return success(taxConfigService.getTaxPage(pageVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改税率配置")
    @PreAuthorize("@ss.hasPermission('uctp:tax:update')")
    public CommonResult<Boolean> updatePost(@Valid @RequestBody TaxUpdateReqVO reqVO) {
        taxConfigService.updateTax(reqVO);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('uctp:tax:query')")
    @Operation(summary = "获得税率配置")
    @Parameter(name = "id")
    public CommonResult<TaxRespVO> getTax(@RequestParam("id") Long id) {
        return success(taxConfigService.getTax(id));
    }

    @GetMapping("/export")
    @Operation(summary = "导出税率配置 Excel")
    @OperateLog(type = EXPORT)
    public void export(HttpServletResponse response, @Validated TaxExportReqVO reqVO) throws IOException {
        List<TaxExcelVO> data = taxConfigService.exportTax(reqVO);
        // 输出
        ExcelUtils.write(response, "配置管理.xls", "税率配置管理", TaxExcelVO.class, data);
    }
}
