package com.newtouch.uctp.module.business.controller.app.qys;

import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.operatelog.core.annotations.OperateLog;
import com.newtouch.uctp.module.business.controller.app.contact.vo.QYSContractVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.*;
import com.newtouch.uctp.module.business.convert.qys.QysConfigConvert;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;
import com.newtouch.uctp.module.system.api.user.dto.AddAccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.newtouch.uctp.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 契约锁")
@RestController
@RequestMapping("/uctp/qys")
@Validated
public class QysConfigController {

    @Resource
    private QysConfigService qysConfigService;

    @PostMapping("/create")
   @Operation(summary ="创建契约锁")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:create')")
    public CommonResult<Long> createQysConfig(@Valid @RequestBody QysConfigCreateReqVO createReqVO) {
        return success(qysConfigService.createQysConfig(createReqVO));
    }

    @PutMapping("/update")
   @Operation(summary ="更新契约锁")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:update')")
    public CommonResult<Boolean> updateQysConfig(@Valid @RequestBody QysConfigUpdateReqVO updateReqVO) {
        qysConfigService.updateQysConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
   @Operation(summary ="删除契约锁")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:delete')")
    public CommonResult<Boolean> deleteQysConfig(@RequestParam("id") Long id) {
        qysConfigService.deleteQysConfig(id);
        return success(true);
    }

    @GetMapping("/get")
   @Operation(summary ="获得契约锁")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:query')")
    public CommonResult<QysConfigRespVO> getQysConfig(@RequestParam("id") Long id) {
        QysConfigDO qysConfig = qysConfigService.getQysConfig(id);
        return success(QysConfigConvert.INSTANCE.convert(qysConfig));
    }

    @GetMapping("/list")
    @Operation(summary ="获得契约锁列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:query')")
    public CommonResult<List<QysConfigRespVO>> getQysConfigList(@RequestParam("ids") Collection<Long> ids) {
        List<QysConfigDO> list = qysConfigService.getQysConfigList(ids);
        return success(QysConfigConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
   @Operation(summary ="获得契约锁分页")
//    @PreAuthorize("@ss.hasPermission('uctp:qys-config:query')")
    public CommonResult<PageResult<QysConfigRespVO>> getQysConfigPage(@Valid @RequestBody QysConfigPageReqVO pageVO) {
        PageResult<QysConfigDO> pageResult = qysConfigService.getQysConfigPage(pageVO);
        return success(QysConfigConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/init")
    @Operation(summary ="初始化契约锁client")
    public CommonResult<Boolean> initLocalCache() {
        qysConfigService.initLocalCache();
        return success(true);
    }

    @PostMapping("/callback/certification")
    @Operation(summary = "saas模式契约锁回调-企业认证")
    public String callbackCertification(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) throws Exception {
        return qysConfigService.callbackCertification(signature, timestamp, content);
    }

    @PostMapping("/callback/certification/person")
    @Operation(summary = "saas模式契约锁回调-个人认证")
    public String callbackCertificationPerson(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) throws Exception {
        return qysConfigService.callbackCertificationPerson(signature, timestamp, content);
    }
    @PostMapping("/callback/privilege")
    @Operation(summary = "saas模式契约锁回调-企业授权")
    public String callbackPrivilege(@RequestParam String signature,
                                        @RequestParam String timestamp,
                                        @RequestParam String content) throws Exception {
        return qysConfigService.callBackPrivilege(signature, timestamp, content);
    }
    @PostMapping("/callback/status")
    @Operation(summary = "saas模式契约锁回调-合同状态")
    public String callbackStatus(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) throws Exception {
        return qysConfigService.status(signature, timestamp, content);
    }
    @PostMapping("/callback/verification")
    @Operation(summary = "saas模式契约锁回调-CAS验证地址")
    public Map<String, Object> callbackVerification(@RequestParam String ticket) throws Exception {
        return qysConfigService.verification(ticket);
    }

    @GetMapping("/sso/url")
    @Operation(summary = "获取单点登录地址")
    @Parameter(name = "pageType", description = "契约锁页面类型", required = true, example = "单点登录指定的目标页面，默认为主页\n" +
            "INDEX_PAGE（“主页”）、SEAL_PAGE（“公司公章页面”）、\n" +
            "TEMPLATE_PAGE（“模板页面”） 、                     CATEGORY_PAGE（“公司业务分类页面”）、ROLE_PAGE（“公司角色权限页面”）、CONTRACT_LIST_PAGE（“合同列表页面”）、\n" +
            "CONTRACT_STATISTICS_PAGE（“公司合同统计页面”）、\n" +
            "COMPANY_INFO_PAGE（“公司基本信息页面”）、\n" +
            "ORGANIZE_PAGE（“公司组织架构页面“）、\n" +
            "COMPANY_FEE_PAGE（”公司费用中心“）、\n" +
            "CONTRACT_DETAIL_PAGE（”合同签署页面“）、\n" +
            "COMPANY_INFO_SETUP_PAGE（”公司设置页面“）。\n" +
            "H5页面仅支持CONTRACT_LIST_PAGE、CONTRACT_DETAIL_PAGE、INDEX_PAGE\n" +
            "以个人身份单点登录时，仅支持指定以上页面说明中，非公司开头的页面\n")
    @Parameter(name = "contractId", description = "合同id", required = true, example = "如果pageType是CONTRACT_DETAIL_PAGE，必填")
    public CommonResult<String> ssoUrl(@RequestParam String pageType,
                                       @RequestParam Long contractId) throws Exception {
        String ssoUrl = qysConfigService.getSsoUrl(pageType,contractId);
        return success(ssoUrl);
    }

    @PostMapping("/callback/login")
    @Operation(summary = "saas模式契约锁回调-登录")
    public String callbackLogin(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String content) {
        return qysConfigService.login(signature, timestamp, content);
    }

    @PostMapping("/send")
    @Operation(summary = "发起契约锁合同")
    public CommonResult<String> send(@Valid @RequestBody QYSContractVO qysContractVO) {
        String result = "";
        //这里只发起委托合同
//        if (qysContractVO.getContractType().equals("1")) {
            result = qysConfigService.send(qysContractVO.getContractId(),true);
//        }
        return success(result);
    }

    @PostMapping("/ContractEcho")
    @Operation(summary ="合同回显")
    @Parameter(name = "carId", description = "车辆id", required = true, example = "1024")
    @Parameter(name = "type", description = "收车或卖车（1：收车，2：卖车）", required = true, example = "1")
    public CommonResult<List<QYSContractVO>> ContractEcho(@RequestParam("carId") @NotNull  Long carId, @RequestParam("type") String type) {
        return success(qysConfigService.ContractEcho(carId,type));
    }

    @GetMapping("/FindBuyAndWTContract")
    @Operation(summary ="收车合同/委托合同查看")
    @Parameter(name = "carId", description = "车辆id", required = true, example = "1024")
    public CommonResult<List<QYSContractVO>> FindQYSContract(@RequestParam("carId") @NotNull  Long carId) {
        return success(qysConfigService.FindQYSContract(carId));
    }

    @PostMapping("/user/auth")
    @PermitAll
    @Operation(summary ="个人认证")
//    @Parameter(name = "userId", description = "用户id", required = true, example = "1024")
    public CommonResult<Boolean> userAuth(@RequestBody @Valid  Map map) {
        Long userId = Long.valueOf(map.get("userId").toString());
        qysConfigService.userAuth(userId);
        return success(true);
    }
    @PostMapping("/company/sign/{contractId}")
    @Operation(summary ="公司默静签章")
    public CommonResult<Boolean> companySign(@PathVariable("contractId") @NotNull  Long contractId) {
        qysConfigService.companySign(contractId);
        return success(true);
    }

    @PostMapping("/company/gyhlsign/{contractId}")
    @Operation(summary ="公司静默签章")
    public CommonResult<Boolean> companyGyhlSign(@PathVariable("contractId") @NotNull  Long contractId) {
        qysConfigService.companyGyhlSign(contractId);
        return success(true);
    }

    @GetMapping("/test")
    @Operation(summary ="测试Id")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @Parameter(name = "type", description = "类型", required = true, example = "1024")
    public CommonResult<Long> testId(@RequestParam("id") @NotNull Long id,
                                     @RequestParam("type") @NotNull Integer type) throws Exception {
        qysConfigService.test(id,type);
        return success(id);
    }

    @PostMapping("/addAccount")
    @PermitAll
    @Operation(summary = "新增&修改子账号")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Map> addAccount(@RequestBody @Valid AddAccountDTO reqVO) {
        Map map = qysConfigService.addAccount(reqVO);
        return success(map);
    }

    @GetMapping("/deleteAccount")
    @Operation(summary = "删除子账号")
    @Parameter(name = "userId", description = "用户id", required = true, example = "1650085024672796674")
    public CommonResult<Integer> deleteAccount(@RequestParam("userId") Long userId) {
        return success(qysConfigService.deleteAccount(userId));
    }
}
