package com.newtouch.uctp.module.business.service.qys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.bpm.api.openinvoice.BpmOpenInvoiceApi;
import com.newtouch.uctp.module.bpm.enums.definition.BpmDefTypeEnum;
import com.newtouch.uctp.module.business.controller.app.account.cash.vo.TransactionRecordReqVO;
import com.newtouch.uctp.module.business.controller.app.contact.vo.QYSContractVO;
import com.newtouch.uctp.module.business.controller.app.qys.dto.ContractStatusDTO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.convert.qys.QysConfigConvert;
import com.newtouch.uctp.module.business.dal.dataobject.*;
import com.newtouch.uctp.module.business.dal.dataobject.cash.MerchantAccountDO;
import com.newtouch.uctp.module.business.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.business.dal.dataobject.user.UserExtDO;
import com.newtouch.uctp.module.business.dal.mysql.*;
import com.newtouch.uctp.module.business.dal.mysql.dept.DeptMapper;
import com.newtouch.uctp.module.business.dal.mysql.qys.QysCallbackMapper;
import com.newtouch.uctp.module.business.dal.mysql.qys.QysConfigMapper;
import com.newtouch.uctp.module.business.dal.mysql.user.UserExtMapper;
import com.newtouch.uctp.module.business.dal.mysql.user.UserMapper;
import com.newtouch.uctp.module.business.enums.CarStatus;
import com.newtouch.uctp.module.business.enums.QysCallBackType;
import com.newtouch.uctp.module.business.enums.QysContractStatus;
import com.newtouch.uctp.module.business.mq.message.UserAuthMessage;
import com.newtouch.uctp.module.business.mq.producer.UserAuthProducer;
import com.newtouch.uctp.module.business.service.BusinessFileService;
import com.newtouch.uctp.module.business.service.CarInfoDetailsService;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.business.service.NoticeService;
import com.newtouch.uctp.module.business.service.account.AccountCashService;
import com.newtouch.uctp.module.business.service.contract.ContractService;
import com.newtouch.uctp.module.business.service.contract.MerchantMoneyService;
import com.newtouch.uctp.module.business.util.*;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileCreateReqDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;
import com.newtouch.uctp.module.system.api.user.dto.AddAccountDTO;
import com.newtouch.uctp.module.system.api.user.dto.AdminUserRespDTO;
import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.EmployeeCreateRequest;
import com.qiyuesuo.sdk.v2.request.EmployeeRemoveRequest;
import com.qiyuesuo.sdk.v2.response.*;
import com.qiyuesuo.sdk.v2.utils.CryptUtils;
import com.qiyuesuo.sdk.v2.utils.IOUtils;
import com.qiyuesuo.sdk.v2.utils.MD5;
import com.qiyuesuo.sdk.v2.utils.StringUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;
import static com.newtouch.uctp.module.business.enums.QysConstants.SECRET;
import static com.newtouch.uctp.module.business.enums.QysContractStatus.INVALIDED;

/**
 * 契约锁 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class QysConfigServiceImpl implements QysConfigService {

    @Resource
    private QysConfigMapper qysConfigMapper;
    @Resource
    private QiyuesuoClientFactory qiyuesuoClientFactory;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private QysCallbackMapper qysCallbackMapper;
    @Resource
    private QysCallbackService qysCallbackService;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private UserMapper userMapper;

    @Resource
    private ContractService contractService;

    @Resource
    private CarInfoDetailsService carInfoDetailsService;
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private UserExtMapper userExtMapper;
    @Resource
    private DeptApi deptApi;
    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private BusinessFileService businessFileService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private InvoiceTitleMapper invoiceTitleMapper;
    @Resource
    private UserAuthProducer userAuthProducer;
    @Resource
    private MerchantAccountMapper merchantAccountMapper;
    @Resource
    private AccountCashService accountCashService;
    @Resource
    private MerchantMoneyService merchantMoneyService;
    @Resource
    private BpmOpenInvoiceApi bpmOpenInvoiceApi;

    @PostConstruct
    @Override
    public void initLocalCache() {
        // 第一步：查询数据
        List<QysConfigDO> configDOS = qysConfigMapper.selectListAuth();
        log.info("[initLocalCache][缓存契约锁client，数量为:{}]", configDOS.size());

        // 第二步：构建缓存：创建或更新短信 Client
//        List<QiyuesuoChannelProperties> propertiesList = QysConfigConvert.INSTANCE.convert01(configDOS);
//        propertiesList.forEach(properties -> qiyuesuoClientFactory.createOrUpdateQiyuesuoClient(properties));
        this.createOrUpdateQiyuesuoClient(configDOS);
    }

    private void createOrUpdateQiyuesuoClient(List<QysConfigDO> configDOS) {
        List<QiyuesuoChannelProperties> propertiesList = QysConfigConvert.INSTANCE.convert01(configDOS);
        propertiesList.forEach(properties -> qiyuesuoClientFactory.createOrUpdateQiyuesuoClient(properties));
    }

    @Override
    public Long createQysConfig(QysConfigCreateReqVO createReqVO) {
        // 插入
        QysConfigDO qysConfig = QysConfigConvert.INSTANCE.convert(createReqVO);
        qysConfigMapper.insert(qysConfig);
        //刷新client
        this.createOrUpdateQiyuesuoClient(Arrays.asList(qysConfig));
        // 返回
        return qysConfig.getId();
    }

    @Override
    public void updateQysConfig(QysConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateQysConfigExists(updateReqVO.getId());
        // 更新
        QysConfigDO updateObj = QysConfigConvert.INSTANCE.convert(updateReqVO);
        qysConfigMapper.updateById(updateObj);
        //刷新client
        this.createOrUpdateQiyuesuoClient(Arrays.asList(updateObj));
    }

    @Override
    public void deleteQysConfig(Long id) {
        // 校验存在
        validateQysConfigExists(id);
        // 删除
        qysConfigMapper.deleteById(id);
    }

    private void validateQysConfigExists(Long id) {
        if (qysConfigMapper.selectById(id) == null) {
            throw exception(QYS_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public QysConfigDO getQysConfig(Long id) {
        return qysConfigMapper.selectById(id);
    }

    @Override
    public List<QysConfigDO> getQysConfigList(Collection<Long> ids) {

        return qysConfigMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<QysConfigDO> getQysConfigPage(QysConfigPageReqVO pageReqVO) {
        return qysConfigMapper.selectPage(pageReqVO);
    }

    @GlobalTransactional
    @Override
    @Transactional
    public String callbackCertification(String signature, String timestamp, String content) throws Exception {
        log.info("[certification]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】", signature, timestamp, content);
        //验证签名
        if (!this.verificationSignature(signature, timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        JSONObject jsonObject = JSON.parseObject(json);
        String companyName = jsonObject.getString("companyName");
        String status = jsonObject.getString("status");
        String companyId = jsonObject.getString("companyId");
        String registerNo = jsonObject.getString("registerNo");
        //通过营业执照，和公司名称找到公司数据
        DeptDO deptDO = deptMapper.findByNameAndTaxNum(companyName, registerNo);
        if (ObjectUtil.isNotNull(deptDO)) {
            //fengin接口回调，如果要用feign 那么这里必须卸载回调里，不然报错没传参数 tenant-id
            TenantUtils.execute(deptDO.getTenantId(), () -> {
                WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID, deptDO.getTenantId());
                //设置当前登录人信息，免得保存报错
                List<AdminUserRespDTO> adminUserRespDTOs = adminUserApi.getUserListByDeptIds(ListUtil.of(deptDO.getId())).getCheckedData();
                if (CollUtil.isEmpty(adminUserRespDTOs)) {
                    log.error("认证公司失败，因为第一位员工不存在，公司：{},公司Id",deptDO.getName(),deptDO.getId());
                }
                WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), adminUserRespDTOs.get(0).getId());
                QysConfigDO configDO = qysConfigMapper.selectOne("COMPANY_ID", companyId);
                AdminUserRespDTO userRespDTO = null;
                //如果回调数据为认证成功，保存公司id
                if ("1".equals(status) && StrUtil.isNotBlank(companyId)) {
                    if (ObjectUtil.isNull(configDO)) {
                        configDO = new QysConfigDO();
                    }
                    configDO.setCompanyId(Long.valueOf(companyId));
                    configDO.setBusinessId(deptDO.getId());
                    configDO.setBusinessName(deptDO.getName());
                    configDO.setServerUrl("https://openapi.qiyuesuo.cn");

                    deptDO.setAuth(Integer.valueOf(status));
                    deptMapper.updateById(deptDO);
                    //保存回调信息
                    qysCallbackService.saveDO(json,
                            QysCallBackType.COMPANY_AUTH.value(), deptDO.getId());
                    //继续做企业授权
                    List<AdminUserRespDTO> checkedData = adminUserApi.getUserListByDeptIds(ListUtil.toList(deptDO.getId())).getCheckedData();
                    if (CollUtil.isNotEmpty(checkedData)) {
                        //此时只会存在一条当前部门下的用户数据
                        userRespDTO = checkedData.get(0);
//                    this.privilegeUrl(userRespDTO.getId());
                        QiyuesuoSaasClient saasClient = qiyuesuoClientFactory.getQiyuesuoSaasClient(1L);
                        SaaSPrivilegeUrlResult privilegeUrlResult = saasClient.saasPrivilegeUrl(configDO.getCompanyId(), userRespDTO.getMobile()
                                , ListUtil.of("SEAL", "TEMPLATE", "CONTRACT", "COMPANY_EMPLOYEE", "ROLE_PERMISSION",
                                        "BASE_INFO", "FILE_STATISTICS", "CATEGORY", "FEE", "COMPANY_SETUP")).getCheckedData();
                        log.info("企业功能授权【{}】,授权地址【{}】", deptDO.getName(), privilegeUrlResult.getPageUrl());
                        List<String> urls = ShortUrlsUtil.shortUrls(ListUtil.of(privilegeUrlResult.getPageUrl()));
                        Map<String, String> map = MapUtil
                                .builder("title", "企业token授权")
                                .put("contentType", "44")
                                .put("name", deptDO.getName())
                                .put("url", urls.get(0))
                                .put("phone", userRespDTO.getMobile())
                                .put("businessId", deptDO.getId().toString())
                                .put("type", "1").build();
                        noticeService.saveNotice(map);
                    } else {
                        log.error("[certification]根据返回的公司名称未查询到数据,companyName:{},tax_num:{}", companyName, registerNo);
                    }
                } else if ("2".equals(status) && StrUtil.isNotBlank(companyId)) {
                    //如果回调数据为认证失败，删除注册数据
                    //2023-05-08 ,删除此需求
//                    deptMapper.deleteById(deptDO);
//                    AdminUserDO adminUserDO = userMapper.selectById(userRespDTO.getId());
//                    if (ObjectUtil.isNotNull(adminUserDO)) {
//                        userMapper.deleteById(adminUserDO);
//                    }
//                    UserExtDO userExtDO = userExtMapper.selectOne("USER_ID", userRespDTO.getId());
//                    if (ObjectUtil.isNotNull(userExtDO)) {
//                        userExtMapper.deleteById(userExtDO);
//                    }
//                    if (ObjectUtil.isNotNull(configDO)) {
//                        qysConfigMapper.deleteById(configDO);
//                    }
                }
                if (ObjectUtil.isNotNull(configDO)) {
                    if (ObjectUtil.isNull(configDO.getId())) {
                        qysConfigMapper.insert(configDO);
                    } else {
                        qysConfigMapper.updateById(configDO);
                    }
                }
            });
        } else {
            log.error("[certification]根据返回的公司名称未查询到数据,companyName:{},tax_num:{}", companyName, registerNo);
        }
        return "success";
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public String status(String signature, String timestamp, String content) throws Exception {
        log.info("[status]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】", signature, timestamp, content);
        //验证签名
        if (!this.verificationSignature(signature, timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        ContractStatusDTO contractStatusDTO = JSONObject.parseObject(json, ContractStatusDTO.class);
        ContractDO contractDO = contractMapper.findByContractId(contractStatusDTO.getContractId());
        if (ObjectUtil.isNull(contractDO)) {
            log.warn("[status]电子签回调,未获取到合同,contractId:{}", contractStatusDTO.getContractId());
            return "fail";
        }
        TenantUtils.execute(contractDO.getTenantId(), () -> {
            WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID, contractDO.getTenantId());
            //设置当前登录人信息，免得保存报错
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(contractDO.getCreator()));
            CarInfoDO carInfo = carInfoService.getCarInfo(contractDO.getCarId());
            if (contractDO.getContractType().equals(1)) {
                //1收车委托合同
                switch (QysContractStatus.toType(contractStatusDTO.getContractStatus())) {
                    case COMPLETE:
                        //A 收车委托已完成->
                        //A-1 合同已完成
                        //A-2 车辆状态合同发起
                        //A-3 发起收车合同，
                        this.doService(contractDO, carInfo, 2,null
                                , CarStatus.COLLECT.value(),
                                CarStatus.COLLECT_B.value(),
                                CarStatus.COLLECT_B_C.value(),
                                Boolean.TRUE,
                                Boolean.FALSE);
                        //下载合同签章文件
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        break;
                    case INVALIDED:
                        //B 收车委托已作废->
                        //B-1 合同作废
                        //B-2 车辆状态
                        this.doService(contractDO, carInfo, null,1
                                , CarStatus.COLLECT.value(),
                                CarStatus.COLLECT_A.value(),
                                CarStatus.COLLECT_A_A.value(),
                                Boolean.FALSE,
                                Boolean.FALSE);
                        //下载合同签章文件
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        break;
                    case SIGNING:

                        break;
                    case REJECTED:
                        //这里合同是 “签署中” 状态时，撤回合同，合同变为 “已撤回” 状态,此时作废收车委托合同
                        //这是作废原因为：卖家已主动作废
                        contractService.entrustContractInvalid(contractDO,"卖家已主动作废");
                        break;
                }
            } else if (contractDO.getContractType().equals(2)) {
                //2收车合同
                switch (QysContractStatus.toType(contractStatusDTO.getContractStatus())) {
                    case COMPLETE:
                        //C 收车合同已完成->
                        //C-1 合同已完成，
                        //C-2 车辆状态待支付
                        this.doService(contractDO, carInfo, 2,null
                                , CarStatus.COLLECT.value(),
                                CarStatus.COLLECT_B.value(),
                                CarStatus.COLLECT_B_D.value(),
                                Boolean.FALSE,
                                Boolean.TRUE);
                        // TODO: 暂时模拟收车合同签署完成，自动跳过支付进行开票（后续需删除）
                        log.info("[status]收车合同：signature【{}】,timestamp【{}】,content【{}】", signature, timestamp, content);
                        this.bpmOpenInvoiceApi.createOpenInvoiceBpm(contractDO.getContractId(), BpmDefTypeEnum.SCKP.name()).getCheckedData();
                        //下载签章合同
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        break;
                    case INVALIDED:
                        //D 收车合同已作废->
                        //D-1 合同作废
                        //D-2 车辆状态
                        this.doService(contractDO, carInfo, null,1
                                , CarStatus.COLLECT.value(),
                                CarStatus.COLLECT_A.value(),
                                CarStatus.COLLECT_A_A.value(),
                                Boolean.FALSE,
                                Boolean.FALSE);
                        this.colCarContractInvalided(contractDO.getCarId());
                        //释放收车保证金预占
                        merchantMoneyService.releaseCash(contractDO.getContractId());
                        //下载合同签章文件
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        //发起委托收车合同作废
                        contractService.entrustContractInvalid(contractDO,contractDO.getInvalidedReason());
                        break;
                    case SIGNING:
                        //如果是个人签署，进行企业静默签章
                        if (ObjectUtil.equals("PERSONAL", contractStatusDTO.getTenantType())) {
                            this.doService(contractDO, carInfo, 1,null
                                    , CarStatus.COLLECT.value(),
                                    CarStatus.COLLECT_B.value(),
                                    CarStatus.COLLECT_B_C.value(),
                                    Boolean.FALSE,
                                    Boolean.FALSE);
                            this.companySign(contractDO.getContractId());
                        }
                        break;
                    case REJECTED:
                        //这里合同是 “签署中” 状态时，撤回合同，合同变为 “已撤回” 状态,此时作废收车委托合同
                        //这是作废原因为：买家已主动作废
                        contractService.entrustContractInvalid(contractDO,"买家已主动作废");
                        break;
                }
            } else if (contractDO.getContractType().equals(3)) {
                //3卖车委托合同
                switch (QysContractStatus.toType(contractStatusDTO.getContractStatus())) {
                    case COMPLETE:
                        //E 卖车委托已完成->
                        //E-1 合同已完成
                        //E-2 车辆状态合同已发起
                        //E-3 发起卖车合同
                        this.doService(contractDO, carInfo, 2,null
                                , CarStatus.SELL.value(),
                                CarStatus.SELL_B.value(),
                                CarStatus.SELL_B_C.value(),
                                Boolean.TRUE,
                                Boolean.FALSE);
                        //下载签章合同
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        break;
                    case INVALIDED:
                        //F 卖车委托合同已作废->
                        //F-1 合同作废
                        //F-2 车辆状态
                        this.doService(contractDO, carInfo, null,1
                                , CarStatus.SELL.value(),
                                CarStatus.SELL_A.value(),
                                CarStatus.SELL_A_A.value(),
                                Boolean.FALSE,
                                Boolean.FALSE);
                        //下载合同签章文件
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        break;
                    case SIGNING:
                        //签署中需要进行下一个企业静默签章
//                        this.companySign(contractDO.getContractId());
                        break;
                }
            } else if (contractDO.getContractType().equals(4)) {
                //4卖车合同
                switch (QysContractStatus.toType(contractStatusDTO.getContractStatus())) {
                    case COMPLETE:
                        //G 卖车合同已完成->
                        //G-1 合同已完成
                        //G-2 车辆状态合同已发起
                        this.doService(contractDO, carInfo, 2,null
                                , CarStatus.SELL.value(),
                                CarStatus.SELL_C.value(),
                                CarStatus.SELL_C_A.value(),
                                Boolean.FALSE,
                                Boolean.TRUE);
                        // TODO: 暂时模拟卖车合同签署完成，自动跳过支付进行开票（后续需删除）
                        log.info("[status]卖车合同完成：signature【{}】,timestamp【{}】,content【{}】", signature, timestamp, content);
                        this.bpmOpenInvoiceApi.createOpenInvoiceBpm(contractDO.getContractId(), BpmDefTypeEnum.MCKP.name()).getCheckedData();
                        //下载签章合同
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        break;
                    case INVALIDED:
                        //H 卖车合同已作废->
                        //H-1 合同作废
                        //H-2 车辆状态
                        this.doService(contractDO, carInfo, null,1
                                , CarStatus.SELL.value(),
                                CarStatus.SELL_A.value(),
                                CarStatus.SELL_A_A.value(),
                                Boolean.FALSE,
                                Boolean.TRUE);
                        this.sellCarContractInvalided(contractDO.getCarId());
                        //释放收车保证金预占
                        merchantMoneyService.releaseCash(contractDO.getContractId());
                        //下载合同签章文件
//                        this.updateContract(contractDO.getContractId());
                        contractService.contractDownload(contractDO.getContractId(),contractDO.getContractName());
                        //作废卖车委托合同
                        contractService.entrustContractInvalid(contractDO,contractDO.getInvalidedReason());
                        break;
                    case SIGNING:
                        //如果是个人签署，进行企业静默签章
                        if (ObjectUtil.equals("PERSONAL", contractStatusDTO.getTenantType())) {
                            this.doService(contractDO, carInfo, 1,null
                                    , CarStatus.SELL.value(),
                                    CarStatus.SELL_B.value(),
                                    CarStatus.SELL_B_C.value(),
                                    Boolean.FALSE,
                                    Boolean.FALSE);
                            this.companySign(contractDO.getContractId());
                        }
                        break;
                }
            }
        });
        return "success";
    }

    /**
     * 车辆收车合同作废并释放保证金预占
     */
    private void colCarContractInvalided(Long carId) {
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapper<ContractDO>()
                .eq(ContractDO::getCarId, carId)
                .in(ContractDO::getStatus, 1, 2)
                .in(ContractDO::getContractType, 1, 2));
        if (CollUtil.isEmpty(contractDOS)) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        for (int i = 0; i < contractDOS.size(); i++) {
            ContractDO contractDO = contractDOS.get(i);
            Contract contract = client.defaultContractDetail(contractDO.getContractId()).getCheckedData();
            if (!ObjectUtil.equals(INVALIDED.value(), contract.getStatus())) {
                //合同作废完成
                log.info("合同不是作废状态,合同id:{}",contractDO.getContractId());
                return;
            }
            if (ObjectUtil.equals(i,contractDOS.size())) {
                //修改合同作废，车辆变为草稿
                CarInfoDO carInfo = carInfoService.getCarInfo(carId);
                if (ObjectUtil.isNotNull(carInfo)) {
                    carInfo.setSalesStatus(CarStatus.COLLECT.value());
                    carInfo.setStatus(CarStatus.COLLECT_A.value());
                    carInfo.setStatusThree(CarStatus.COLLECT_A_A.value());
                    carInfoService.update(carInfo);
                }
                contractDO.setInvalided(1);
                contractMapper.updateById(contractDO);
            }
        }
    }

    /**
     * 车辆卖车合同作废并释放保证金预占
     */
    private void sellCarContractInvalided(Long carId) {
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapper<ContractDO>()
                .eq(ContractDO::getCarId, carId)
                .in(ContractDO::getStatus, 1, 2)
                .in(ContractDO::getContractType, 3, 4));
        if (CollUtil.isEmpty(contractDOS)) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        for (int i = 0; i < contractDOS.size(); i++) {
            ContractDO contractDO = contractDOS.get(i);
            Contract contract = client.defaultContractDetail(contractDO.getContractId()).getCheckedData();
            if (!ObjectUtil.equals(INVALIDED.value(), contract.getStatus())) {
                //合同作废完成
                log.info("合同不是作废状态,合同id:{}",contractDO.getContractId());
                return;
            }
            if (ObjectUtil.equals(i,contractDOS.size())) {
                //修改合同作废，车辆变为草稿
                CarInfoDO carInfo = carInfoService.getCarInfo(carId);
                if (ObjectUtil.isNotNull(carInfo)) {
                    carInfo.setSalesStatus(CarStatus.SELL.value());
                    carInfo.setStatus(CarStatus.SELL_A.value());
                    carInfo.setStatusThree(CarStatus.SELL_A_A.value());
                    carInfoService.update(carInfo);
                }
                contractDO.setInvalided(1);
                contractMapper.updateById(contractDO);
            }
        }
    }

    /**
     * 合同状态回调业务处理
     *
     * @param contractDO    合同
     * @param carInfo       车辆
     * @param contratStatus 合同状态
     * @param invalided     作废状态
     * @param salesStatus   车辆一级状态
     * @param status        车辆二级状态
     * @param statusThree   车辆三级状态
     * @param send          是否发起合同
     * @param pay           是否调用支付
     */
    private void doService(ContractDO contractDO, CarInfoDO carInfo,
                           Integer contratStatus,
                           Integer invalided,
                           Integer salesStatus,
                           Integer status,
                           Integer statusThree,
                           Boolean send,
                           Boolean pay
    ) {
        //合同已完成
        if (ObjectUtil.isNotNull(contratStatus)) {
            contractDO.setStatus(contratStatus);
        }
        if (ObjectUtil.isNotNull(invalided)) {
            contractDO.setInvalided(invalided);
        }
        if (ObjectUtil.equals(contratStatus, 1)) {
            //表示合同完成，签署时间
            contractDO.setSigningDate(LocalDateTime.now());
        }
        //车辆状态
        carInfo.setSalesStatus(salesStatus);
        carInfo.setStatus(status);
        carInfo.setStatusThree(statusThree);
        // 发起合同
        if (send) {
            //这里只会发起收/卖车合同，所以，合同状态是在原来的委托+1
            ContractDO collectContractDO = contractService.getCollectDraft(carInfo.getId(), contractDO.getContractType() + 1);
            if (ObjectUtil.isNotNull(collectContractDO)) {
//                QysConfigDO configDO = getByDeptId(contractDO.getBusinessId());
                QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
                client.defaultContractSend(collectContractDO.getContractId()).getCheckedData();
            } else {
                log.error("数据异常，未找到合同数据，carId:{},contractType:{},tenantId:{}", carInfo.getId(), contractDO.getContractType(), contractDO.getTenantId());
            }
        }
        contractService.update(contractDO);
        carInfoService.update(carInfo);
        //TODO 调用支付接口
        if (pay) {

        }

    }

    @Override
    public Map<String, Object> verification(String ticket) {
        log.info("[verification]电子签CAS校验：ticket【{}】", ticket);
        Map<String, Object> retMap = new HashMap<>();
        //拿到 aes 密钥
//        redisTemplate.opsForValue().set("ticket:"+ticket,"密钥");
        String aesKeyStr = redisTemplate.opsForValue().get("ticket:" + ticket);
        if (StrUtil.isBlank(aesKeyStr)) {
            log.error("[verification]电子签CAS校验：失败，未获取到redis的密钥[ticket:{}]", ticket);
            retMap.put("code", -1);
            return retMap;
        }
        byte[] aesKey = Byte2StrUtil.toByteArray(aesKeyStr);
        AES aes = SecureUtil.aes(aesKey);
        //解密 获取到userId
        String userId = aes.decryptStr(ticket, CharsetUtil.CHARSET_UTF_8);
        //查询到用户数据并返回
        AdminUserDO adminUser = userMapper.findById(Long.valueOf(userId));
        if (ObjectUtil.isNull(adminUser)) {
            log.error("[verification]电子签CAS校验：失败，未获取到用户userId:{}", userId);
            retMap.put("code", -1);
            return retMap;
        }
//        AdminUserDO adminUser = userMapper.selectById(userId);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("contract", adminUser.getMobile());
        userMap.put("name", adminUser.getNickname());

        retMap.put("code", 0);
        retMap.put("message", "SUCCESS");
        retMap.put("result", userMap);
        return retMap;
    }

    @Override
    public String login(String signature, String timestamp, String content) {
        TenantUtils.execute(150L, () -> {
            WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID, 150L);
            //设置当前登录人信息，免得保存报错
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(236L));
            QysConfigDO configDO = new QysConfigDO();
            configDO.setCode("default");
            configDO.setStatus(0);
            configDO.setBusinessId(324L);
//            configDO.setServerUrl("https://openapi.qiyuesuo.cn");
            configDO.setBusinessName("成都森斯尔科技有限公司1");
            configDO.setCompanyId(3093050858133467346L);
            configDO.setTenantId(150L);
            configDO.setAccessKey("5XsdYvfo1v");
            configDO.setAccessSecret("GxmoAnxUQrSMyTQ0pfxBsUN9yOZfK3");
            qysConfigMapper.insert(configDO);
            this.initLocalCache();
            QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(configDO.getId());
            System.out.println("woqu");
        });
        return "success";
    }

    @Override
    public void test(Long id, Integer type) throws Exception {
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(1656959683577090050L);
        QiyuesuoSaasClient saasClient = qiyuesuoClientFactory.getQiyuesuoSaasClient(1L);
//        qiyuesuoClient.defaultDraftSend(null);
//        qiyuesuoSaasClient.saasCompanyAuthPageUrl(null);
        if (type.equals(0)) {
            this.companyAuth(id);
        } else if (type.equals(1)) {
            this.userAuth(id);
        } else if (type.equals(2)) {
            DateTime authDeadline = DateUtil.offset(DateUtil.date(), DateField.MONTH, 12);
            SaaSSealSignAuthUrlResult checkedData = saasClient.saasSealSignAuthUrl("17380123816", 3088322841008022468L, DateUtil.formatDate(authDeadline), "授权静默签章").getCheckedData();
            System.out.println(checkedData.getPageUrl());
        } else if (type.equals(3)) {
            this.companySign(id);
        } else if (type.equals(4)) {
            SaaSPrivilegeUrlResult checkedData = saasClient.saasPrivilegeUrl(3088322841008022468L, "17380123816", ListUtil.of("CONTRACT")).getCheckedData();
            System.out.println(checkedData);
        } else if (type.equals(5)) {
            userAuthProducer.sendUserAuthMessage(666L, "17396202169", UserAuthProducer.FIVE_MINUTES);
        } else if (type.equals(6)) {
            contractService.contractDownload(id,"二手车委托合同");
        }
    }

    @Override
    @Transactional
    public String send(Long contractId, Boolean hasReserve) {
        ContractDO contractDO = contractMapper.selectOne(ContractDO::getContractId, contractId);
        if (ObjectUtil.equals(1, contractDO.getContractType()) && hasReserve) {
           // ContractDO contractDO1 = contractMapper.selectOne(ContractDO::getCarId, contractDO.getCarId(), ContractDO::getContractType, 2);
            ContractDO contractDO1 = contractMapper.selectOne(new LambdaQueryWrapper<ContractDO>().eq(ContractDO::getCarId, contractDO.getCarId()).eq(ContractDO::getContractType, 2).eq(ContractDO::getInvalided, 0));
            //保证金预占
            Boolean reserveCash = merchantMoneyService.reserveCash(contractDO1.getContractId());
            if (!reserveCash) {
                throw exception(ACC_RESERVECASH_ERROR);
            }
        }
        //这里修改合同状态,车辆状态
        contractDO.setStatus(1);
        contractService.update(contractDO);
        CarInfoDO carInfo = carInfoService.getCarInfo(contractDO.getCarId());
        //合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
        if (ObjectUtil.equals(1, contractDO.getContractType())) {
            carInfo.setSalesStatus(CarStatus.COLLECT.value());
            carInfo.setStatus(CarStatus.COLLECT_B.value());
            carInfo.setStatusThree(CarStatus.COLLECT_B_B.value());
        }
        else if (ObjectUtil.equals(3, contractDO.getContractType())) {
            carInfo.setSalesStatus(CarStatus.SELL.value());
            carInfo.setStatus(CarStatus.SELL_B.value());
            carInfo.setStatusThree(CarStatus.SELL_B_B.value());
        }
        carInfoService.update(carInfo);
        //这里必须要市场方发起
//        QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", platformDept.getId());
        //发起方为平台方，平台方ID 为2L
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        //合同发起
        client.defaultContractSend(contractId).getCheckedData();
        //ContractDO buyContrsctDo = contractMapper.selectOne("CONTRACT_ID",contractId);
        //buyContrsctDo.setStatus(1);
        //存合同草稿合同到表
        //合同发起后修改状态为已发起
        contractMapper.updateContractByContractId("1", contractId);
        this.companySign(contractId);
//        String ssoUrl = getSsoUrl("CONTRACT_DETAIL_PAGE", contractId);
        //表示委托合同已发起
        return "OK";
    }

    public void updateContract(Long contractId){
        FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
        ContractDO contractDO = contractMapper.selectByContractId(contractId);
        //通过契约锁文档ID将文档内容转为字节流
        byte[] bytes = ContractUtil.ContractDownDone(contractId);

        fileCreateReqDTO.setContent(bytes);
        fileCreateReqDTO.setName(contractDO.getContractName()+".pdf");
        fileCreateReqDTO.setPath(null);
        //文件上传致服务器
        CommonResult<FileDTO> resultFile = fileApi.createFileNew(fileCreateReqDTO);
        FileDTO FileDTO = resultFile.getData();

        BusinessFileDO bDO = businessFileMapper.selectOne("main_id", contractId);
        bDO.setId(FileDTO.getId());
        //删除中间表business的数据
        businessFileService.deleteByMainId(contractId);

        businessFileMapper.insert(bDO);

    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public List<QYSContractVO> ContractEcho(Long carId, String type) {
        List<QYSContractVO> qysContractVOList = new ArrayList<>();
        CarInfoDO carInfo = carInfoService.getCarInfo(carId);
        CarInfoDetailsDO carInfoDetailsDO = carInfoDetailsService.getCarInfoDetailsByCarId(carId);

        if (ObjectUtil.isNull(carInfo)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        if (ObjectUtil.isNull(carInfoDetailsDO)) {
            throw exception(CAR_INFO_DETAILS_NOT_EXISTS);
        }
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();

        AdminUserDO usersDO = usersMapper.selectById(loginUser.getId());
        //AdminUserDO usersDO = usersMapper.selectById(294);
        if (ObjectUtil.isNull(usersDO)) {
            throw exception(USERS_INFO_ERROR);
        }
        UserExtDO userExtDO = userExtMapper.selectOne("USER_ID", usersDO.getId());

        DeptDO userDept = deptMapper.selectById(usersDO.getDeptId());
        if (ObjectUtil.isNull(userDept)) {
            throw exception(DEPT_INFO_ERROR);
        }
        DeptDO pDept = deptMapper.selectOne("id", userDept.getParentId());
        DeptDO platformDept = deptMapper.selectOne("parent_id", pDept.getParentId(), "attr", 1);
        if (ObjectUtil.isNull(platformDept)) {
            throw exception(DEPT_INFO_ERROR);
        }
        //平台方发票抬头信息
        InvoiceTitleDO invoiceTitleDO = invoiceTitleMapper.selectOne("dept_id", platformDept.getId());
        //这里必须要市场方去发起合同
//        QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", platformDept.getId());
        //QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", 184);
//        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        Contract contract = new Contract();
        //模版参数
        List<TemplateParam> template = new ArrayList<>();
        Long contractId = null;
        Long documentId = null;
        BusinessFileDO businessFile = new BusinessFileDO();
        String contractName = "二手车收购协议.pdf";
        QYSContractVO qysContractVO = new QYSContractVO();


        if (type.equals("1")) {
            //这里要考虑作废合同时，这里是否再写入合同  TODO
            ContractDO buyWTContract = contractMapper.getContractOneBuyType(carId, "1");
            //判断收车委托合同是否存在，不存在则存入草稿
            QYSContractVO qysContractVO1 = new QYSContractVO();

            qysContractVO1 = ContractWTSave(carInfo, carInfoDetailsDO, userDept, platformDept, usersDO, userExtDO, "1", buyWTContract);
            qysContractVOList.add(qysContractVO1);
            /*if (buyWTContract == null) {
                qysContractVO1 = ContractWTSave(carInfo, carInfoDetailsDO, userDept, platformDept, usersDO, userExtDO, "1");
                qysContractVOList.add(qysContractVO1);
            } else {

                contractId = buyWTContract.getContractId();
                qysContractVO1.setType("1");
                qysContractVO1.setContractType("1");
                qysContractVO1.setCarId(carId);
                qysContractVO1.setContractId(contractId);
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractId);
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    qysContractVO1.setUrl(listCommonResult.getData().get(0).getUrl());
                }
                qysContractVOList.add(qysContractVO1);
            }*/

            ContractDO buyContract = contractMapper.getContractOneBuyType(carId, "2");

            businessFile.setFileType("11");//收车合同类型
            qysContractVO.setType("1");
            qysContractVO.setContractName("二手车收购协议");

            //收车合同构建
            contract = this.buildBuyContract(carInfo, carInfoDetailsDO, userDept, platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //收车合同模版参数
            String buyCode = contractService.GenerateCode(2);
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, "3", buyCode);
            contractId = checkContract.getId();
            //选模版
            DocumentAddResult documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3089853213923414533L, template, "二手车收购协议").getData();
            documentId = documentAddResult.getDocumentId();

            if (buyContract != null) {
                FileRespDTO fileRespDTO = new FileRespDTO();
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", buyContract.getContractId());
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    fileRespDTO = listCommonResult.getData().get(0);
                    //将服务器上的文件和表数据删除
                    fileApi.deleteFileNew(fileRespDTO.getId());

                }
                //删除中间表business的数据
                businessFileService.deleteByMainId(contractId);
                //删除合同表数据
                contractMapper.deleteByContractId(buyContract.getContractId());


            }
            ContractDO buyContrsctDo = new ContractDO();
            buyContrsctDo.setCarId(carId);
            buyContrsctDo.setContractId(contractId);
            buyContrsctDo.setContractName("二手车收购协议");
            buyContrsctDo.setStatus(0);
            buyContrsctDo.setContractType(2);
            buyContrsctDo.setTenantId(TenantContextHolder.getTenantId());
            buyContrsctDo.setBusinessId(usersDO.getDeptId());
            buyContrsctDo.setDocumentId(documentId);
            buyContrsctDo.setCode(buyCode);
            buyContrsctDo.setFirstParty(carInfoDetailsDO.getSellerName());//甲方
            buyContrsctDo.setSecondParty(platformDept.getName());//乙方
            buyContrsctDo.setSignUpAmount(NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount())));//签约金额
            //存合同草稿合同到表
            contractMapper.insert(buyContrsctDo);

            FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
            //通过契约锁文档ID将文档内容转为字节流
            byte[] bytes = ContractUtil.ContractDown(documentId);
//                byte[] bytes = this.documentDownload(documentId);
            fileCreateReqDTO.setContent(bytes);
            fileCreateReqDTO.setName(contractName);
            fileCreateReqDTO.setPath(null);
            //文件上传致服务器
            CommonResult<FileDTO> resultFile = fileApi.createFileNew(fileCreateReqDTO);
            FileDTO FileDTO = resultFile.getData();
            if (ObjectUtil.isNull(FileDTO)) {
                throw exception(FILL_ERROR);
            }
            businessFile.setId(FileDTO.getId());
            businessFile.setTenantId(TenantContextHolder.getTenantId());
            businessFile.setMainId(contractId);
            businessFileMapper.insert(businessFile);
            qysContractVO.setUrl(FileDTO.getUrl());
            System.out.println("访问路径-----》" + FileDTO.getUrl());
            /*
            if (buyContract == null) {


            } else {

                contractId = buyContract.getContractId();

                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractId);
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    qysContractVO.setUrl(listCommonResult.getData().get(0).getUrl());
                }

            }*/

        } else if (type.equals("2")) {
            Integer sellType = carInfo.getSellType();

            ContractDO sellWTContract = contractMapper.getContractOneBuyType(carId, "3");
            QYSContractVO qysContractVO1 = new QYSContractVO();
            //判断付款方式
            if (sellType == 0) {//全款方式
                qysContractVO1 = ContractWTSave(carInfo, carInfoDetailsDO, userDept, platformDept, usersDO, userExtDO, "2", sellWTContract);
            } else {//按揭方式
                qysContractVO1 = ContractWTSave(carInfo, carInfoDetailsDO, userDept, platformDept, usersDO, userExtDO, "3", sellWTContract);
            }
            qysContractVOList.add(qysContractVO1);

            /*if (sellWTContract == null) {


            } else {

                contractId = sellWTContract.getContractId();
                qysContractVO1.setType("2");
                qysContractVO1.setContractType("1");
                qysContractVO1.setCarId(carId);
                qysContractVO1.setContractId(contractId);
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractId);
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    qysContractVO1.setUrl(listCommonResult.getData().get(0).getUrl());
                }
                qysContractVOList.add(qysContractVO1);
            }*/

            String sellCode = contractService.GenerateCode(4);
            DocumentAddResult documentAddResult = new DocumentAddResult();


            businessFile.setFileType("13");//卖车合同类型
            contractName = "二手车销售协议.pdf";
            qysContractVO.setType("2");
            qysContractVO.setContractName("二手车销售协议");
            //---------卖车合同--------------
            ContractDO sellContract = contractMapper.getContractOneBuyType(carId, "4");
            // ContractDO contractDO = contractMapper.selectOne("contract_id", contractId, "status", "0");

            contract = this.buildSellContract(carInfo, carInfoDetailsDO, userDept, platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //模版参数

            contractId = checkContract.getId();

            if (sellType == 0) {//全款方式
                template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, "4", sellCode);
                documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3089853375219569187L, template, "二手车销售协议").getData();
            } else {//按揭方式
                template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, "41", sellCode);
                documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3089853343477006794L, template, "二手车销售协议").getData();
            }

            if (sellContract != null) {
                FileRespDTO fileRespDTO = new FileRespDTO();
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", sellContract.getContractId());
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    fileRespDTO = listCommonResult.getData().get(0);
                    //将服务器上的文件和表数据删除
                    fileApi.deleteFileNew(fileRespDTO.getId());

                }
                //删除中间表business的数据
                businessFileService.deleteByMainId(contractId);
                //删除合同表数据
                contractMapper.deleteByContractId(sellContract.getContractId());


            }


            documentId = documentAddResult.getDocumentId();
            ContractDO sellContrsctDo = new ContractDO();
            sellContrsctDo.setCarId(carId);
            sellContrsctDo.setContractId(contractId);
            sellContrsctDo.setContractName("二手车销售协议");
            sellContrsctDo.setStatus(0);
            sellContrsctDo.setContractType(4);
            sellContrsctDo.setTenantId(TenantContextHolder.getTenantId());
            sellContrsctDo.setBusinessId(usersDO.getDeptId());
            sellContrsctDo.setDocumentId(documentId);
            sellContrsctDo.setCode(sellCode);
            sellContrsctDo.setFirstParty(carInfoDetailsDO.getBuyerName());//甲方
            sellContrsctDo.setSecondParty(platformDept.getName());//乙方
            sellContrsctDo.setSignUpAmount(NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount())));//签约金额
            contractMapper.insert(sellContrsctDo);

            FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
            byte[] bytes = ContractUtil.ContractDown(documentId);
//                byte[] bytes = this.documentDownload(documentId);
            fileCreateReqDTO.setContent(bytes);
            fileCreateReqDTO.setName(contractName);
            fileCreateReqDTO.setPath(null);
            CommonResult<FileDTO> resultFile = fileApi.createFileNew(fileCreateReqDTO);
            FileDTO FileDTO = resultFile.getData();
            if (ObjectUtil.isNull(FileDTO)) {
                throw exception(FILL_ERROR);
            }
            businessFile.setId(FileDTO.getId());
            businessFile.setTenantId(TenantContextHolder.getTenantId());
            businessFile.setMainId(contractId);
            businessFileMapper.insert(businessFile);

            qysContractVO.setUrl(FileDTO.getUrl());
            System.out.println("访问路径-----》" + FileDTO.getUrl());


        /*    if (sellContract == null) {



            } else {
                contractId = sellContract.getContractId();
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractId);
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    qysContractVO.setUrl(listCommonResult.getData().get(0).getUrl());
                }

            }*/
        }
        qysContractVO.setContractType("2");
        qysContractVO.setCarId(carId);
        qysContractVO.setContractId(contractId);
        //正常合同放入
        qysContractVOList.add(qysContractVO);

       /* for (QYSContractVO contractVO : qysContractVOList) {
            send(carId,type,contractVO.getContractId(),contractVO.getContractType());
        }*/


        return qysContractVOList;
    }

    @Override
    public List<QYSContractVO> FindQYSContract(Long carId) {
        List<QYSContractVO> qysContractVOList =new ArrayList<>();
        QYSContractVO qysContractVO1=new QYSContractVO();
        QYSContractVO qysContractVO=new QYSContractVO();
        //收车委托合同
       // ContractDO buyWTContractDO = contractMapper.selectOne("car_id", carId, "contract_type", 1);
        ContractDO buyWTContractDO = contractMapper.selectOne(new LambdaQueryWrapper<ContractDO>().eq(ContractDO::getCarId, carId).eq(ContractDO::getContractType, 1).eq(ContractDO::getInvalided, 0));
        BusinessFileDO byWTBusinessFile = businessFileMapper.selectOne("main_id", buyWTContractDO.getContractId());
        List<Long> contractIds = new ArrayList<>();
        contractIds.add(byWTBusinessFile.getId());
        CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
        if (listCommonResult.getData() != null) {
            qysContractVO1.setUrl(listCommonResult.getData().get(0).getUrl());
            qysContractVO1.setContractName(listCommonResult.getData().get(0).getName());
            qysContractVO1.setContractType("1");
            qysContractVOList.add(qysContractVO1);

        }
        //收车合同
       // ContractDO buyContractDO = contractMapper.selectOne("car_id", carId, "contract_type", 2);
        ContractDO buyContractDO = contractMapper.selectOne(new LambdaQueryWrapper<ContractDO>().eq(ContractDO::getCarId, carId).eq(ContractDO::getContractType, 2).eq(ContractDO::getInvalided, 0));
        BusinessFileDO buyBusinessFile = businessFileMapper.selectOne("main_id", buyContractDO.getContractId());
        List<Long> contractIdList = new ArrayList<>();
        contractIdList.add(buyBusinessFile.getId());
        CommonResult<List<FileRespDTO>> buyCommonResult = fileApi.fileList(contractIdList);
        if (buyCommonResult.getData() != null) {
            qysContractVO.setUrl(buyCommonResult.getData().get(0).getUrl());
            qysContractVO.setContractName(buyCommonResult.getData().get(0).getName());
            qysContractVO.setContractType("2");
            qysContractVOList.add(qysContractVO);

        }
        return qysContractVOList;
    }

    //回显合同时保存委托合同
    private QYSContractVO ContractWTSave(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO, DeptDO userDept, DeptDO platformDept, AdminUserDO usersDO, UserExtDO userExtDO, String type, ContractDO contractDO) {
        QYSContractVO qysContractVO = new QYSContractVO();
        //这里必须要市场方去发起合同
        QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", platformDept.getId());
        // QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", 184);
//        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        Contract contract = new Contract();
        //模版参数
        List<TemplateParam> template = new ArrayList<>();
        Long contractId = null;
        Long documentId = null;
        BusinessFileDO businessFile = new BusinessFileDO();
        String contractName = "二手车委托收购协议.pdf";
        if (type.equals("1")) {
            String buyWTCode = contractService.GenerateCode(1);
            //收车委托
            contract = this.buildBuyWTContract(userDept, platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //模版参数
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, "1", buyWTCode);
            contractId = checkContract.getId();
            log.info("=================收车委托合同ID，{}", contractId);
            //选模版
            DocumentAddResult documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3089851249420403111L, template, "二手车委托收购协议").getData();
            documentId = documentAddResult.getDocumentId();

            if (contractDO != null) {
                FileRespDTO fileRespDTO = new FileRespDTO();
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractDO.getContractId());
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    fileRespDTO = listCommonResult.getData().get(0);
                    //将服务器上的文件和表数据删除
                    fileApi.deleteFileNew(fileRespDTO.getId());

                }
                //删除中间表business的数据
                businessFileService.deleteByMainId(contractId);
                //删除合同表数据
                contractMapper.deleteByContractId(contractDO.getContractId());


            }

            businessFile.setFileType("10");//收车委托合同类型

            ContractDO buyContrsctDo = new ContractDO();
            buyContrsctDo.setCarId(carInfo.getId());
            buyContrsctDo.setContractId(contractId);
            buyContrsctDo.setContractName("二手车委托收购协议");
            buyContrsctDo.setStatus(0);
            buyContrsctDo.setContractType(1);
            buyContrsctDo.setTenantId(TenantContextHolder.getTenantId());
            buyContrsctDo.setBusinessId(usersDO.getDeptId());
            buyContrsctDo.setDocumentId(documentId);
            buyContrsctDo.setCode(buyWTCode);
            buyContrsctDo.setFirstParty(platformDept.getName());//甲方
            buyContrsctDo.setSecondParty(userDept.getName());//乙方
            buyContrsctDo.setSignUpAmount(NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount())));//签约金额
            //存合同草稿合同到表
            contractMapper.insert(buyContrsctDo);
            qysContractVO.setType("1");

        } else if (type.equals("2")) {//卖车委托直接付款
            String sellWTCode = contractService.GenerateCode(3);
            //卖车委托
            contract = this.buildSellWTContract(userDept, platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //草稿
            /*QiyuesuoCommonResult<Contract> sellResult = client.defaultDraftSend(contract);
            if (!sellResult.getCode().equals(0)) {
                throw new ServiceException(sellResult.getCode(), sellResult.getApiMsg());
            }*/
            //模版参数
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, "2", sellWTCode);
            contractId = checkContract.getId();
            //选模版
            //旧模板  3088683482805772326L
            //
            DocumentAddResult documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3089853271330792185L, template, "二手车委托代销售协议").getData();
            documentId = documentAddResult.getDocumentId();
            businessFile.setFileType("12");//卖车委托合同类型
            contractName = "二手车委托代销售协议.pdf";
            //删除旧逻辑
            if (contractDO != null) {
                FileRespDTO fileRespDTO = new FileRespDTO();
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractDO.getContractId());
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    fileRespDTO = listCommonResult.getData().get(0);
                    //将服务器上的文件和表数据删除
                    fileApi.deleteFileNew(fileRespDTO.getId());

                }
                //删除中间表business的数据
                businessFileService.deleteByMainId(contractId);
                //删除合同表数据
                contractMapper.deleteByContractId(contractDO.getContractId());


            }

            ContractDO sellWTContrsctDo = new ContractDO();
            sellWTContrsctDo.setCarId(carInfo.getId());
            sellWTContrsctDo.setContractId(contractId);
            sellWTContrsctDo.setContractName("二手车委托代销售协议");
            sellWTContrsctDo.setStatus(0);
            sellWTContrsctDo.setContractType(3);
            sellWTContrsctDo.setTenantId(TenantContextHolder.getTenantId());
            sellWTContrsctDo.setDocumentId(documentId);
            sellWTContrsctDo.setBusinessId(usersDO.getDeptId());
            sellWTContrsctDo.setCode(sellWTCode);
            sellWTContrsctDo.setFirstParty(platformDept.getName());//甲方
            sellWTContrsctDo.setSecondParty(userDept.getName());//乙方
            sellWTContrsctDo.setSignUpAmount(NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount())));//签约金额
            //存合同草稿合同到表
            contractMapper.insert(sellWTContrsctDo);
            qysContractVO.setType("2");

        } else if (type.equals("3")) {////卖车委托按揭付款
            String sellWTCode = contractService.GenerateCode(3);
            //卖车委托
            contract = this.buildSellWTContract(userDept, platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //草稿
            //模版参数
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, "21", sellWTCode);
            contractId = checkContract.getId();
            //选模版
            //旧模板  3088683482805772326L
            //
            DocumentAddResult documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3089853299281699606L, template, "二手车委托代销售协议").getData();
            documentId = documentAddResult.getDocumentId();
            if (contractDO != null) {
                FileRespDTO fileRespDTO = new FileRespDTO();
                BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractDO.getContractId());
                List<Long> contractIds = new ArrayList<>();
                contractIds.add(businessFileDO.getId());
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData() != null) {
                    fileRespDTO = listCommonResult.getData().get(0);
                    //将服务器上的文件和表数据删除
                    fileApi.deleteFileNew(fileRespDTO.getId());

                }
                //删除中间表business的数据
                businessFileService.deleteByMainId(contractId);
                //删除合同表数据
                contractMapper.deleteByContractId(contractDO.getContractId());


            }

            businessFile.setFileType("12");//卖车委托合同类型
            contractName = "二手车委托代销售协议.pdf";

            ContractDO sellWTContrsctDo = new ContractDO();
            sellWTContrsctDo.setCarId(carInfo.getId());
            sellWTContrsctDo.setContractId(contractId);
            sellWTContrsctDo.setContractName("二手车委托代销售协议");
            sellWTContrsctDo.setStatus(0);
            sellWTContrsctDo.setContractType(3);
            sellWTContrsctDo.setTenantId(TenantContextHolder.getTenantId());
            sellWTContrsctDo.setDocumentId(documentId);
            sellWTContrsctDo.setBusinessId(usersDO.getDeptId());
            sellWTContrsctDo.setCode(sellWTCode);
            sellWTContrsctDo.setFirstParty(platformDept.getName());//甲方
            sellWTContrsctDo.setSecondParty(userDept.getName());//乙方
            sellWTContrsctDo.setSignUpAmount(NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount())));//签约金额
            //存合同草稿合同到表
            contractMapper.insert(sellWTContrsctDo);
            qysContractVO.setType("2");

        }

        //将委托合同写入远程服务器以及中间表
        FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
        byte[] bytes = ContractUtil.ContractDown(documentId);
//        byte[] bytes = this.documentDownload(documentId);
        fileCreateReqDTO.setContent(bytes);
        fileCreateReqDTO.setName(contractName);
        fileCreateReqDTO.setPath(null);
        CommonResult<FileDTO> resultFile = fileApi.createFileNew(fileCreateReqDTO);
        FileDTO FileDTO = resultFile.getData();
        if (ObjectUtil.isNull(FileDTO)) {
            throw exception(FILL_ERROR);
        }
        businessFile.setId(FileDTO.getId());
        businessFile.setTenantId(TenantContextHolder.getTenantId());
        businessFile.setMainId(contractId);
        businessFileMapper.insert(businessFile);
        qysContractVO.setUrl(FileDTO.getUrl());
        qysContractVO.setCarId(carInfo.getId());
        qysContractVO.setContractId(contractId);
        qysContractVO.setContractType("1");


        return qysContractVO;
    }

    @Override
    public String getSsoUrl(String pageType, Long contractId) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        AdminUserDO adminUserDO = userMapper.selectById(loginUserId);
        QysConfigDO configDO = qysConfigMapper.selectOne("BUSINESS_ID", adminUserDO.getDeptId());
//        QysConfigDO configDO = qysConfigMapper.selectOne("BUSINESS_ID", 236L);
        if (ObjectUtil.isNull(configDO)) {
            throw exception(QYS_CONFIG_NOT_AUTH);
        }
        ContractDO contract = contractService.getByContractId(contractId);
        if (ObjectUtil.isNull(contract)) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        //加密
        // 随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        // 构建
        AES aes = SecureUtil.aes(key);
        // 加密为16进制表示
        String ticket = aes.encryptHex(loginUserId.toString());
        //存入redis
        String strKey = Byte2StrUtil.toHexString(key);
        this.redisTemplate.opsForValue().set("ticket:" + ticket, strKey);
        String ssoUrl = "https://cloudapi.qiyuesuo.cn/saas/ssogateway?ticket=%s&pageType=%s";
        if ("CONTRACT_DETAIL_PAGE".equals(pageType)) {
            //合同签署页面
            Long companyId = configDO.getCompanyId();
            String qysContractId = contract.getContractId().toString();
            //TODO 这里换成系统的页面然后配置白名单
            String signReturnUrl = "";
            ssoUrl += "&companyId=%s&contractId=%s&signReturnUrl=%s";
            ssoUrl = String.format(ssoUrl, ticket, pageType, companyId, qysContractId, signReturnUrl);
        } else {
            ssoUrl = String.format(ssoUrl, ticket, pageType);
        }
        return ssoUrl;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void companyAuth(Long userId) throws FileNotFoundException {
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(userId).getCheckedData();
        DeptRespDTO deptRespDTO = deptApi.getDept(userRespDTO.getDeptId()).getCheckedData();
        QysConfigDO configDO = qysConfigMapper.selectById(1L);
        QiyuesuoSaasClient client = qiyuesuoClientFactory.getQiyuesuoSaasClient(configDO.getId());
        List<FileRespDTO> fileList = businessFileService.getDTOByMainId(deptRespDTO.getId());
        //获取营业执照图片
        StreamFile streamFile = null;
        /*if (CollUtil.isNotEmpty(fileList)) {
            FileRespDTO fileRespDTO = fileList.get(0);
            streamFile = new StreamFile(fileRespDTO.getName(), new FileInputStream(fileRespDTO.getUrl()));
        }*/
//        {"name":"aaa","contact": "15100000000","contactType": "MOBILE"}
        Map<String, String> applicantInfo = MapUtil
                .builder("name", userRespDTO.getNickname())
                .put("contact", userRespDTO.getMobile())
                .put("contactType", "MOBILE").build();

        SaaSCompanyAuthPageResult checkedData = client.saasCompanyAuthPageUrl(deptRespDTO.getName(),
                JSON.toJSONString(applicantInfo),
                deptRespDTO.getLegalRepresentative(),
                deptRespDTO.getTaxNum(),
                streamFile).getCheckedData();
        //保存config
//        QysConfigDO newConfigDO = new QysConfigDO();
//        newConfigDO.setCode("default");
//        newConfigDO.setBusinessId(deptRespDTO.getId());
//        newConfigDO.setBusinessName(deptRespDTO.getName());
//        newConfigDO.setStatus(0);
//        qysConfigMapper.insert(newConfigDO);
        log.info("企业认证【{}】,认证地址【{}】", deptRespDTO.getName(), checkedData.getPageUrl());
        List<String> urls = ShortUrlsUtil.shortUrls(ListUtil.of(checkedData.getPageUrl()));
        Map<String, String> map = MapUtil
                .builder("title", "企业认证")
                .put("contentType", "40")
                .put("name", deptRespDTO.getName())
                .put("url", urls.get(0))
                .put("phone", userRespDTO.getMobile())
                .put("businessId", deptRespDTO.getId().toString())
                .put("type", "1").build();
        noticeService.saveNotice(map);
    }

    @GlobalTransactional
    @Transactional
    @Override
    public void userAuth(Long userId) {
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(userId).getCheckedData();
        UserExtDO userExtDO = userExtMapper.selectOne("USER_ID", userId);
        if (ObjectUtil.isNull(userRespDTO)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        DeptRespDTO deptRespDTO = deptApi.getDept(userRespDTO.getDeptId()).getCheckedData();
        QysConfigDO configDO = qysConfigMapper.selectById(1);
        QiyuesuoSaasClient client = qiyuesuoClientFactory.getQiyuesuoSaasClient(configDO.getId());
        SaaSUserAuthPageResult checkedData = client.saasUserAuthPage(userRespDTO.getMobile(),userRespDTO.getNickname(),userExtDO.getIdCard()).getCheckedData();
        String authId = checkedData.getAuthId();
        userExtDO.setAuthId(authId);
        userExtMapper.updateById(userExtDO);
        log.info("个人认证【{}】,认证地址【{}】", deptRespDTO.getName(), checkedData.getAuthUrl());
        List<String> urls = ShortUrlsUtil.shortUrls(ListUtil.of(checkedData.getAuthUrl()));
        Map<String, String> map = MapUtil
                .builder("title", "个人认证")
                .put("contentType", "42")
                .put("name", deptRespDTO.getName())
                .put("userName", userRespDTO.getNickname())
                .put("url", urls.get(0))
                .put("phone", userRespDTO.getMobile())
                .put("businessId", deptRespDTO.getId().toString())
                .put("type", "1").build();
        noticeService.saveNotice(map);
        //发送消息，做认证后结果查询
//        userAuthProducer.sendUserAuthMessage(userId, userRespDTO.getMobile(), UserAuthProducer.TWO_MINUTES);
    }

    @Override
    public void userAuthResult(UserAuthMessage message) {
        Long userId = message.getUserId();
        String contract = message.getContract();
        if (StrUtil.isBlank(contract) && ObjectUtil.isNull(userId)) {
            throw exception(QYS_CONFIG_PARAM_ERROR);
        }
        AdminUserDO adminUserDO = null;
        if (StrUtil.isBlank(contract)) {
            adminUserDO = userMapper.selectById(userId);
            contract = adminUserDO.getMobile();
        }
        QiyuesuoSaasClient qiyuesuoSaasClient = qiyuesuoClientFactory.getQiyuesuoSaasClient(1L);
        SaaSUserAuthResult checkedData = qiyuesuoSaasClient.saasUserAuthResult(contract).getCheckedData();
        if (checkedData.getRealName()) {
            //认证通过
            if (ObjectUtil.isNull(adminUserDO)) {
                adminUserDO = userMapper.selectOne("mobile", contract);
            }
            UserExtDO userExtDO = userExtMapper.selectOne("USER_ID", adminUserDO.getId());
            userExtDO.setStatus(0);
            userExtMapper.updateById(userExtDO);
        } else {
            if (ObjectUtil.equals(1, message.getCount())) {
                //认证不通过，且为第一次发送消息，发一条5分钟的的延时消息
//                userAuthProducer.sendUserAuthMessage(contract, UserAuthProducer.TEN_MINUTES);
            }
        }
    }

    @Transactional
    @Override
    public String callBackPrivilege(String signature, String timestamp, String content) throws Exception {
        log.info("[privilege]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】", signature, timestamp, content);
        //验证签名
        if (!this.verificationSignature(signature, timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        JSONObject jsonObject = JSON.parseObject(json);
        Long companyId = jsonObject.getLong("companyId");
//        QysConfigDO configDO = qysConfigMapper.selectOne("COMPANY_ID", companyId);
        QysConfigDO configDO = qysConfigMapper.selectByCompanyId(companyId);
        if (ObjectUtil.isNull(configDO)) {
            log.warn("[privilege]电子签回调出错，找不到对应公司【{}】", jsonObject);
            return "fail";
        }
        String accessToken = jsonObject.getString("accessToken");
        String accessSecret = jsonObject.getString("accessSecret");
        //判断是否存在token标志
        Boolean existSeal = StrUtil.isBlank(configDO.getAccessKey()) ? Boolean.FALSE : Boolean.TRUE;
        TenantUtils.execute(configDO.getTenantId(), () -> {
            WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID, configDO.getTenantId());
            //设置当前登录人信息，免得保存报错
            List<AdminUserRespDTO> adminUserRespDTOs = adminUserApi.getUserListByDeptIds(ListUtil.of(configDO.getBusinessId())).getCheckedData();
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(configDO.getCreator()));
            //保存回调信息
            qysCallbackService.saveDO(json,
                    QysCallBackType.COMPANY_AUTH.value(), configDO.getBusinessId());
            if (existSeal) {
                //如果存在表示授权完成，不用走下面的业务，会导致重复制作印章，发送短息
                //这里需要把企业认证的第一个用户认证状态通过
                DeptRespDTO deptRespDTO = deptApi.getDept(configDO.getBusinessId()).getCheckedData();
                List<AdminUserRespDTO> adminUserRespDTOS = adminUserApi.getUserListByDeptIds(ListUtil.toList(deptRespDTO.getId())).getCheckedData();
                if (CollUtil.isEmpty(adminUserRespDTOS)) {
                    log.error("修改用户认证状态失败，未找到唯一的用户数据");
                    return;
                }
                UserExtDO userExtDO = userExtMapper.selectOne(
                        new LambdaQueryWrapperX<UserExtDO>().eq(UserExtDO::getUserId,
                        adminUserRespDTOS.get(0).getId()));
                userExtDO.setStatus(0);
                userExtMapper.updateById(userExtDO);
                return;
            }
            configDO.setCode("default");
            configDO.setAccessKey(accessToken);
            configDO.setAccessSecret(accessSecret);
            //初始化client必须先存一次
            qysConfigMapper.updateById(configDO);
            //刷新client，里面的url这个时候获取不到yaml配置的
            this.initLocalCache();
            DeptRespDTO deptRespDTO = deptApi.getDept(configDO.getBusinessId()).getCheckedData();
            List<AdminUserRespDTO> adminUserRespDTOS = adminUserApi.getUserListByDeptIds(ListUtil.toList(deptRespDTO.getId())).getCheckedData();
            //这里先去生成一个企业公章
            QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(configDO.getId());
            Seal seal = client.defaultSealAutoCreate("公章", "").getCheckedData();
            configDO.setSealId(seal.getId());
            //保存公章
            qysConfigMapper.updateById(configDO);
            if (CollUtil.isEmpty(adminUserRespDTOS)) {
                log.error("授权印章业务时，没有获取到部门下主要员工");
                return;
            }
            log.info("企业公章生成，企业:{},公章id:{}", deptRespDTO.getName(), seal.getId());
            QiyuesuoSaasClient saasClient = qiyuesuoClientFactory.getQiyuesuoSaasClient(1L);
//            List<AdminUserRespDTO> userRespDTOS = adminUserApi.getUserListByDeptIds(ListUtil.of(configDO.getBusinessId())).getCheckedData();
            //不存在token,企业印章自动签授权
            AdminUserRespDTO userRespDTO = adminUserRespDTOS.get(0);
            //5年期限
            DateTime authDeadline = DateUtil.offset(DateUtil.date(), DateField.YEAR, 5);
            SaaSSealSignAuthUrlResult authUrlResult = saasClient.saasSealSignAuthUrl(userRespDTO.getMobile(),
                    companyId, DateUtil.formatDate(authDeadline), "授权盖章").getCheckedData();
            log.info("企业印章自动签授权,用户【{}】,授权地址【{}】", userRespDTO.getNickname(), authUrlResult.getPageUrl());
            List<String> urls1 = ShortUrlsUtil.shortUrls(ListUtil.of(authUrlResult.getPageUrl()));
            //发送短信
            Map<String, String> map1 = MapUtil
                    .builder("title", "企业印章自动签授权")
                    .put("contentType", "43")
                    .put("url", urls1.get(0))
                    .put("phone", userRespDTO.getMobile())
                    .put("businessId", configDO.getBusinessId().toString())
                    .put("type", "1").build();
            noticeService.saveNotice(map1);
        });
        return "success";
    }

    @Transactional
    @Override
    public String callBackSealSignAuth(String signature, String timestamp, String content) throws Exception {
        log.info("[SealSignAuth]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】", signature, timestamp, content);
        //验证签名
        if (!this.verificationSignature(signature, timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        JSONObject jsonObject = JSON.parseObject(json);
        Long companyId = jsonObject.getLong("companyId");
        QysConfigDO configDO = qysConfigMapper.selectByCompanyId(companyId);
        TenantUtils.execute(configDO.getTenantId(), () -> {
            WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID, configDO.getTenantId());
            String creator = configDO.getCreator();
            WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(creator));
            qysCallbackService.saveDO(json,
                    QysCallBackType.COMPANY_AUTH.value(), configDO.getBusinessId());
        });
        return null;
    }


    @Override
    public QysConfigDO getByDeptId(Long businessId) {
        return qysConfigMapper.selectOne("BUSINESS_ID", businessId);
    }

    @Override
    @Transactional
    public void companySign(Long contractId) {
        ContractDO contractDO = contractService.getByContractId(contractId);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        Long businessId = contractDO.getBusinessId();
        QysConfigDO configDO = qysConfigMapper.selectOne("BUSINESS_ID", businessId);
        if (ObjectUtil.isNull(configDO)) {
            throw exception(QYS_CONFIG_NOT_EXISTS);
        }
        String dateKeyword = ObjectUtil.equals(contractDO.getContractType(), 1) ||
                ObjectUtil.equals(contractDO.getContractType(), 3) ? "签订时间：" : null;
        //商户签章
        this.companySign(configDO, contractDO,
                ListUtil.of(this.getKeyword(contractDO.getContractType(), Boolean.FALSE)),
                dateKeyword);
        //平台方签章
        QysConfigDO platformConfigDO = qysConfigMapper.selectById(8L);
        this.companySign(platformConfigDO, contractDO,
                ListUtil.of(this.getKeyword(contractDO.getContractType(), Boolean.TRUE)),
                null);
    }

    /**
     * 签章关键字
     *
     * @param contractType 合同类型
     * @param isPlatform   是否平台方
     * @return
     */
    private String getKeyword(Integer contractType, Boolean isPlatform) {
        String keyword = "";
        if (ObjectUtil.equals(1, contractType) || ObjectUtil.equals(3, contractType)) {
            if (isPlatform) {
                keyword = "甲方（章）：";
            } else {
                keyword = "乙方（章）：";
            }
        }
        if (ObjectUtil.equals(2, contractType) || ObjectUtil.equals(4, contractType)) {
            if (isPlatform) {
                keyword = "乙方：";
            } else {
                keyword = "丙方：";
            }
        }
        return keyword;
    }

    private void companySign(QysConfigDO configDO, ContractDO contractDO, List<String> keywords,String dateKeyword) {
//      //如果是收/卖车合同，需要等待个人签署完成后，自动签署
//        if (ObjectUtil.equals(2,contractDO.getContractType()) || ObjectUtil.equals(4,contractDO.getContractType())) {
//            return;
//        }
        log.info("开始自动盖章-盖章公司：{},关键字:{}",configDO.getBusinessName(),keywords.get(0));
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(configDO.getId());
        //获取签署方公章
        Long sealId = null;
        if (ObjectUtil.isNull(configDO.getSealId())) {
            SealListResult checkedData = client.defaultSealList(configDO.getBusinessName()).getCheckedData();
            if (checkedData.getTotalCount() == 0) {
                throw exception(QYS_CONFIG_ENTERPRISE_NOT_EXISTS);
            }
            sealId = this.getSealId(checkedData.getList());
            if (ObjectUtil.isNull(sealId)) {
                throw exception(QYS_CONFIG_ENTERPRISE_NOT_EXISTS);
            }
            configDO.setSealId(sealId);
            qysConfigMapper.updateById(configDO);
        } else {
            sealId = configDO.getSealId();
        }
        client.defaultCompanysign(contractDO.getContractId(), contractDO.getDocumentId(), sealId, keywords,dateKeyword).getCheckedData();
    }

    @Override
    public byte[] documentDownload(Long documentId) {
        if (ObjectUtil.isNull(documentId)) {
            throw exception(QYS_CONFIG_DOCUMENT_DOWNLOAD_FAIL);
        }
        File tempFile = null;
        FileOutputStream fos = null;
        try {
            QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
            tempFile = File.createTempFile("temp", ".pdf");
            fos = new FileOutputStream(tempFile);
            client.defaultDocumentDownload(fos, documentId).getCheckedData();
            FileReader reader = new FileReader(tempFile);
            return reader.readBytes();
        } catch (Exception e) {
            log.error("契约锁合同下载失败", e);
            throw exception(QYS_CONFIG_DOCUMENT_DOWNLOAD_FAIL);
        } finally {
            FileUtil.del(tempFile);
            IOUtils.safeClose(fos);
        }
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void companyGyhlSign(Long contractId) {
        if (ObjectUtil.isNull(contractId)) {
            throw new IllegalArgumentException("收车委托收购协议草稿合同不能为空");
        }
        //合同类型（1收车委托合同   2收车合同  3卖车委托合同  4卖车合同）
        ContractDO contractDO = contractService.getByContractId(contractId);
        if (ObjectUtil.isNull(contractDO) || ObjectUtil.isNull(contractDO.getCarId()) || ObjectUtil.equals(contractDO.getContractType(), 1)) {
            throw new IllegalArgumentException("根据收车委托收购协议草稿合同匹配收车基本信息失败");
        }
        ContractDO contractDO1 = contractMapper.selectOne(ContractDO::getCarId, contractDO.getCarId(), ContractDO::getContractType, 2);
        if (ObjectUtil.isNull(contractDO1) || ObjectUtil.isNull(contractDO1.getContractId())) {
            throw new IllegalArgumentException("根据收车委托收购协议草稿合同获取[收车收购合同]信息失败");
        }
        CarInfoDO carInfoDO = carInfoService.getCarInfo(contractDO.getCarId());
        if (ObjectUtil.isNull(carInfoDO) || ObjectUtil.isNull(carInfoDO.getBusinessId()) || ObjectUtil.isNull(carInfoDO.getVehicleReceiptAmount())) {
            throw new IllegalArgumentException("根据收车委托收购协议草稿合同匹配收车基本信息失败");
        }
        //商户id
        Long merchantId = carInfoDO.getBusinessId();
        //收车金额（单位：元）
        BigDecimal vehicleReceiptAmount = carInfoDO.getVehicleReceiptAmount();
        //收车金额（单位：分）
        Long tranAmount = vehicleReceiptAmount.multiply(new BigDecimal(100)).longValue();
        if (tranAmount <= 0L) {
            throw new IllegalArgumentException("收车金额必须大于0,合同签章失败");
        }
        MerchantAccountDO merchantAccountDO = merchantAccountMapper.selectOne(new LambdaQueryWrapper<MerchantAccountDO>().eq(MerchantAccountDO::getMerchantId, merchantId));
        if (ObjectUtil.isNull(merchantAccountDO) || !org.springframework.util.StringUtils.hasText(merchantAccountDO.getAccountNo())) {
            throw new IllegalArgumentException("获取商户虚拟账户失败");
        }
        String accountNo = merchantAccountDO.getAccountNo();
        Integer revision = merchantAccountDO.getRevision();
        TransactionRecordReqVO transactionRecordReqVO = new TransactionRecordReqVO();
        transactionRecordReqVO.setAccountNo(accountNo);
        transactionRecordReqVO.setTranAmount(tranAmount);
        transactionRecordReqVO.setRevision(revision);
        transactionRecordReqVO.setContractNo(String.valueOf(contractDO1.getContractId()));
        // 1.保证金预占
        Boolean hasReserve = accountCashService.reserve(transactionRecordReqVO);
        if (hasReserve) {
            // 2.收车委托收购协议草稿合同后台进行自动静默签章
            this.companyGyhlSign(contractId);
        }
    }

    @Transactional
    @Override
    public String callbackCertificationPerson(String signature, String timestamp, String content) throws Exception {
        log.info("[certification-person]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】", signature, timestamp, content);
        //验证签名
        if (!this.verificationSignature(signature, timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        JSONObject jsonObject = JSON.parseObject(json);
        log.info("个人认证参数：{}", jsonObject.toJSONString());
        String authId = jsonObject.getString("authId");
        String status = jsonObject.getString("status");
        UserExtDO userExtDO = userExtMapper.selectOne("AUTH_ID", authId);
        //通过营业执照，和公司名称找到公司数据
        if (ObjectUtil.isNotNull(userExtDO)) {
            //fengin接口回调，如果要用feign 那么这里必须卸载回调里，不然报错没传参数 tenant-id
            TenantUtils.execute(userExtDO.getTenantId(), () -> {
                WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID, userExtDO.getTenantId());
                //设置当前登录人信息，免得保存报错
                WebFrameworkUtils.setLoginUserId(WebFrameworkUtils.getRequest(), Long.valueOf(userExtDO.getUserId()));
                userExtDO.setStatus(0);
                userExtMapper.updateById(userExtDO);
                AdminUserRespDTO userRespDTO = adminUserApi.getUser(userExtDO.getUserId()).getCheckedData();
//                AdminUserDO userDO = userMapper.selectById(userExtDO.getUserId());
//                userDO.setStatus(0);
//                userMapper.updateById(userDO);
                QysConfigDO configDO = qysConfigMapper.selectOne(QysConfigDO::getBusinessId, userRespDTO.getDeptId());
                QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(configDO.getId());
                //添加员工
                client.defaultEmployeeCreate(userRespDTO.getNickname(), userRespDTO.getMobile()).getCheckedData();
                //授权印章角色
                client.defaultRoleManage(ListUtil.of(userRespDTO.getMobile())).getCheckedData();
            });
        } else {
            log.warn("个人认证失败，找不到数据，authId：{}", authId);
            return "fail";
        }
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public Map addAccount(AddAccountDTO reqVO) {
        Map map = adminUserApi.addAccount(reqVO);
        String type =String.valueOf(map.get("type"));
        if("1".equals(type)){
            Long userId = Long.valueOf(String.valueOf(map.get("userId")));
            userAuth(userId);
            map.put("success","0");
        }
        return map;
    }


    private Long getSealId(List<Seal> list) {
        Long seaId = null;
        for (Seal seal : list) {
            if (ObjectUtil.equals("ENTERPRISE", seal.getSealType())) {
                //公章
                seaId = seal.getId();
                break;
            }
        }
        return seaId;
    }

    //收车委托合同
    private Contract buildBuyWTContract(DeptDO userDept, DeptDO platformDept) {
        Contract draftContract = new Contract();
        AdminUserDO platformUserDO = userMapper.selectFAUser(platformDept.getId());
        AdminUserDO pUserDO = userMapper.selectFAUser(userDept.getId());
        draftContract.setSubject("两方-二手车-收车委托合同");
        //注意：契约锁会按照list的下标来确认签署顺序

        // 平台方
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformDept.getName());
        platformSignatory.setReceiver(new User(platformUserDO.getMobile(), "MOBILE"));
//        platformSignatory.setSerialNo(1);
        draftContract.addSignatory(platformSignatory);

        //签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("COMPANY");
        persoanlSignatory.setTenantName(userDept.getName());
        persoanlSignatory.setReceiver(new User(pUserDO.getMobile(), "MOBILE"));
//        persoanlSignatory.setSerialNo(2);
        draftContract.addSignatory(persoanlSignatory);


        //模板参数
        draftContract.setCategory(new Category(3078145859615985671L));//业务分类配置`w
        //创建人
//        draftContract.setCreator(new User("17396202169","MOBILE"));
        draftContract.setSend(false); //发起合同
        log.info("发起收车委托合同草稿参数：{}", JSON.toJSONString(draftContract));
        return draftContract;
    }

    //卖车委托合同
    private Contract buildSellWTContract(DeptDO userDept, DeptDO platformDept) {
        Contract draftContract = new Contract();
        AdminUserDO platformUserDO = userMapper.selectFAUser(platformDept.getId());
        AdminUserDO pUserDO = userMapper.selectFAUser(userDept.getId());
        draftContract.setSubject("两方-二手车-卖车委托合同");
        // 设置合同接收方
        // 甲方平台
//        Signatory platformSignatory = new Signatory();
//        platformSignatory.setTenantType("PERSONAL");
//        platformSignatory.setTenantName("罗聪");
//        platformSignatory.setReceiver(new User("17396202169", "MOBILE"));
//        draftContract.addSignatory(platformSignatory);
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformDept.getName());
        platformSignatory.setReceiver(new User(platformUserDO.getMobile(), "MOBILE"));
        draftContract.addSignatory(platformSignatory);

        //乙方个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("COMPANY");
        persoanlSignatory.setTenantName(userDept.getName());
        persoanlSignatory.setReceiver(new User(pUserDO.getMobile(), "MOBILE"));
//        persoanlSignatory.setTenantName(userDept.getName());
//        persoanlSignatory.setReceiver(new User( userDept.getPhone(), "MOBILE"));
        draftContract.addSignatory(persoanlSignatory);
        //模板参数
        draftContract.setCategory(new Category(3078145859615985671L));//业务分类配置`
        //创建人
//        draftContract.setCreator(new User("17396202169","MOBILE"));
        draftContract.setSend(false); //发起合同
        log.info("发起卖车委托合同草稿参数：{}", draftContract);
        return draftContract;
    }


    //收车合同
    private Contract buildBuyContract(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO, DeptDO userDept, DeptDO platformDept) {
        Contract draftContract = new Contract();
        AdminUserDO platformUserDO = userMapper.selectFAUser(platformDept.getId());
        AdminUserDO pUserDO = userMapper.selectFAUser(userDept.getId());
        draftContract.setSubject("三方-二手车-收车合同");

        //个人
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName(carInfoDetailsDO.getSellerName());
        persoanlSignatory.setReceiver(new User(carInfoDetailsDO.getSellerTel(), "MOBILE"));
//        persoanlSignatory.setSerialNo(1);
        draftContract.addSignatory(persoanlSignatory);
        //丙方
        Signatory initiator2 = new Signatory();
        initiator2.setTenantType("COMPANY");
        initiator2.setTenantName(userDept.getName());
        initiator2.setReceiver(new User(pUserDO.getMobile(), "MOBILE"));
//        initiator2.setSerialNo(2);
        draftContract.addSignatory(initiator2);
        // 乙方平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformDept.getName());
        platformSignatory.setReceiver(new User(platformUserDO.getMobile(), "MOBILE"));
//        platformSignatory.setSerialNo(3);
        draftContract.addSignatory(platformSignatory);
        //模板参数
        //收车
        draftContract.setCategory(new Category(3083237961123238073L));//业务分类配置
        //创建人
//        draftContract.setCreator(new User("17396202169","MOBILE"));
        draftContract.setSend(false); //发起合同
        return draftContract;
    }

    //卖车合同
    private Contract buildSellContract(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO, DeptDO userDept, DeptDO platformDept) {
        AdminUserDO platformUserDO = userMapper.selectFAUser(platformDept.getId());
        AdminUserDO pUserDO = userMapper.selectFAUser(userDept.getId());
        Contract draftContract = new Contract();
        draftContract.setSubject("三方-二手车-卖车合同");
        // 设置合同接收方
        // 甲方个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName(carInfoDetailsDO.getBuyerName());
        persoanlSignatory.setReceiver(new User(carInfoDetailsDO.getBuyerTel(), "MOBILE"));
        persoanlSignatory.setSerialNo(1);
        draftContract.addSignatory(persoanlSignatory);
        // 乙方平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformDept.getName());
        platformSignatory.setReceiver(new User(platformUserDO.getMobile(), "MOBILE"));
        platformSignatory.setSerialNo(2);
        draftContract.addSignatory(platformSignatory);
        //丙方
        Signatory initiator2 = new Signatory();
        initiator2.setTenantType("COMPANY");
        initiator2.setTenantName(userDept.getName());
        initiator2.setReceiver(new User(pUserDO.getMobile(), "MOBILE"));
        initiator2.setSerialNo(3);
        draftContract.addSignatory(initiator2);

        //模板参数
        //卖车
        draftContract.setCategory(new Category(3083237961123238073L));//业务分类配置
        //创建人
//        draftContract.setCreator(new User("17396202169","MOBILE"));
        draftContract.setSend(false); //发起合同
        log.info("发起卖车合同草稿参数：{}", draftContract);
        return draftContract;
    }

    private String getFeesAndCommitments(String vehicle, String type) {
        String result = "";
        if (vehicle != null) {
            if (type.equals("1")) {
                //A-轿车200元/天,B-商务车400元/天,C-豪车1000元/天,D-乙方（平台）无需承担
                if (vehicle.equals("vehicleA")) {
                    result = "轿车200元/天";
                } else if (vehicle.equals("vehicleB")) {
                    result = "商务车400元/天";
                } else if (vehicle.equals("vehicleC")) {
                    result = "豪车1000元/天";
                } else if (vehicle.equals("vehicleD")) {
                    result = "乙方（平台）无需承担";
                }
            } else if (type.equals("2")) {
                //A-销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人),B-销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人,C-甲方（买方）无需承担
                if (vehicle.equals("transferA")) {
                    result = "销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人";
                } else if (vehicle.equals("transferB")) {
                    result = "销售车辆首次交易过户费(乙方（平台）过户甲方（买方）指定过户人";
                } else if (vehicle.equals("transferC")) {
                    result = "甲方（买方）无需承担";
                }
            } else if (type.equals("3")) {
                //A-依据本协议第二条车辆价款的5%支付车辆折损费用,B-依据本协议第二条车辆价款的5%支付车辆折损费用
                if (vehicle.equals("lossA")) {
                    result = "依据本协议第二条车辆价款的5%支付车辆折损费用";
                } else if (vehicle.equals("lossB")) {
                    result = "依据本协议第二条车辆价款的5%支付车辆折损费用";
                }
            } else if (type.equals("4")) {
                //A-全车检测费用,B-乙方（平台）无需承担
                if (vehicle.equals("testingA")) {
                    result = "全车检测费用";
                } else if (vehicle.equals("testingB")) {
                    result = "乙方（平台）无需承担";
                }
            }
        }
        return result;
    }

    private String getProceduresAndSpareParts(ProceduresAndSpareParts parts) {
        String result = "";
        if (parts != null) {
            if (parts.getDrivingLicense() != null && parts.getDrivingLicense()) {
                result += "行驶证(正/副)本、";
            }
            if (parts.getCarInvoice() != null && parts.getCarInvoice()) {
                result += "购车发票、";
            }
            if (parts.getRegistrationCertificate() != null && parts.getRegistrationCertificate()) {
                result += "机动车登记证、";
            }
            if (parts.getPurchaseTax() != null && parts.getPurchaseTax()) {
                result += "购置税完税凭证、";
            }
            if (parts.getSpareTire() != null && parts.getSpareTire()) {
                result += "备胎、";
            }
            if (parts.getCarShipTax() != null && parts.getCarShipTax()) {
                result += "车船使用税完税凭证、";
            }
            if (parts.getHeavyTrafficInsurance() != null && parts.getHeavyTrafficInsurance()) {
                result += "交强险保单、";
            }
            if (parts.getCommercialInsurance() != null && parts.getCommercialInsurance()) {
                result += "商业险保单、";
            }
            if (parts.getJack() != null && parts.getJack()) {
                result += "千斤顶、";
            }
            if (parts.getSpecification() != null && parts.getSpecification()) {
                result += "说明书、";
            }
            if (parts.getVehicleKey() != null&&parts.getVehicleKey()!=0) {
                result += "钥匙数量:" + parts.getVehicleKey() + "组、";
            }
            if (StringUtils.isNotEmpty(parts.getAccidentVehicle())) {
                result += parts.getAccidentVehicle()+"、";
            }
            result=result.substring(0,result.length()-1);
        }
        return result;
    }

    private String getProceduresAndSpareSell(ProceduresAndSpareSell parts) {
        String result = "";
        if (parts != null) {
            if (parts.getDrivingLicense() != null && parts.getDrivingLicense()) {
                result += "行驶证(正/副)本、";
            }
            if (parts.getCarInvoice() != null && parts.getCarInvoice()) {
                result += "购车发票、";
            }
            if (parts.getRegistrationCertificate() != null && parts.getRegistrationCertificate()) {
                result += "机动车登记证、";
            }
            if (parts.getPurchaseTax() != null && parts.getPurchaseTax()) {
                result += "购置税完税凭证、";
            }
            if (parts.getSpareTire() != null && parts.getSpareTire()) {
                result += "备胎、";
            }
            if (parts.getCarShipTax() != null && parts.getCarShipTax()) {
                result += "车船使用税完税凭证、";
            }
            if (parts.getHeavyTrafficInsurance() != null && parts.getHeavyTrafficInsurance()) {
                result += "交强险保单、";
            }
            if (parts.getCommercialInsurance() != null && parts.getCommercialInsurance()) {
                result += "商业险保单、";
            }
            if (parts.getJack() != null && parts.getJack()) {
                result += "千斤顶、";
            }
            if (parts.getSpecification() != null && parts.getSpecification()) {
                result += "说明书、";
            }
            if (parts.getVehicleKey() != null&&parts.getVehicleKey()!=0) {
                result += "钥匙数量:" + parts.getVehicleKey() + "组、";
            }
            if (StringUtils.isNotEmpty(parts.getAccidentVehicle())) {
                result += parts.getAccidentVehicle()+"、";
            }
            result=result.substring(0,result.length()-1);
        }
        return result;
    }

    private String dateFormal(String dateString) {
        String result = "";
        if (StringUtils.isNotEmpty(dateString) && dateString != "null") {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年M月d日");
            result = sdf1.format(date);
        }
        return result;
    }


    //模板参数
    private List<TemplateParam> buildTemplateParam(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO, DeptDO userDept, DeptDO platformDept, String type, String code) {
        InvoiceTitleDO invoiceTitleDO = invoiceTitleMapper.selectOne("dept_id", platformDept.getId());
        AdminUserDO platformUserDO = userMapper.selectFAUser(platformDept.getId());
        AdminUserDO pUserDO = userMapper.selectFAUser(userDept.getId());
        List<TemplateParam> params = new ArrayList<>();
        VehicleProblem vehicleProblem = carInfoDetailsDO.getVehicleProblem();
        //车辆手续及备件（收车）
        ProceduresAndSpareParts proceduresAndSpareParts = carInfoDetailsDO.getProceduresAndSpareParts();
        // String Parts = getProceduresAndSpareParts(proceduresAndSpareParts);
        //车辆手续及备件（卖车）
        ProceduresAndSpareSell proceduresAndSpareSell = carInfoDetailsDO.getProceduresAndSpareSell();
        //String Sells = getProceduresAndSpareSell(proceduresAndSpareSell);

        String vehicle = "";
        //车辆过户
        String transfer = "";
        //车辆折损
        String loss = "";
        //第三方检测
        String testing = "";
        FeesAndCommitments feesAndCommitments = carInfoDetailsDO.getFeesAndCommitments();
        if (feesAndCommitments != null) {
            //车辆使用租金
            vehicle = getFeesAndCommitments(feesAndCommitments.getVehicle(), "1");
            //车辆过户
            transfer = getFeesAndCommitments(feesAndCommitments.getTransfer(), "2");
            //车辆折损
            loss = getFeesAndCommitments(feesAndCommitments.getLoss(), "3");
            //第三方检测
            testing = getFeesAndCommitments(feesAndCommitments.getTesting(), "4");
        }
        String conditionA = "确认";
        String conditionB = "非事故车";
        String conditionC = "非泡水车";
        String conditionD = "非火烧车";
        if (vehicleProblem != null) {
            if (vehicleProblem.getConditionA() != null && vehicleProblem.getConditionA()) {
                conditionA = "不确认";
            }
            if (vehicleProblem.getConditionB() != null && vehicleProblem.getConditionB()) {
                conditionA = "是事故车";
            }
            if (vehicleProblem.getConditionC() != null && vehicleProblem.getConditionC()) {
                conditionA = "是泡水车";
            }
            if (vehicleProblem.getConditionD() != null && vehicleProblem.getConditionD()) {
                conditionA = "是火烧车";
            }
        }
        if (carInfo != null && carInfoDetailsDO != null && userDept != null && platformDept != null & invoiceTitleDO != null & platformUserDO != null & pUserDO != null) {
            //收车委托合同
            if ("1".equals(type)) {
                params.add(new TemplateParam("合同编号", code));
                params.add(new TemplateParam("受托人", platformDept.getName()));
                params.add(new TemplateParam("甲方营业执照号", platformDept.getTaxNum()));
                params.add(new TemplateParam("甲方法定代表人", platformDept.getLegalRepresentative()));
                params.add(new TemplateParam("甲方联系电话", platformUserDO.getMobile()));
                //params.add(new TemplateParam("甲方联系电话", "18942820000"));
                params.add(new TemplateParam("甲方联系地址", platformDept.getAddress()));

                params.add(new TemplateParam("委托人", userDept.getName()));
                params.add(new TemplateParam("乙方营业执照号", userDept.getTaxNum()));
                params.add(new TemplateParam("乙方法定代表人", userDept.getLegalRepresentative()));
                params.add(new TemplateParam("乙方联系电话", pUserDO.getMobile()));
                params.add(new TemplateParam("乙方联系地址", userDept.getAddress()));

                params.add(new TemplateParam("车辆牌号", carInfo.getPlateNum()));
                params.add(new TemplateParam("车辆类型", carInfo.getCarType()));
                params.add(new TemplateParam("厂牌、型号", carInfo.getModel()));
                params.add(new TemplateParam("颜色", carInfoDetailsDO.getColour()));
                // params.add(new TemplateParam("初次登记日期", carInfoDetailsDO.getFirstRegistDate()));
                params.add(new TemplateParam("初次登记日期", dateFormal(carInfoDetailsDO.getFirstRegistDate())));
                params.add(new TemplateParam("登记证号", carInfoDetailsDO.getCertificateNo()));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                //params.add(new TemplateParam("使用年限", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getScrapDate()))));
                //params.add(new TemplateParam("年检签证有效期", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getAnnualInspectionDate()))));
                params.add(new TemplateParam("年检签证有效期", dateFormal(String.valueOf(carInfo.getAnnualInspectionDate()))));
                params.add(new TemplateParam("保险险种", carInfo.getInsurance()));
                //params.add(new TemplateParam("保险有效期", carInfo.getInsuranceEndData()));
                params.add(new TemplateParam("保险有效期", dateFormal(carInfo.getInsuranceEndData())));
                params.add(new TemplateParam("收车金额大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("收车金额小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                params.add(new TemplateParam("甲方收款银行", invoiceTitleDO.getBank()));
                params.add(new TemplateParam("甲方收款账号", invoiceTitleDO.getBankAccount()));
                params.add(new TemplateParam("甲方收款名称", invoiceTitleDO.getCompanyName()));
                //params.add(new TemplateParam("甲方收款银行", "工商"));
                //params.add(new TemplateParam("甲方收款账号", "6228 4804 8172 3886 810"));
            } else if ("2".equals(type)) {
                //卖车委托合同-直接付款
                params.add(new TemplateParam("合同编号", code));

                params.add(new TemplateParam("受托人", platformDept.getName()));
                params.add(new TemplateParam("甲方营业执照号", platformDept.getTaxNum()));
                params.add(new TemplateParam("甲方法定代表人", platformDept.getLegalRepresentative()));
                params.add(new TemplateParam("甲方联系电话", platformUserDO.getMobile()));
                params.add(new TemplateParam("甲方联系地址", platformDept.getAddress()));

                params.add(new TemplateParam("委托人", userDept.getName()));
                params.add(new TemplateParam("乙方营业执照号", userDept.getTaxNum()));
                params.add(new TemplateParam("乙方法定代表人", userDept.getLegalRepresentative()));
                params.add(new TemplateParam("乙方联系电话", pUserDO.getMobile()));
                params.add(new TemplateParam("乙方联系地址", userDept.getAddress()));

                params.add(new TemplateParam("车辆牌号", carInfo.getPlateNum()));
                params.add(new TemplateParam("车辆类型", carInfo.getCarType()));
                params.add(new TemplateParam("厂牌、型号", carInfo.getModel()));
                params.add(new TemplateParam("颜色", carInfoDetailsDO.getColour()));
                params.add(new TemplateParam("初次登记日期", dateFormal(carInfoDetailsDO.getFirstRegistDate())));
                params.add(new TemplateParam("登记证号", carInfoDetailsDO.getCertificateNo()));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                // params.add(new TemplateParam("使用年限",NullReplaceUtil.nullReplace(String.valueOf(carInfo.getScrapDate())));
                //params.add(new TemplateParam("年检签证有效期", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getAnnualInspectionDate()))));
                params.add(new TemplateParam("年检签证有效期", dateFormal(String.valueOf(carInfo.getAnnualInspectionDate()))));
                params.add(new TemplateParam("保险险种", carInfo.getInsurance()));
                params.add(new TemplateParam("保险有效期", dateFormal(carInfo.getInsuranceEndData())));
                params.add(new TemplateParam("卖车金额小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount()))));
                params.add(new TemplateParam("卖车金额大写", UppercaseUtil.convert(carInfo.getSellAmount())));
                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getSellAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount()))));
                params.add(new TemplateParam("定金大写", UppercaseUtil.convert(carInfo.getDeposit())));
                params.add(new TemplateParam("定金小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getDeposit()))));
                params.add(new TemplateParam("尾款大写", UppercaseUtil.convert(carInfo.getBalancePayment())));
                params.add(new TemplateParam("尾款小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getBalancePayment()))));
            } else if ("21".equals(type)) {
                //卖车委托合同-按揭付款
                params.add(new TemplateParam("合同编号", code));

                params.add(new TemplateParam("受托人", platformDept.getName()));
                params.add(new TemplateParam("甲方营业执照号", platformDept.getTaxNum()));
                params.add(new TemplateParam("甲方法定代表人", platformDept.getLegalRepresentative()));
                params.add(new TemplateParam("甲方联系电话", platformUserDO.getMobile()));
                params.add(new TemplateParam("甲方联系地址", platformDept.getAddress()));

                params.add(new TemplateParam("委托人", userDept.getName()));
                params.add(new TemplateParam("乙方营业执照号", userDept.getTaxNum()));
                params.add(new TemplateParam("乙方法定代表人", userDept.getLegalRepresentative()));
                params.add(new TemplateParam("乙方联系电话", pUserDO.getMobile()));
                params.add(new TemplateParam("乙方联系地址", userDept.getAddress()));

                params.add(new TemplateParam("车辆牌号", carInfo.getPlateNum()));
                params.add(new TemplateParam("车辆类型", carInfo.getCarType()));
                params.add(new TemplateParam("厂牌、型号", carInfo.getModel()));
                params.add(new TemplateParam("颜色", carInfoDetailsDO.getColour()));
                params.add(new TemplateParam("初次登记日期", dateFormal(carInfoDetailsDO.getFirstRegistDate())));
                params.add(new TemplateParam("登记证号", carInfoDetailsDO.getCertificateNo()));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                // params.add(new TemplateParam("使用年限",NullReplaceUtil.nullReplace(String.valueOf(carInfo.getScrapDate()))));
                params.add(new TemplateParam("年检签证有效期", dateFormal(String.valueOf(carInfo.getAnnualInspectionDate()))));
                params.add(new TemplateParam("保险险种", carInfo.getInsurance()));
                params.add(new TemplateParam("保险有效期", dateFormal(carInfo.getInsuranceEndData())));
                params.add(new TemplateParam("卖车金额小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount()))));
                params.add(new TemplateParam("卖车金额大写", UppercaseUtil.convert(carInfo.getSellAmount())));

                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getSellAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount()))));
                params.add(new TemplateParam("定金大写", UppercaseUtil.convert(carInfo.getDeposit())));
                params.add(new TemplateParam("定金小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getDeposit()))));
//            params.add(new TemplateParam("尾款大写", "0"));
//            params.add(new TemplateParam("尾款小写", "0"));
            } else if ("3".equals(type)) {
                //s收车合同
                params.add(new TemplateParam("合同编号", code));
                params.add(new TemplateParam("卖方受托人", carInfoDetailsDO.getSellerName()));
                params.add(new TemplateParam("甲方身份证号", carInfoDetailsDO.getSellerIdCard()));
                params.add(new TemplateParam("甲方法定代表人", carInfoDetailsDO.getSellerName()));
                params.add(new TemplateParam("甲方联系电话", carInfoDetailsDO.getSellerTel()));
                params.add(new TemplateParam("甲方联系地址", carInfoDetailsDO.getSellerAdder()));

                params.add(new TemplateParam("平台受托人", platformDept.getName()));
                params.add(new TemplateParam("乙方营业执照号", platformDept.getTaxNum()));
                params.add(new TemplateParam("乙方法定代表人", platformDept.getLegalRepresentative()));
                params.add(new TemplateParam("乙方联系电话", platformUserDO.getMobile()));
                params.add(new TemplateParam("乙方联系地址", platformDept.getAddress()));

                params.add(new TemplateParam("车商公司名称", userDept.getName()));
                params.add(new TemplateParam("丙方营业执照号", userDept.getTaxNum()));
                params.add(new TemplateParam("丙方法定代表人", userDept.getLegalRepresentative()));
                params.add(new TemplateParam("丙方联系电话", pUserDO.getMobile()));
                params.add(new TemplateParam("丙方联系地址", userDept.getAddress()));

                params.add(new TemplateParam("首次登记日期", dateFormal(carInfoDetailsDO.getFirstRegistDate())));
                //params.add(new TemplateParam("首次登记日期", carInfoDetailsDO.getFirstRegistDate()));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                params.add(new TemplateParam("车辆手续及备件", getProceduresAndSpareParts(proceduresAndSpareParts)));

                params.add(new TemplateParam("车辆总价大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("车辆总价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                //  params.add(new TemplateParam("付款方式", "全款"));
            } else if ("4".equals(type)) {
                //卖车合同-直接付款
                params.add(new TemplateParam("合同编号", code));

                params.add(new TemplateParam("买方受托人", carInfoDetailsDO.getBuyerName()));
                params.add(new TemplateParam("甲方身份证号", carInfoDetailsDO.getBuyerIdCard()));
                params.add(new TemplateParam("甲方法定代表人", carInfoDetailsDO.getBuyerName()));
                params.add(new TemplateParam("甲方联系电话", carInfoDetailsDO.getBuyerTel()));
                params.add(new TemplateParam("甲方联系地址", carInfoDetailsDO.getBuyerAdder()));


                params.add(new TemplateParam("平台受托人", platformDept.getName()));
                params.add(new TemplateParam("乙方营业执照号", platformDept.getTaxNum()));
                params.add(new TemplateParam("乙方法定代表人", platformDept.getLegalRepresentative()));
                params.add(new TemplateParam("乙方联系电话", platformUserDO.getMobile()));
                params.add(new TemplateParam("乙方联系地址", platformDept.getAddress()));

                params.add(new TemplateParam("车商公司名称", userDept.getName()));
                params.add(new TemplateParam("丙方营业执照号", userDept.getTaxNum()));
                params.add(new TemplateParam("丙方法定代表人", userDept.getLegalRepresentative()));
                params.add(new TemplateParam("丙方联系电话", pUserDO.getMobile()));
                params.add(new TemplateParam("丙方联系地址", userDept.getAddress()));


                params.add(new TemplateParam("首次登记日期", dateFormal(carInfoDetailsDO.getFirstRegistDate())));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                params.add(new TemplateParam("车辆手续及备件", getProceduresAndSpareSell(proceduresAndSpareSell)));

                params.add(new TemplateParam("确认", conditionA));
                params.add(new TemplateParam("是非事故车", conditionB));
                params.add(new TemplateParam("是非泡水车", conditionC));
                params.add(new TemplateParam("是非火烧车", conditionD));
                if (StringUtils.isEmpty(carInfo.getRemarks())) {
                    params.add(new TemplateParam("其他约定", "无"));
                } else {
                    params.add(new TemplateParam("其他约定", carInfo.getRemarks()));
                }
                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getSellAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount()))));
                params.add(new TemplateParam("定金大写", UppercaseUtil.convert(carInfo.getDeposit())));
                params.add(new TemplateParam("定金小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getDeposit()))));
                params.add(new TemplateParam("剩余车款大写", UppercaseUtil.convert(carInfo.getBalancePayment())));
                params.add(new TemplateParam("剩余车款小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getBalancePayment()))));

                params.add(new TemplateParam("车辆使用租金", vehicle));
                params.add(new TemplateParam("交易过户费", transfer));
                params.add(new TemplateParam("车辆折损费用", loss));
                params.add(new TemplateParam("第三方检测费用", testing));
                params.add(new TemplateParam("其他", carInfo.getOther()));

            } else if ("41".equals(type)) {
                //卖车合同-按揭付款
                params.add(new TemplateParam("合同编号", code));

                params.add(new TemplateParam("买方受托人", carInfoDetailsDO.getBuyerName()));
                params.add(new TemplateParam("甲方身份证号", carInfoDetailsDO.getBuyerIdCard()));
                params.add(new TemplateParam("甲方法定代表人", carInfoDetailsDO.getBuyerName()));
                params.add(new TemplateParam("甲方联系电话", carInfoDetailsDO.getBuyerTel()));
                params.add(new TemplateParam("甲方联系地址", carInfoDetailsDO.getBuyerAdder()));


                params.add(new TemplateParam("平台受托人", platformDept.getName()));
                params.add(new TemplateParam("乙方营业执照号", platformDept.getTaxNum()));
                params.add(new TemplateParam("乙方法定代表人", platformDept.getLegalRepresentative()));
                params.add(new TemplateParam("乙方联系电话", platformUserDO.getMobile()));
                params.add(new TemplateParam("乙方联系地址", platformDept.getAddress()));

                params.add(new TemplateParam("车商公司名称", userDept.getName()));
                params.add(new TemplateParam("丙方营业执照号", userDept.getTaxNum()));
                params.add(new TemplateParam("丙方法定代表人", userDept.getLegalRepresentative()));
                params.add(new TemplateParam("丙方联系电话", pUserDO.getMobile()));
                params.add(new TemplateParam("丙方联系地址", userDept.getAddress()));


                params.add(new TemplateParam("首次登记日期", dateFormal(carInfoDetailsDO.getFirstRegistDate())));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                params.add(new TemplateParam("车辆手续及备件", getProceduresAndSpareSell(proceduresAndSpareSell)));
                params.add(new TemplateParam("确认", conditionA));
                params.add(new TemplateParam("是非事故车", conditionB));
                params.add(new TemplateParam("是非泡水车", conditionC));
                params.add(new TemplateParam("是非火烧车", conditionD));
                if (StringUtils.isEmpty(carInfo.getRemarks())) {
                    params.add(new TemplateParam("其他约定", "无"));
                } else {
                    params.add(new TemplateParam("其他约定", carInfo.getRemarks()));
                }
                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getSellAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getSellAmount()))));
                params.add(new TemplateParam("定金大写", UppercaseUtil.convert(carInfo.getDeposit())));
                params.add(new TemplateParam("定金小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getDeposit()))));
                // params.add(new TemplateParam("定金大写", "0"));
                //params.add(new TemplateParam("定金小写", "0"));
                params.add(new TemplateParam("车辆使用租金", vehicle));
                params.add(new TemplateParam("交易过户费", transfer));
                params.add(new TemplateParam("车辆折损费用", loss));
                params.add(new TemplateParam("第三方检测费用", testing));
                params.add(new TemplateParam("其他", carInfo.getOther()));


            }
        }
        return params;
    }

    private Boolean verificationSignature(String signature, String timestamp) {
        String md5 = MD5.toMD5(timestamp + SECRET);
        if (!StrUtil.equals(signature, md5)) {
            log.error("验证签名不一致，md5【{}】,signature【{}】", md5, signature);
            System.out.println("签名不一致");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private String decryptMessage(String content) throws Exception {
        return CryptUtils.aesDerypt(content, SECRET);
    }


    /*
     * 契约锁子公司员工新增接口
     * name 员工姓名（必传）
     * contact 联系方式（必传）
     * contactType联系方式，默认 MOBILE（必传）
     * number 员工编号
     * tenantName 子公司名称 （若为空，默认为平台方主公司）
     *
     * */

    public void addQysUser(String name, String contact, String contactType, String number, String tenantName) {
        //临时sdk，需切换到工厂类中
        SdkClient sdkClient = new SdkClient("https://openapi.qiyuesuo.cn", "q4xKsNcFI8", "qKPK101VGyLsnSqFoLzSCu3JGiMAVO");

        // 添加员工
        User user = new User(name, contact, contactType);
        EmployeeCreateRequest request = new EmployeeCreateRequest(user, number);
        request.setTenantName(tenantName);
        String response = sdkClient.service(request);
        SdkResponse<Employee> responseObj = JSONUtils.toQysResponse(response, Employee.class);
        if (responseObj.getCode() == 0) {
            Employee result = responseObj.getResult();
            log.info("创建员工，员工ID：{}", result.getId());
        } else {
            log.info("创建员工失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
        }


    }

    /*
     * 契约锁子公司员工移除接口
     * name 员工姓名（必传）
     * contact 联系方式（必传）
     * contactType联系方式，默认 MOBILE（必传）
     *
     * tenantName 子公司名称 （子公司名称，若移除的该员工位于子公司中，则需要传递该值，默认为平台方主公司）
     *
     * */
    public void removeQysUser(String name, String contact, String contactType, String tenantName) {
        //临时sdk，需切换到工厂类中
        SdkClient sdkClient = new SdkClient("https://openapi.qiyuesuo.cn", "q4xKsNcFI8", "qKPK101VGyLsnSqFoLzSCu3JGiMAVO");
        // 移除员工
        User user = new User(name, contact, contactType);
        EmployeeRemoveRequest request = new EmployeeRemoveRequest(user);
        request.setTenantName(tenantName);
        String response = sdkClient.service(request);
        SdkResponse<Employee> responseObj = JSONUtils.toQysResponse(response, Employee.class);
        if (responseObj.getCode() == 0) {
            log.info("移除员工成功");
        } else {
            log.info("移除失败，错误码:{}，错误信息:{}", responseObj.getCode(), responseObj.getMessage());
        }

    }

    public static void main(String[] args) {

        /*try {
            String md5 = MD5.toMD5("1681907332818" + SECRET);
            if (StrUtil.equals("03fb7492aa65106be0b6ab14a22cc781",md5)) {
                System.out.println("666");
            }else {
                System.out.println("666");
            }
            //解密消息
            String content = "dn9AJvP1tgxz+hrH7KLE5E1xQowx3dRnZn/vVOyFpG7qUZ7bUieKk1404aFK5NTR5o4SX44AMc2TiCZTOxTmDEjbAwjC7UEMMJjSxlKuCsSubptFjiCI0CrPeSjO66zNzIdsQMJCrkJ+IjjTFh9+91tH0miMVPrDj3tuv/MRtxBaO4ENI5HPCDn7cJnlM+dVO4kCcvogcDquj1aTy/B3aGHgygpkbKOMJa7D40kw7m+9XNixvwot3afXisOU1ta2woWO7XYqhGBNZ9RZ39WDr7wKAbkhvK7uk+B2elPHz78=";
            String s = CryptUtils.aesDerypt(content, SECRET);
//            {"companyId":"3086283524031066663","requestId":"09dfb74d-63f1-4f9d-957b-c2840ba6c806","companyName":"西瓜科技公司","legalPerson":"罗风","actionEvent":7,"status":1,"registerNo":"916100007869672125638"}
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        String content = "test中文";

// 随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
// 构建
        AES aes = SecureUtil.aes(key);


// 加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        System.out.println(encryptHex);
// 解密为字符串
//        Byte2StrUtil.toByteArray("");
        String s = Byte2StrUtil.toHexString(key);
        byte[] key1 = Byte2StrUtil.toByteArray(s);
        AES aes1 = SecureUtil.aes(key1);
        String decryptStr = aes1.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);
    }

}
