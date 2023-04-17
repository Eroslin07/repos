package com.newtouch.uctp.module.business.controller.app.qiyuesuo;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigRespVO;
import com.newtouch.uctp.module.business.controller.app.qiyuesuo.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.convert.qysconfig.QysConfigConvert;
import com.newtouch.uctp.module.business.dal.dataobject.qysconfig.QysConfigDO;
import com.newtouch.uctp.module.business.service.qysconfig.QysConfigService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 契约锁")
@RestController
@RequestMapping("/uctp/qys")
@Validated
public class QysConfigController {

    @Resource
    private QysConfigService qysConfigService;

    @PostMapping("/create")
   @Schema(description ="创建契约锁")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:create')")
    public CommonResult<Long> createQysConfig(@Valid @RequestBody QysConfigCreateReqVO createReqVO) {
        return success(qysConfigService.createQysConfig(createReqVO));
    }

    @PutMapping("/update")
   @Schema(description ="更新契约锁")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:update')")
    public CommonResult<Boolean> updateQysConfig(@Valid @RequestBody QysConfigUpdateReqVO updateReqVO) {
        qysConfigService.updateQysConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
   @Schema(description ="删除契约锁")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:delete')")
    public CommonResult<Boolean> deleteQysConfig(@RequestParam("id") Long id) {
        qysConfigService.deleteQysConfig(id);
        return success(true);
    }

    @GetMapping("/get")
   @Schema(description ="获得契约锁")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:query')")
    public CommonResult<QysConfigRespVO> getQysConfig(@RequestParam("id") Long id) {
        QysConfigDO qysConfig = qysConfigService.getQysConfig(id);
        return success(QysConfigConvert.INSTANCE.convert(qysConfig));
    }

    @GetMapping("/list")
    @Schema(description ="获得契约锁列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:query')")
    public CommonResult<List<QysConfigRespVO>> getQysConfigList(@RequestParam("ids") Collection<Long> ids) {
        List<QysConfigDO> list = qysConfigService.getQysConfigList(ids);
        return success(QysConfigConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
   @Schema(description ="获得契约锁分页")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:query')")
    public CommonResult<PageResult<QysConfigRespVO>> getQysConfigPage(@Valid QysConfigPageReqVO pageVO) {
        PageResult<QysConfigDO> pageResult = qysConfigService.getQysConfigPage(pageVO);
        return success(QysConfigConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/callback/certification")
    @Schema(description = "saas模式契约锁回调-企业认证")
    public String callbackCertification(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) throws Exception {
        return qysConfigService.certification(signature, timestamp, content);
    }
    @PostMapping("/callback/status")
    @Schema(description = "saas模式契约锁回调-合同状态")
    public String callbackStatus(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) throws Exception {
        return qysConfigService.status(signature, timestamp, content);
    }
    @PostMapping("/callback/verification")
    @Schema(description = "saas模式契约锁回调-验证地址")
    public String callbackVerification(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) throws Exception {
        return qysConfigService.verification(signature, timestamp, content);
    }
    @PostMapping("/callback/login")
    @Schema(description = "saas模式契约锁回调-验证地址")
    public String callbackLogin(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) throws Exception {
        return qysConfigService.login(signature, timestamp, content);
    }

    @GetMapping("/test")
    @Schema(description ="测试Id")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Long> testId(@RequestParam("id") Long id) {
        return success(id);
    }

}
