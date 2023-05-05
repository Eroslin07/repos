package com.newtouch.uctp.module.business.controller.admin.settlement;

import com.newtouch.uctp.framework.excel.core.util.ExcelUtils;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.admin.settlement.vo.*;
import com.newtouch.uctp.module.business.service.settlement.InvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;

import java.io.IOException;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.operatelog.core.enums.OperateTypeEnum.*;

@Tag(name = "结算中心 - 发票管理实例")
@RestController
@RequestMapping("/uctp/settlement/invoice")
@Validated
public class InvoiceController {

    @Resource
    private InvoiceService invoiceService;

    @GetMapping("/page")
    @Operation(summary = "获取发票管理分页")
    public CommonResult<PageResult<InvoiceRespVO>> getInvoice(InvoiceReqVO pageVO) {
        return success(invoiceService.getInvoicePage(pageVO));
    }

    @GetMapping("/export")
    @Operation(summary = "导出发票管理 Excel")
    @OperateLog(type = EXPORT)
    public void export(HttpServletResponse response, @Validated InvoiceExportReqVO reqVO) throws IOException {
        List<InvoiceExcelVO> data = invoiceService.getInvoice(reqVO);
        // 输出
        ExcelUtils.write(response, "结算中心.xls", "发票管理", InvoiceExcelVO.class, data);
    }
}