package com.newtouch.uctp.module.business.controller.app.qys;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackRespVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysCallbackUpdateReqVO;
import com.newtouch.uctp.module.business.convert.qys.QysCallbackConvert;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysCallbackDO;
import com.newtouch.uctp.module.business.service.qys.QysCallbackService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Schema(description = "管理后台 - 契约锁回调日志")
@RestController
@RequestMapping("/uctp/qys-callback")
@Validated
public class QysCallbackController {

    @Resource
    private QysCallbackService qysCallbackService;

    @PostMapping("/create")
    @Schema(description ="创建契约锁回调日志")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-callback:create')")
    public CommonResult<Long> createQysCallback(@Valid @RequestBody QysCallbackCreateReqVO createReqVO) {
        return success(qysCallbackService.createQysCallback(createReqVO));
    }

    @PutMapping("/update")
    @Schema(description ="更新契约锁回调日志")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-callback:update')")
    public CommonResult<Boolean> updateQysCallback(@Valid @RequestBody QysCallbackUpdateReqVO updateReqVO) {
        qysCallbackService.updateQysCallback(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Schema(description ="删除契约锁回调日志")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('uctp:qys-callback:delete')")
    public CommonResult<Boolean> deleteQysCallback(@RequestParam("id") Long id) {
        qysCallbackService.deleteQysCallback(id);
        return success(true);
    }

    @GetMapping("/get")
    @Schema(description ="获得契约锁回调日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-callback:query')")
    public CommonResult<QysCallbackRespVO> getQysCallback(@RequestParam("id") Long id) {
        QysCallbackDO qysCallback = qysCallbackService.getQysCallback(id);
        return success(QysCallbackConvert.INSTANCE.convert(qysCallback));
    }

    @GetMapping("/list")
    @Schema(description ="获得契约锁回调日志列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-callback:query')")
    public CommonResult<List<QysCallbackRespVO>> getQysCallbackList(@RequestParam("ids") Collection<Long> ids) {
        List<QysCallbackDO> list = qysCallbackService.getQysCallbackList(ids);
        return success(QysCallbackConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Schema(description ="获得契约锁回调日志分页")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-callback:query')")
    public CommonResult<PageResult<QysCallbackRespVO>> getQysCallbackPage(@Valid QysCallbackPageReqVO pageVO) {
        PageResult<QysCallbackDO> pageResult = qysCallbackService.getQysCallbackPage(pageVO);
        return success(QysCallbackConvert.INSTANCE.convertPage(pageResult));
    }


}
