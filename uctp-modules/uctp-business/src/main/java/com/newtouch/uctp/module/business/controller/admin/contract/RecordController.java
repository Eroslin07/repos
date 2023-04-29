package com.newtouch.uctp.module.business.controller.admin.contract;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.excel.core.util.ExcelUtils;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;
import com.newtouch.uctp.module.business.service.contract.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;
import static com.newtouch.uctp.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

/**
 * @ClassName RecordController
 * @Author: zhang
 * @Date 2023/4/21.
 */
@Tag(name = "合同管理 - 合同档案管理")
@RestController
@RequestMapping("/uctp/contract/record")
@Validated
public class RecordController {
    @Resource
    private RecordService recordService;

    @GetMapping("/page")
    @Operation(summary = "获取合同档案分页")
    public CommonResult<PageResult<RecordRespVO>> getRecord(RecordReqVO pageVO) {
        PageResult<RecordRespVO> recordPage = recordService.getRecordPage(pageVO);
        return success(recordPage);
    }

    @GetMapping("/export")
    @Operation(summary = "导出合同档案 Excel")
    @OperateLog(type = EXPORT)
    public void recordExport(HttpServletResponse response, @Validated RecordExportReqVO reqVO) throws IOException {
        List<RecordExport> data = recordService.exportRecord(reqVO);
        // 输出
        ExcelUtils.write(response, "合同管理.xls", "合同档案管理", RecordExport.class, data);
    }
}
