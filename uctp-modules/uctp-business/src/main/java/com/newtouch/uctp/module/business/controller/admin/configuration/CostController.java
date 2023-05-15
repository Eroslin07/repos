package com.newtouch.uctp.module.business.controller.admin.configuration;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.excel.core.util.ExcelUtils;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.admin.configuration.vo.*;
import com.newtouch.uctp.module.business.service.configuration.CostToService;
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
 * @ClassName CostController
 * @Author: zhang
 * @Date 2023/5/13
 */
@Tag(name = "配置管理 - 费用明细配置")
@RestController
@RequestMapping("/uctp/configuration/cost")
@Validated
public class CostController {
    @Resource
    private CostToService costToService;

    @PostMapping("/create")
    @Operation(summary = "创建费用配置信息")
    public CommonResult<Boolean> createPost(@Valid @RequestBody CostRespVO reqVO) {
        costToService.createCost(reqVO);
        return success(true);
    }

    @GetMapping("/acquire")
    @Operation(summary = "获取费用配置信息")
    @Parameter(name = "type")
    public CommonResult<List<CostRespVO>> getCostAcquire(@RequestParam("type") Integer type) {
        List<CostRespVO> acquire = costToService.getCostAcquire(type);
        return success(acquire);
    }

    @GetMapping("/page")
    @Operation(summary = "获取费用配置分页")
    public CommonResult<PageResult<CostRespVO>> getCost(CostReqVO pageVO) {
        return success(costToService.getCostPage(pageVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改费用配置")
    @PreAuthorize("@ss.hasPermission('uctp:cost:update')")
    public CommonResult<Boolean> updatePost(@Valid @RequestBody CostUpdateReqVO reqVO) {
        costToService.updateCost(reqVO);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('uctp:cost:query')")
    @Operation(summary = "获得费用配置")
    @Parameter(name = "id")
    public CommonResult<CostRespVO> getCost(@RequestParam("id") Long id) {
        return success(costToService.getCost(id));
    }

    @GetMapping("/export")
    @Operation(summary = "导出费用配置 Excel")
    @OperateLog(type = EXPORT)
    public void export(HttpServletResponse response, @Validated CostExportReqVO reqVO) throws IOException {
        List<CostExcelVO> data = costToService.exportCost(reqVO);
        // 输出
        ExcelUtils.write(response, "配置管理.xls", "费用配置管理", CostExcelVO.class, data);
    }
}
