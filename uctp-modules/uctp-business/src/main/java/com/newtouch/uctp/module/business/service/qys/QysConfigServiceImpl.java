package com.newtouch.uctp.module.business.service.qys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.common.pojo.PageResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.framework.tenant.core.util.TenantUtils;
import com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils;
import com.newtouch.uctp.module.business.controller.app.contact.QYSContractVO;
import com.newtouch.uctp.module.business.controller.app.qys.dto.CompanyAuthDTO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigCreateReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigPageReqVO;
import com.newtouch.uctp.module.business.controller.app.qys.vo.QysConfigUpdateReqVO;
import com.newtouch.uctp.module.business.convert.qys.QysConfigConvert;
import com.newtouch.uctp.module.business.dal.dataobject.BusinessFileDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDO;
import com.newtouch.uctp.module.business.dal.dataobject.CarInfoDetailsDO;
import com.newtouch.uctp.module.business.dal.dataobject.ContractDO;
import com.newtouch.uctp.module.business.dal.dataobject.dept.DeptDO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysCallbackDO;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.dal.dataobject.user.AdminUserDO;
import com.newtouch.uctp.module.business.dal.dataobject.user.UserExtDO;
import com.newtouch.uctp.module.business.dal.mysql.BusinessFileMapper;
import com.newtouch.uctp.module.business.dal.mysql.ContractMapper;
import com.newtouch.uctp.module.business.dal.mysql.UsersMapper;
import com.newtouch.uctp.module.business.dal.mysql.dept.DeptMapper;
import com.newtouch.uctp.module.business.dal.mysql.qys.QysCallbackMapper;
import com.newtouch.uctp.module.business.dal.mysql.qys.QysConfigMapper;
import com.newtouch.uctp.module.business.dal.mysql.user.UserExtMapper;
import com.newtouch.uctp.module.business.dal.mysql.user.UserMapper;
import com.newtouch.uctp.module.business.enums.QysCallBackType;
import com.newtouch.uctp.module.business.service.*;
import com.newtouch.uctp.module.business.util.Byte2StrUtil;
import com.newtouch.uctp.module.business.util.ContractUtil;
import com.newtouch.uctp.module.business.util.UppercaseUtil;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.infra.api.file.dto.FileCreateReqDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileDTO;
import com.newtouch.uctp.module.infra.api.file.dto.FileRespDTO;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dept.dto.DeptRespDTO;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;
import com.newtouch.uctp.module.system.api.user.dto.AdminUserRespDTO;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.response.*;
import com.qiyuesuo.sdk.v2.utils.CryptUtils;
import com.qiyuesuo.sdk.v2.utils.MD5;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils.HEADER_TENANT_ID;
import static com.newtouch.uctp.framework.web.core.util.WebFrameworkUtils.REQUEST_ATTRIBUTE_LOGIN_USER_ID;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;
import static com.newtouch.uctp.module.business.enums.QysConstants.SECRET;

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
    private RedisTemplate<String,String> redisTemplate;
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
   /* @Resource
    private FileMapper fileMapper;*/
    @Resource
    private BusinessFileMapper businessFileMapper;
   /* @Resource
    private FileService fileService;*/
    @Resource
    private BusinessFileService businessFileService;
    @Resource
    private NoticeService noticeService;

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

    private void createOrUpdateQiyuesuoClient(List<QysConfigDO> configDOS){
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
        log.info("[certification]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
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
//        DeptDO deptDO = deptMapper.selectOne("name", companyName, "tax_num", registerNo);
        DeptDO deptDO = deptMapper.findByNameAndTaxNum(companyName,registerNo);
        if (ObjectUtil.isNotNull(deptDO)) {
//            this.configurationSystemVariable(deptDO);
            //fengin接口回调，如果要用feign 那么这里必须卸载回调里，不然报错没传参数 tenant-id
            TenantUtils.execute(deptDO.getTenantId(), () -> {
                WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID,deptDO.getTenantId());
                //设置当前登录人信息，免得保存报错
                List<AdminUserRespDTO> adminUserRespDTOs = adminUserApi.getUserListByDeptIds(ListUtil.of(deptDO.getId())).getCheckedData();
                if (CollUtil.isEmpty(adminUserRespDTOs)) {
                    //是在找不到
                    WebFrameworkUtils.getRequest().setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID,"admin");
                }else {
                    WebFrameworkUtils.getRequest().setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID,adminUserRespDTOs.get(0).getId());
                }
                //如果回调数据为认证成功，保存公司id
                if ("1".equals(status) && StrUtil.isNotBlank(companyId)) {
                    QysConfigDO configDO = qysConfigMapper.selectOne("COMPANY_ID", companyId);
                    if (ObjectUtil.isNull(configDO)) {
                        configDO = new QysConfigDO();
                    }
                    configDO.setCompanyId(Long.valueOf(companyId));
                    configDO.setBusinessId(deptDO.getId());
                    configDO.setBusinessName(deptDO.getName());
                    qysConfigMapper.insert(configDO);
                    deptDO.setAuth(Integer.valueOf(status));
                    deptMapper.updateById(deptDO);
                    //保存回调信息
                    qysCallbackService.saveDO(json,
                            QysCallBackType.COMPANY_AUTH.value(),deptDO.getId());
                    //继续做企业授权
                    List<AdminUserRespDTO> checkedData = adminUserApi.getUserListByDeptIds(ListUtil.toList(deptDO.getId())).getCheckedData();
                    if (CollUtil.isNotEmpty(checkedData)) {
                        //此时只会存在一条当前部门下的用户数据
                        AdminUserRespDTO userRespDTO = checkedData.get(0);
//                    this.privilegeUrl(userRespDTO.getId());
                        QiyuesuoSaasClient client = qiyuesuoClientFactory.getQiyuesuoSaasClient(1L);
                        SaaSPrivilegeUrlResult privilegeUrlResult = client.saasPrivilegeUrl(configDO.getCompanyId(), userRespDTO.getMobile()).getCheckedData();
                        log.info("企业授权【{}】,授权地址【{}】",deptDO.getName(),privilegeUrlResult.getPageUrl());
                        Map<String, String> map = MapUtil
                                .builder("title", "企业认证")
                                .put("contentType", "40")
                                .put("name", deptDO.getName())
                                .put("url", privilegeUrlResult.getPageUrl())
                                .put("phone", userRespDTO.getMobile())
                                .put("businessId", deptDO.getId().toString())
                                .put("type", "1").build();
                        noticeService.saveNotice(map);
                    }else {
                        log.error("[certification]根据返回的公司名称未查询到数据,companyName:{},tax_num:{}",companyName,registerNo);
                    }
                }
            });
        }else {
            log.error("[certification]根据返回的公司名称未查询到数据,companyName:{},tax_num:{}",companyName,registerNo);
        }
        return "success";
    }

    private void configurationSystemVariable(DeptDO deptDO){
        //设置租户
        TenantUtils.execute(deptDO.getTenantId(), () -> {
            WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID,deptDO.getTenantId());
            //设置当前登录人信息，免得保存报错
            List<AdminUserRespDTO> adminUserRespDTOs = adminUserApi.getUserListByDeptIds(ListUtil.of(deptDO.getId())).getCheckedData();
            if (CollUtil.isEmpty(adminUserRespDTOs)) {
                //是在找不到
                WebFrameworkUtils.getRequest().setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID,"admin");
            }else {
                WebFrameworkUtils.getRequest().setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID,adminUserRespDTOs.get(0).getId());
            }
        });


    }

    @Override
    public String status(String signature, String timestamp, String content) throws Exception {
        log.info("[status]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        //TODO 处理业务

        return "success";
    }

    @Override
    public Map<String, Object> verification(String ticket) throws Exception {
        log.info("[verification]电子签CAS校验：ticket【{}】",ticket);
        //拿到 aes 密钥
//        redisTemplate.opsForValue().set("ticket:"+ticket,"密钥");
        String aesKeyStr = redisTemplate.opsForValue().get("ticket:"+ticket);
        byte[] aesKey = Byte2StrUtil.toByteArray(aesKeyStr);
        AES aes = SecureUtil.aes(aesKey);
        //解密 获取到userId
        String userId = aes.decryptStr(ticket, CharsetUtil.CHARSET_UTF_8);
        //查询到用户数据并返回
        AdminUserDO adminUser = userMapper.selectById(userId);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("contract", adminUser.getMobile());
        userMap.put("name", adminUser.getNickname());
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("code", 0);
        retMap.put("message", "SUCCESS");
        retMap.put("result", userMap);
        return retMap;
    }

    @Override
    public String login(String signature, String timestamp, String content) throws Exception {
        log.info("[login]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        //TODO 处理业务
        return "success";
    }

    @Override
    public void test(Long id,Integer type) throws Exception {
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(1650772257324167170L);
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
        }else if (type.equals(3)) {
            Object checkedData = client.defaultCompanysign(3088393275632066703L).getCheckedData();
            System.out.println(checkedData);
        }else if (type.equals(4)) {
            SaaSPrivilegeUrlResult checkedData = saasClient.saasPrivilegeUrl(3088322841008022468L, "17380123816").getCheckedData();
            System.out.println(checkedData);
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(Long carId,String type,Long contractId,String contractType)  {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();

        AdminUserDO usersDO = usersMapper.selectById(loginUser.getId());
        // AdminUserDO usersDO = usersMapper.selectById(211);
       /* CarInfoDO carInfo = carInfoService.getCarInfo(carId);
        CarInfoDetailsDO carInfoDetailsDO =carInfoDetailsService.getCarInfoDetailsByCarId(carId);

        if (ObjectUtil.isNull(carInfo)) {
            throw exception(CAR_INFO_NOT_EXISTS);
        }
        if (ObjectUtil.isNull(carInfoDetailsDO)) {
            throw exception(CAR_INFO_DETAILS_NOT_EXISTS);
        }
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();

        AdminUserDO usersDO = usersMapper.selectById(loginUser.getId());
        if (ObjectUtil.isNull(usersDO)) {
            throw exception(USERS_INFO_ERROR);
        }
        UserExtDO userExtDO= userExtMapper.selectOne("USER_ID",usersDO.getId());
        DeptDO userDept=deptMapper.selectById(usersDO.getDeptId());
        if (ObjectUtil.isNull(userDept)) {
            throw exception(DEPT_INFO_ERROR);
        }
        DeptDO pDept=deptMapper.selectOne("id",userDept.getParentId());
        DeptDO platformDept=deptMapper.selectOne("parent_id",pDept.getParentId(),"attr",1);
        if (ObjectUtil.isNull(platformDept)) {
            throw exception(DEPT_INFO_ERROR);
        }

        BusinessFileDO businessFileDO = businessFileMapper.selectOne("main_id", contractId);
        */
        QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", usersDO.getDeptId());
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        //合同发起
        client.defaultContractSend(contractId);

        //保存在那里需要确认
    }

    @Override
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
        //AdminUserDO usersDO = usersMapper.selectById(211);
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
        QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", usersDO.getDeptId());
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        Contract contract = new Contract();
        //模版参数
        List<TemplateParam> template = new ArrayList<>();
        Long contractId = null;
        Long documentId = null;
        BusinessFileDO businessFile = new BusinessFileDO();
        String contractName = "二手车收购协议.pdf";
        QYSContractVO qysContractVO = new QYSContractVO();
        //这里要考虑作废合同时，这里是否再写入合同  TODO
        ContractDO buyWTContract = contractMapper.getContractOneBuyType( carId,"1");
        //判断收车委托合同是否存在，不存在则存入草稿
        if (buyWTContract==null){
            QYSContractVO qysContractVO1 = ContractWTSave(carInfo, carInfoDetailsDO, userDept, platformDept, usersDO, userExtDO, "1");
            qysContractVOList.add(qysContractVO1);
        }
        ContractDO sellWTContract = contractMapper.getContractOneBuyType(carId,"3");
        //判断卖车委托合同是否存在，不存在则存入草稿
        if (sellWTContract==null){
            QYSContractVO qysContractVO1 = ContractWTSave(carInfo, carInfoDetailsDO, userDept, platformDept, usersDO, userExtDO, "2");
            qysContractVOList.add(qysContractVO1);
        }
        if (type.equals("1")) {

            ContractDO buyContract = contractMapper.getContractOneBuyType( carId,"2");
            //收车合同构建
            contract = this.buildBuyContract(carInfo, carInfoDetailsDO, userDept, platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //收车合同模版参数
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, userExtDO, "3");
            contractId = checkContract.getId();
            //选模版
            DocumentAddResult documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3086576123044233944L, template, "二手车收购协议").getData();
            documentId = documentAddResult.getDocumentId();
            businessFile.setFileType("11");//收车合同类型
            if (buyContract==null) {

                    ContractDO buyContrsctDo = new ContractDO();
                    buyContrsctDo.setCarId(carId);
                    buyContrsctDo.setContractId(contractId);
                    buyContrsctDo.setContractName("二手车收购协议");
                    buyContrsctDo.setStatus(0);
                    buyContrsctDo.setContractType(2);
                    buyContrsctDo.setTenantId(TenantContextHolder.getTenantId());
                    buyContrsctDo.setBusinessId(usersDO.getDeptId());
                    //存合同草稿合同到表
                    contractMapper.insert(buyContrsctDo);

                try {

                    FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
                    //通过契约锁文档ID将文档内容转为字节流
                    byte[] bytes = ContractUtil.ContractDown(documentId);
                    fileCreateReqDTO.setContent(bytes);
                    fileCreateReqDTO.setName(contractName);
                    fileCreateReqDTO.setPath(null);
                    //文件上传致服务器
                    CommonResult<FileDTO> resultFile = fileApi.createFile(fileCreateReqDTO);
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }else {

                contractId=buyContract.getContractId();
                List<Long> contractIds=new ArrayList<>();
                contractIds.add(contractId);
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData()!=null) {
                    qysContractVO.setUrl(listCommonResult.getData().get(0).getUrl());
                }

            }

        }
        else if (type.equals("2")) {
            //卖车
            contract = this.buildSellContract(carInfo, carInfoDetailsDO, userDept, platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //模版参数
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, userExtDO, "4");
            contractId = checkContract.getId();
            //选模版
            DocumentAddResult documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3086576170024633076L, template, "二手车代销售协议").getData();
            documentId = documentAddResult.getDocumentId();
            businessFile.setFileType("13");//卖车合同类型
            contractName = "二手车销售协议.pdf";
            ContractDO sellContract = contractMapper.getContractOneBuyType(carId,"4");
           // ContractDO contractDO = contractMapper.selectOne("contract_id", contractId, "status", "0");
            if (sellContract == null) {
                ContractDO buyContrsctDo = new ContractDO();
                buyContrsctDo.setCarId(carId);
                buyContrsctDo.setContractId(contractId);
                buyContrsctDo.setContractName("二手车销售协议");
                buyContrsctDo.setStatus(0);
                buyContrsctDo.setContractType(4);
                buyContrsctDo.setTenantId(TenantContextHolder.getTenantId());
                buyContrsctDo.setBusinessId(usersDO.getDeptId());
                contractMapper.insert(buyContrsctDo);

                try {

                    FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
                    byte[] bytes = ContractUtil.ContractDown(documentId);
                    fileCreateReqDTO.setContent(bytes);
                    fileCreateReqDTO.setName(contractName);
                    fileCreateReqDTO.setPath(null);
                    CommonResult<FileDTO> resultFile = fileApi.createFile(fileCreateReqDTO);
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                contractId=sellContract.getContractId();
                List<Long> contractIds=new ArrayList<>();
                contractIds.add(contractId);
                CommonResult<List<FileRespDTO>> listCommonResult = fileApi.fileList(contractIds);
                if (listCommonResult.getData()!=null) {
                    qysContractVO.setUrl(listCommonResult.getData().get(0).getUrl());
                }

                }
            }


        qysContractVO.setContractType("2");
        qysContractVO.setCarId(carId);
        qysContractVO.setContractId(contractId);
        //正常合同放入
        qysContractVOList.add(qysContractVO);

        //send(carId,type,qysContractVOList.get(0).getContractId(),"1");

        return qysContractVOList;
    }

    //回显合同时保存委托合同
    private QYSContractVO ContractWTSave(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO,DeptDO userDept,DeptDO platformDept,AdminUserDO usersDO,UserExtDO userExtDO,String type){
        QYSContractVO qysContractVO=new QYSContractVO();
        QysConfigDO qysConfigDO = qysConfigMapper.selectOne("BUSINESS_ID", usersDO.getDeptId());
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        Contract contract =  new Contract();
        //模版参数
        List<TemplateParam> template = new ArrayList<>();
        Long contractId = null;
        Long documentId = null;
        BusinessFileDO businessFile =new BusinessFileDO();
        String contractName="二手车委托收购协议.pdf";
        if (type.equals("1")){
            //收车委托
            contract =  this.buildBuyWTContract(userDept,platformDept);
            Contract checkContract = client.defaultDraftSend(contract).getCheckedData();
            //模版参数
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, userExtDO, "1");
            contractId = checkContract.getId();
            //选模版
            DocumentAddResult documentAddResult = client.defaultDocumentAddByTemplate(contractId, 3086496292898148540L, template, "二手车委托收购协议").getData();
            documentId = documentAddResult.getDocumentId();
            businessFile.setFileType("10");//收车委托合同类型

            ContractDO buyContrsctDo = new ContractDO();
            buyContrsctDo.setCarId(carInfo.getId());
            buyContrsctDo.setContractId(contractId);
            buyContrsctDo.setContractName("二手车委托收购协议");
            buyContrsctDo.setStatus(0);
            buyContrsctDo.setContractType(1);
            buyContrsctDo.setTenantId(TenantContextHolder.getTenantId());
            buyContrsctDo.setBusinessId(usersDO.getDeptId());
            //存合同草稿合同到表
            contractMapper.insert(buyContrsctDo);



        }else if(type.equals("2")){
            //卖车委托
            contract =  this.buildSellWTContract(userDept,platformDept);
            //草稿
            QiyuesuoCommonResult<Contract> sellResult = client.defaultDraftSend(contract);
            if (!sellResult.getCode().equals(0)) {
                throw new ServiceException(sellResult.getCode(), sellResult.getApiMsg());
            }
            //模版参数
            template = buildTemplateParam(carInfo, carInfoDetailsDO, userDept, platformDept, userExtDO, "4");
            contractId = sellResult.getData().getId();
            //选模版
            DocumentAddResult documentAddResult =  client.defaultDocumentAddByTemplate(contractId,3086576170024633076L,template,"二手车委托代销售协议").getData();
            documentId = documentAddResult.getDocumentId();
            businessFile.setFileType("12");//卖车委托合同类型
            contractName="二手车委托代销售协议.pdf";

            ContractDO buyContrsctDo = new ContractDO();
            buyContrsctDo.setCarId(carInfo.getId());
            buyContrsctDo.setContractId(contractId);
            buyContrsctDo.setContractName("二手车委托代销售协议");
            buyContrsctDo.setStatus(0);
            buyContrsctDo.setContractType(3);
            buyContrsctDo.setTenantId(TenantContextHolder.getTenantId());
            buyContrsctDo.setBusinessId(usersDO.getDeptId());
            //存合同草稿合同到表
            contractMapper.insert(buyContrsctDo);


        }

        //将委托合同写入远程服务器以及中间表
        try {

            FileCreateReqDTO fileCreateReqDTO=new FileCreateReqDTO();
            byte[] bytes = ContractUtil.ContractDown(documentId);
            fileCreateReqDTO.setContent(bytes);
            fileCreateReqDTO.setName(contractName);
            fileCreateReqDTO.setPath(null);
            CommonResult<FileDTO> resultFile = fileApi.createFile(fileCreateReqDTO);
            FileDTO FileDTO = resultFile.getData();
            if (ObjectUtil.isNull(FileDTO)) {
                throw exception(FILL_ERROR);
            }
            businessFile.setId(FileDTO.getId());
            businessFile.setTenantId(TenantContextHolder.getTenantId());
            businessFile.setMainId(contractId);
            businessFileMapper.insert(businessFile);
            qysContractVO.setUrl(FileDTO.getUrl());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        qysContractVO.setCarId(carInfo.getId());
        qysContractVO.setContractId(contractId);
        qysContractVO.setContractType("1");

        return qysContractVO;
    }

    @Override
    public String getSsoUrl(String pageType,Long contractId) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Long loginUserId = loginUser.getId();
        //加密
        // 随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        // 构建
        AES aes = SecureUtil.aes(key);
        // 加密为16进制表示
        String ticket = aes.encryptHex(loginUserId.toString());
        String ssoUrl = "https://cloudapi.qiyuesuo.cn/saas/ssogateway?ticket=%s&pageType=%s";
        if ("CONTRACT_DETAIL_PAGE".equals(pageType)) {
            //合同签署页面
            List<QysCallbackDO> callbackDOS = qysCallbackService.getByMainIdAndType(loginUser.getDeptId(),QysCallBackType.COMPANY_AUTH.value());
             ContractDO contract =contractService.getById(contractId);
            if (CollUtil.isNotEmpty(callbackDOS)) {
                QysCallbackDO callbackDO = callbackDOS.get(0);
                CompanyAuthDTO companyAuthDTO = JSON.parseObject(callbackDO.getContent(), CompanyAuthDTO.class);
                String companyId = companyAuthDTO.getCompanyId();
                String qysContractId = contract.getContractId().toString();
                //TODO 这里换成系统的页面然后配置白名单
                String signReturnUrl = "www.baidu.com";
                ssoUrl += "&companyId=%s&contractId&signReturnUrl=%s";
                ssoUrl = String.format(ssoUrl, ticket, pageType,companyId,qysContractId,signReturnUrl);
            }
        }else {
            ssoUrl = String.format(ssoUrl, ticket, pageType);
        }
        return ssoUrl;
    }

    @GlobalTransactional
    @Transactional
    @Override
    public void companyAuth(Long userId) throws FileNotFoundException {
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(userId).getCheckedData();
        DeptRespDTO deptRespDTO = deptApi.getDept(userRespDTO.getDeptId()).getCheckedData();
        QysConfigDO configDO = qysConfigMapper.selectById(1);
        QiyuesuoSaasClient client = qiyuesuoClientFactory.getQiyuesuoSaasClient(configDO.getId());
        List<FileRespDTO> fileList = businessFileService.getDTOByMainId(deptRespDTO.getId());
        //获取营业执照图片
        StreamFile streamFile = null;
        if (CollUtil.isNotEmpty(fileList)) {
            FileRespDTO fileRespDTO = fileList.get(0);
            streamFile = new StreamFile(fileRespDTO.getName(), new FileInputStream(fileRespDTO.getUrl()));
        }
//        {"name":"aaa","contact": "15100000000","contactType": "MOBILE"}
        Map<String, String> applicantInfo = MapUtil
                .builder("name", userRespDTO.getNickname())
                .put("contact", userRespDTO.getMobile())
                .put("contactType", "MOBILE").build();

        QiyuesuoCommonResult<SaaSCompanyAuthPageResult> result = client.saasCompanyAuthPageUrl(deptRespDTO.getName(),
                JSON.toJSONString(applicantInfo),
                deptRespDTO.getLegalRepresentative(),
                deptRespDTO.getTaxNum(),
                streamFile);
        SaaSCompanyAuthPageResult checkedData = result.getCheckedData();
        log.info("企业认证【{}】,认证地址【{}】",deptRespDTO.getName(),checkedData.getPageUrl());
        Map<String, String> map = MapUtil
                .builder("title", "企业认证")
                .put("contentType", "40")
                .put("name", deptRespDTO.getName())
                .put("url", checkedData.getPageUrl())
                .put("phone", userRespDTO.getMobile())
                .put("businessId", deptRespDTO.getId().toString())
                .put("type", "1").build();
        noticeService.saveNotice(map);
    }

    @GlobalTransactional
    @Transactional
    @Override
    public void userAuth(Long userId){
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(userId).getCheckedData();
        DeptRespDTO deptRespDTO = deptApi.getDept(userRespDTO.getDeptId()).getCheckedData();
        QysConfigDO configDO = qysConfigMapper.selectById(1);
        QiyuesuoSaasClient client = qiyuesuoClientFactory.getQiyuesuoSaasClient(configDO.getId());
        SaaSUserAuthPageResult checkedData = client.saasUserAuthPage(userRespDTO.getMobile()).getCheckedData();
        log.info("个人认证【{}】,认证地址【{}】",deptRespDTO.getName(),checkedData.getAuthUrl());
        Map<String, String> map = MapUtil
                .builder("title", "企业认证")
                .put("contentType", "42")
                .put("name", deptRespDTO.getName())
                .put("userName", userRespDTO.getNickname())
                .put("url", checkedData.getAuthUrl())
                .put("phone", userRespDTO.getMobile())
                .put("businessId", deptRespDTO.getId().toString())
                .put("type", "1").build();
        noticeService.saveNotice(map);
    }
    @Transactional
    @Override
    public String callBackPrivilege(String signature, String timestamp, String content) throws Exception {
        log.info("[privilege]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
            return "fail";
        }
        //解密消息
        String json = this.decryptMessage(content);
        JSONObject jsonObject = JSON.parseObject(json);
        Long companyId = jsonObject.getLong("companyId");
//        QysConfigDO configDO = qysConfigMapper.selectOne("COMPANY_ID", companyId);
        QysConfigDO configDO = qysConfigMapper.selectByCompanyId(companyId);
        if (ObjectUtil.isNull(configDO)) {
            log.warn("[privilege]电子签回调出错，找不到对应公司【{}】",jsonObject);
            return "fail";
        }
        String accessToken = jsonObject.getString("accessToken");
        String accessSecret = jsonObject.getString("accessSecret");

        TenantUtils.execute(configDO.getTenantId(), () -> {
            WebFrameworkUtils.getRequest().setAttribute(HEADER_TENANT_ID,configDO.getTenantId());
            //设置当前登录人信息，免得保存报错
            List<AdminUserRespDTO> adminUserRespDTOs = adminUserApi.getUserListByDeptIds(ListUtil.of(configDO.getBusinessId())).getCheckedData();
            if (CollUtil.isEmpty(adminUserRespDTOs)) {
                //是在找不到
                WebFrameworkUtils.getRequest().setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID,"admin");
            }else {
                WebFrameworkUtils.getRequest().setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID,adminUserRespDTOs.get(0).getId());
            }
            //保存回调信息
            qysCallbackService.saveDO(json,
                    QysCallBackType.COMPANY_AUTH.value(),configDO.getBusinessId());
            configDO.setCode("default");
//            configDO.setServerUrl("https://openapi.qiyuesuo.cn");
            configDO.setAccessKey(accessToken);
            configDO.setAccessSecret(accessSecret);
            qysConfigMapper.updateById(configDO);
            //获取企业印章自动签授权链接
            QiyuesuoSaasClient saasClient = qiyuesuoClientFactory.getQiyuesuoSaasClient(1L);
            List<AdminUserRespDTO> userRespDTOS = adminUserApi.getUserListByDeptIds(ListUtil.of(configDO.getBusinessId())).getCheckedData();
            if (CollUtil.isNotEmpty(userRespDTOS)) {
                AdminUserRespDTO userRespDTO = userRespDTOS.get(0);
                DateTime authDeadline = DateUtil.offset(DateUtil.date(), DateField.MONTH, 12);
                SaaSSealSignAuthUrlResult authUrlResult = saasClient.saasSealSignAuthUrl(userRespDTO.getMobile(),
                        companyId, DateUtil.formatDate(authDeadline), "授权盖章").getCheckedData();
                log.info("企业印章自动签授权,用户【{}】,授权地址【{}】",userRespDTO.getNickname(),authUrlResult.getPageUrl());
                //发送短信
                Map<String, String> map = MapUtil
                        .builder("title", "企业印章自动签授权")
                        .put("contentType", "43")
                        .put("url", authUrlResult.getPageUrl())
                        .put("phone", userRespDTO.getMobile())
                        .put("businessId", configDO.getBusinessId().toString())
                        .put("type", "1").build();
                noticeService.saveNotice(map);
            }
        });
        return "success";
    }

    @Transactional
    @Override
    public String callBackSealSignAuth(String signature, String timestamp, String content) throws Exception {
        log.info("[SealSignAuth]电子签回调参数：signature【{}】,timestamp【{}】,content【{}】",signature,timestamp,content);
        //验证签名
        if (!this.verificationSignature(signature,timestamp)) {
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


    //收车委托合同
    private Contract buildBuyWTContract(DeptDO userDept,DeptDO platformDept) {
        Contract draftContract = new Contract();
        draftContract.setSubject("三方-二手车-666");
        // 设置合同接收方
        // 甲方平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
       // platformSignatory.setTenantName(platformDept.getName());
        platformSignatory.setTenantName("广东光耀汽车公司");
        platformSignatory.setReceiver(new User("17380123816", "MOBILE"));
        draftContract.addSignatory(platformSignatory);

        //乙方个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("COMPANY");
        persoanlSignatory.setTenantName("平头哥二手车");
        persoanlSignatory.setReceiver(new User("17311271898", "MOBILE"));
//        persoanlSignatory.setTenantName(userDept.getName());
//        persoanlSignatory.setReceiver(new User( userDept.getPhone(), "MOBILE"));
        draftContract.addSignatory(persoanlSignatory);
        //模板参数
        //draftContract.setCategory(new Category(3083237961123238073L));//业务分类配置`
        draftContract.setCategory(new Category(3078145859615985671L));//业务分类配置`
        draftContract.setSend(false); //发起合同
        return draftContract;
    }

    //卖车委托合同
    private Contract buildSellWTContract(DeptDO userDept,DeptDO platformDept) {
        Contract draftContract = new Contract();
        draftContract.setSubject("三方-二手车");
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
        platformSignatory.setReceiver(new User(platformDept.getPhone(), "MOBILE"));
        draftContract.addSignatory(platformSignatory);

        //乙方个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName(userDept.getName());
        persoanlSignatory.setReceiver(new User(userDept.getPhone(), "MOBILE"));
//        persoanlSignatory.setTenantName(userDept.getName());
//        persoanlSignatory.setReceiver(new User( userDept.getPhone(), "MOBILE"));
        draftContract.addSignatory(persoanlSignatory);
        //模板参数
        draftContract.setCategory(new Category(3078145859615985671L));//业务分类配置`
        draftContract.setSend(true); //发起合同
        return draftContract;
    }


    //收车合同
    private Contract buildBuyContract(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO,DeptDO userDept,DeptDO platformDept) {
        Contract draftContract = new Contract();
        draftContract.setSubject("三方-二手车");
        // 设置合同接收方
        // 甲方个人签署方
//        Signatory persoanlSignatory = new Signatory();
//        persoanlSignatory.setTenantType("PERSONAL");
//        persoanlSignatory.setTenantName(carInfoDetailsDO.getSellerName());
//        persoanlSignatory.setReceiver(new User(carInfoDetailsDO.getSellerTel(), "MOBILE"));
//        draftContract.addSignatory(persoanlSignatory);
//        // 乙方平台
//        Signatory platformSignatory = new Signatory();
//        platformSignatory.setTenantType("COMPANY");
//        platformSignatory.setTenantName(platformDept.getName());
//        platformSignatory.setReceiver(new User( platformDept.getPhone(), "MOBILE"));
//        draftContract.addSignatory(platformSignatory);
//        //丙方
//        Signatory initiator2 = new Signatory();
//        initiator2.setTenantType("COMPANY");
//        initiator2.setTenantName(userDept.getName());
//        initiator2.setReceiver(new User(userDept.getPhone(), "MOBILE"));
//        draftContract.addSignatory(initiator2);

        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        // platformSignatory.setTenantName(platformDept.getName());
        platformSignatory.setTenantName("平头哥二手车");
        platformSignatory.setReceiver(new User("17311271898", "MOBILE"));
        draftContract.addSignatory(platformSignatory);

        //乙方个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("COMPANY");
        persoanlSignatory.setTenantName("广东光耀汽车公司");
        persoanlSignatory.setReceiver(new User("17380123816", "MOBILE"));
//        persoanlSignatory.setTenantName(userDept.getName());
//        persoanlSignatory.setReceiver(new User( userDept.getPhone(), "MOBILE"));
        draftContract.addSignatory(persoanlSignatory);
        Signatory initiator2 = new Signatory();
        initiator2.setTenantType("PERSONAL");
        initiator2.setTenantName(carInfoDetailsDO.getBuyerName());
        initiator2.setReceiver(new User(carInfoDetailsDO.getBuyerTel(), "MOBILE"));
       // initiator2.setTenantName("阿卡丽");
        //initiator2.setReceiver(new User("17396202169", "MOBILE"));
        draftContract.addSignatory(initiator2);

        //模板参数
        //收车

        draftContract.setCategory(new Category(3083237961123238073L));//业务分类配置
        draftContract.setSend(true); //发起合同
        return draftContract;
    }

    //卖车合同
    private Contract buildSellContract(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO,DeptDO userDept,DeptDO platformDept) {
        Contract draftContract = new Contract();
        draftContract.setSubject("三方-二手车");
        // 设置合同接收方
        // 甲方个人签署方
        Signatory persoanlSignatory = new Signatory();
        persoanlSignatory.setTenantType("PERSONAL");
        persoanlSignatory.setTenantName(carInfoDetailsDO.getBuyerName());
        persoanlSignatory.setReceiver(new User(carInfoDetailsDO.getBuyerTel(), "MOBILE"));
        draftContract.addSignatory(persoanlSignatory);
        // 乙方平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformDept.getName());
        platformSignatory.setReceiver(new User( platformDept.getPhone(), "MOBILE"));
        draftContract.addSignatory(platformSignatory);
        //丙方
        Signatory initiator2 = new Signatory();
        initiator2.setTenantType("COMPANY");
        initiator2.setTenantName(userDept.getName());
        initiator2.setReceiver(new User(userDept.getPhone(), "MOBILE"));
        draftContract.addSignatory(initiator2);

        //模板参数
        //卖车
        draftContract.setCategory(new Category(3083237961123238073L));//业务分类配置
        draftContract.setSend(true); //发起合同
        return draftContract;
    }


    //模板参数
    private List<TemplateParam>  buildTemplateParam(CarInfoDO carInfo, CarInfoDetailsDO carInfoDetailsDO,DeptDO userDept,DeptDO platformDept,UserExtDO userDo,String type) {
        List<TemplateParam> params =new ArrayList<>();
        //收车委托合同
        if ("1".equals(type)) {
            params.add(new TemplateParam("受托人", platformDept.getName()));
            params.add(new TemplateParam("甲方营业执照号", platformDept.getTaxNum()));
            params.add(new TemplateParam("甲方法定代表人", platformDept.getLegalRepresentative()));
            params.add(new TemplateParam("甲方联系电话", platformDept.getPhone()));
            params.add(new TemplateParam("甲方联系地址", platformDept.getAddress()));

            params.add(new TemplateParam("委托人", userDept.getName()));
            params.add(new TemplateParam("乙方营业执照号", userDept.getTaxNum()));
            params.add(new TemplateParam("乙方法定代表人", userDept.getLegalRepresentative()));
            params.add(new TemplateParam("乙方联系电话", userDept.getPhone()));
            params.add(new TemplateParam("乙方联系地址", userDept.getAddress()));

            params.add(new TemplateParam("车辆牌号", carInfo.getPlateNum()));
            params.add(new TemplateParam("车辆类型", carInfo.getCarType()));
            params.add(new TemplateParam("厂牌、型号", carInfo.getModel()));
            params.add(new TemplateParam("颜色", carInfoDetailsDO.getColour()));
            params.add(new TemplateParam("初次登记日期", carInfoDetailsDO.getFirstRegistDate()));
            params.add(new TemplateParam("登记证号", carInfoDetailsDO.getCertificateNo()));
            params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
            params.add(new TemplateParam("车架号码", carInfo.getVin()));
            params.add(new TemplateParam("行驶里程", carInfoDetailsDO.getMileage().toString()));
            params.add(new TemplateParam("使用年限", carInfo.getScrapDate().toString()));
            params.add(new TemplateParam("年检签证有效期", carInfo.getAnnualInspectionDate().toString()));
            params.add(new TemplateParam("保险险种", carInfo.getInsurance()));
            params.add(new TemplateParam("保险有效期", carInfo.getInsuranceEndData()));
            params.add(new TemplateParam("收车金额大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
            params.add(new TemplateParam("收车金额小写", carInfo.getVehicleReceiptAmount().toString()));
            params.add(new TemplateParam("甲方收款银行",userDo.getBankName()));
            params.add(new TemplateParam("甲方收款账号",userDo.getBondBankAccount()));
            //params.add(new TemplateParam("甲方收款银行", "工商"));
            //params.add(new TemplateParam("甲方收款账号", "6228 4804 8172 3886 810"));
        }else if ("2".equals(type)){
            //卖车委托合同
            params.add(new TemplateParam("受托人", platformDept.getName()));
            params.add(new TemplateParam("甲方营业执照号",platformDept.getTaxNum()));
            params.add(new TemplateParam("甲方法定代表人",platformDept.getLegalRepresentative()));
            params.add(new TemplateParam("甲方联系电话",platformDept.getPhone()));
            params.add(new TemplateParam("甲方联系地址",platformDept.getAddress()));

            params.add(new TemplateParam("委托人",userDept.getName()));
            params.add(new TemplateParam("乙方营业执照号",userDept.getTaxNum()));
            params.add(new TemplateParam("乙方法定代表人",userDept.getLegalRepresentative()));
            params.add(new TemplateParam("乙方联系电话",userDept.getPhone()));
            params.add(new TemplateParam("乙方联系地址",userDept.getAddress()));

            params.add(new TemplateParam("车辆牌号",carInfo.getPlateNum()));
            params.add(new TemplateParam("车辆类型",carInfo.getCarType()));
            params.add(new TemplateParam("厂牌、型号",carInfo.getModel()));
            params.add(new TemplateParam("颜色",carInfoDetailsDO.getColour()));
            params.add(new TemplateParam("初次登记日期",carInfoDetailsDO.getFirstRegistDate()));
            params.add(new TemplateParam("登记证号",carInfoDetailsDO.getCertificateNo()));
            params.add(new TemplateParam("发动机号码",carInfo.getEngineNum()));
            params.add(new TemplateParam("车架号码",carInfo.getVin()));
            params.add(new TemplateParam("行驶里程",carInfoDetailsDO.getMileage().toString()));
            params.add(new TemplateParam("使用年限",carInfo.getScrapDate().toString()));
            params.add(new TemplateParam("年检签证有效期",carInfo.getAnnualInspectionDate().toString()));
            params.add(new TemplateParam("保险险种",carInfo.getInsurance()));
            params.add(new TemplateParam("保险有效期",carInfo.getInsuranceEndData()));
            params.add(new TemplateParam("收车金额大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
        }else if ("3".equals(type)){
            //s收车合同
            params.add(new TemplateParam("卖方受托人", carInfoDetailsDO.getSellerName()));
            params.add(new TemplateParam("甲方身份证号", carInfoDetailsDO.getSellerIdCard()));
            params.add(new TemplateParam("甲方法定代表人", carInfoDetailsDO.getSellerName()));
            params.add(new TemplateParam("甲方联系电话", carInfoDetailsDO.getSellerTel()));
            params.add(new TemplateParam("甲方联系地址", carInfoDetailsDO.getSellerAdder()));

            params.add(new TemplateParam("平台受托人", platformDept.getName()));
            params.add(new TemplateParam("乙方营业执照号",platformDept.getTaxNum()));
            params.add(new TemplateParam("乙方法定代表人",platformDept.getLegalRepresentative()));
            params.add(new TemplateParam("乙方联系电话",platformDept.getPhone()));
            params.add(new TemplateParam("乙方联系地址",platformDept.getAddress()));

            params.add(new TemplateParam("车商公司名称",userDept.getName()));
            params.add(new TemplateParam("丙方营业执照号",userDept.getTaxNum()));
            params.add(new TemplateParam("丙方法定代表人",userDept.getLegalRepresentative()));
            params.add(new TemplateParam("丙方联系电话",userDept.getPhone()));
            params.add(new TemplateParam("丙方联系地址",userDept.getAddress()));


            params.add(new TemplateParam("首次登记日期",carInfoDetailsDO.getFirstRegistDate()));
            params.add(new TemplateParam("发动机号码",carInfo.getEngineNum()));
            params.add(new TemplateParam("车架号码",carInfo.getVin()));
            params.add(new TemplateParam("行驶里程",carInfoDetailsDO.getMileage().toString()));

            params.add(new TemplateParam("收车金额大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
            params.add(new TemplateParam("收车金额小写",carInfo.getVehicleReceiptAmount().toString()));
            params.add(new TemplateParam("付款方式","全款"));
        }else if ("4".equals(type)) {
            //卖车合同
            params.add(new TemplateParam("买方受托人", carInfoDetailsDO.getBuyerName()));
            params.add(new TemplateParam("甲方身份证号", carInfoDetailsDO.getBuyerIdCard()));
            params.add(new TemplateParam("甲方法定代表人", carInfoDetailsDO.getBuyerName()));
            params.add(new TemplateParam("甲方联系电话", carInfoDetailsDO.getBuyerTel()));
            params.add(new TemplateParam("甲方联系地址", carInfoDetailsDO.getBuyerAdder()));


            params.add(new TemplateParam("平台受托人", platformDept.getName()));
            params.add(new TemplateParam("乙方营业执照号",platformDept.getTaxNum()));
            params.add(new TemplateParam("乙方法定代表人",platformDept.getLegalRepresentative()));
            params.add(new TemplateParam("乙方联系电话",platformDept.getPhone()));
            params.add(new TemplateParam("乙方联系地址",platformDept.getAddress()));

            params.add(new TemplateParam("车商公司名称",userDept.getName()));
            params.add(new TemplateParam("丙方营业执照号",userDept.getTaxNum()));
            params.add(new TemplateParam("丙方法定代表人",userDept.getLegalRepresentative()));
            params.add(new TemplateParam("丙方联系电话",userDept.getPhone()));
            params.add(new TemplateParam("丙方联系地址",userDept.getAddress()));


            params.add(new TemplateParam("首次登记日期",carInfoDetailsDO.getFirstRegistDate()));
            params.add(new TemplateParam("发动机号码",carInfo.getEngineNum()));
            params.add(new TemplateParam("车架号码",carInfo.getVin()));
            params.add(new TemplateParam("行驶里程",carInfoDetailsDO.getMileage().toString()));
            if (carInfo.getOther()!=null) {
                params.add(new TemplateParam("其他约定", carInfo.getOther()));
            }else
            {
                params.add(new TemplateParam("其他约定", "无"));
            }
        }

        return params;
    }

    private Boolean verificationSignature(String signature, String timestamp){
        String md5 = MD5.toMD5(timestamp + SECRET);
        if (!StrUtil.equals(signature,md5)) {
            log.error("验证签名不一致，md5【{}】,signature【{}】",md5,signature);
            System.out.println("签名不一致");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private String decryptMessage(String content) throws Exception {
        return CryptUtils.aesDerypt(content, SECRET);
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
