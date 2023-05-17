package com.newtouch.uctp.module.business.controller.app.pos;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.app.pos.vo.AddPosReqVO;
import com.newtouch.uctp.module.business.controller.app.pos.vo.PosRespVO;
import com.newtouch.uctp.module.business.dal.dataobject.PosDO;
import com.newtouch.uctp.module.business.service.PosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name =  "App管理 - pos机管理")
@RestController
@RequestMapping("/uctp/pos")
@Validated
public class AppPosController {

    @Autowired
    private PosService posService;

    @PostMapping("/addPos")
    @PermitAll
    @Operation(summary = "新增&修改pos机")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> addAccount(@RequestBody @Valid AddPosReqVO reqVO) {
        posService.addPos(reqVO);
        return success(Boolean.TRUE);
    }


    @GetMapping("/deletePos")
    @Operation(summary = "删除POS机")
    @Parameter(name = "userId", description = "用户id", required = true, example = "1650085024672796674")
    public CommonResult<Integer> deleteAccount(@RequestParam("userId") Long id) {
        return success(posService.deletePos(id));

    }

    @GetMapping("/getPosList")
    @Operation(summary = "获取POS机列表")
    @Parameter(name = "deptId", description = "商户id", required = true, example = "263")
    public CommonResult<List<PosDO>> getPosList(@RequestParam("deptId") Long deptId) {
        return success(posService.getPosList(deptId));
    }

    @GetMapping("/getPosListDrop")
    @Operation(summary = "获取POS机列表下拉")
    @Parameter(name = "deptId", description = "商户id", required = true, example = "263")
    public CommonResult<List<PosDO>> getPosListDrop(@RequestParam("deptId") Long deptId) {
        return success(posService.getPosListDrop(deptId));
    }

}
