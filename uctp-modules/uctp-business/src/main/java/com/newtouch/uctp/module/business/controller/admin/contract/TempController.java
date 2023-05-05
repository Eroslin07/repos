package com.newtouch.uctp.module.business.controller.admin.contract;

import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.excel.core.util.ExcelUtils;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.admin.contract.vo.*;
import com.newtouch.uctp.module.business.service.contract.TempService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * @ClassName TempController
 * @Author: zhang
 * @Date 2023/4/19.
 */
@Tag(name = "合同管理 - 合同模板")
@RestController
@RequestMapping("/uctp/contract/temp")
@Validated
public class TempController {
    @Resource
    private TempService tempService;

    @GetMapping("/page")
    @Operation(summary = "获取合同模板分页")
    public CommonResult<PageResult<TempRespVO>> getTemp(TempReqVO pageVO) {
        return success(tempService.getTempPage(pageVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改模板")
    public CommonResult<Boolean> updatePost(@Valid @RequestBody TempUpdateReqVO reqVO) {
        tempService.updateTemp(reqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同模板")
    @Parameter(name = "id")
    public CommonResult<TempRespVO> getTemp(@RequestParam("id") Long id) {
        return success(tempService.getTemp(id));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除通知公告")
    @Parameter(name = "id")
    public CommonResult<Boolean> deleteNotice(@RequestParam("id") Long id) {
        tempService.deleteTemp(id);
        return success(true);
    }

    @GetMapping("/export")
    @Operation(summary = "导出合同模板 Excel")
    @OperateLog(type = EXPORT)
    public void export(HttpServletResponse response, @Validated TempExportReqVO reqVO) throws IOException {
        List<TempExcelVO> data = tempService.exportTemp(reqVO);
        // 输出
        ExcelUtils.write(response, "合同管理.xls", "合同模板管理", TempExcelVO.class, data);
    }
}
