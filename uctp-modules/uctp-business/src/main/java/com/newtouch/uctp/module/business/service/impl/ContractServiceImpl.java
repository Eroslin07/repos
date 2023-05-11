package com.newtouch.uctp.module.business.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.security.core.LoginUser;
import com.newtouch.uctp.framework.security.core.util.SecurityFrameworkUtils;
import com.newtouch.uctp.framework.tenant.core.context.TenantContextHolder;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.AppContractarVO;
import com.newtouch.uctp.module.business.controller.app.carInfo.vo.CarDCVo;
import com.newtouch.uctp.module.business.dal.dataobject.*;
import com.newtouch.uctp.module.business.dal.dataobject.qys.QysConfigDO;
import com.newtouch.uctp.module.business.dal.dataobject.user.UserExtDO;
import com.newtouch.uctp.module.business.dal.mysql.ContractMapper;
import com.newtouch.uctp.module.business.dal.mysql.InvoiceTitleMapper;
import com.newtouch.uctp.module.business.dal.mysql.user.UserExtMapper;
import com.newtouch.uctp.module.business.dal.mysql.user.UserMapper;
import com.newtouch.uctp.module.business.enums.QysContractStatus;
import com.newtouch.uctp.module.business.service.BusinessFileService;
import com.newtouch.uctp.module.business.service.CarInfoDetailsService;
import com.newtouch.uctp.module.business.service.CarInfoService;
import com.newtouch.uctp.module.business.service.contract.ContractService;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;
import com.newtouch.uctp.module.business.util.NullReplaceUtil;
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
import com.qiyuesuo.sdk.v2.response.DocumentAddResult;
import com.qiyuesuo.sdk.v2.utils.IOUtils;
import com.qiyuesuo.sdk.v2.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.date.DatePattern.CHINESE_DATE_PATTERN;
import static com.newtouch.uctp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.newtouch.uctp.module.business.enums.ErrorCodeConstants.*;
import static com.newtouch.uctp.module.infra.enums.ErrorCodeConstants.FILE_SAVE_ERROR;
import static com.newtouch.uctp.module.system.enums.ErrorCodeConstants.*;

/**
 * 车辆合同 Service 实现类
 *
 * @author lc
 */
@Service
@Validated
@Slf4j
public class ContractServiceImpl implements ContractService {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private QiyuesuoClientFactory qiyuesuoClientFactory;
    @Resource
    private FileApi fileApi;
    @Resource
    @Lazy
    private QysConfigService qysConfigService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    @Lazy
    private CarInfoService carInfoService;
    @Resource
    @Lazy
    private CarInfoDetailsService carInfoDetailsService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private UserExtMapper userExtMapper;
    @Resource
    private DeptApi deptApi;
    @Resource
    private InvoiceTitleMapper invoiceTitleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BusinessFileService businessFileService;

    @Override
    public List<AppContractarVO> getContractInfo(String carID) {
        List<AppContractarVO> contractInfo = contractMapper.getContractInfo(carID);

        List<AppContractarVO> contractInfo1 =new ArrayList<>();
        for (AppContractarVO appContractarVO : contractInfo) {
            contractInfo1.add(setContractUrl(appContractarVO));
        }
        return contractInfo1;
    }


    @Override
    public String updateContractStatas(CarDCVo carDCVo) {
        String result="更新失败";
        int i=contractMapper.updateContractStatas(carDCVo);
        if (i>0)
            result="更新失败";
        return result;
    }

    @Override
    public List<CarDCVo> getContractIds(String contractID) {
        return contractMapper.getContractIds(contractID);
    }

    @Override
    public List<ContractDO> getContractListByCarId(Long id) {
        return contractMapper.selectByCarID(id);
    }

    @Override
    public ContractDO getById(Long id) {
        return contractMapper.selectById(id);
    }

    @Transactional
    @Override
    public void contractInvalid(Long id,String reason) {
        ContractDO contractDO = contractMapper.selectById(id);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        //修改契约锁合同状态已作废
        QysConfigDO qysConfigDO = qysConfigService.getByDeptId(contractDO.getBusinessId());
        if (ObjectUtil.isNull(qysConfigDO) || StrUtil.isBlank(qysConfigDO.getAccessKey())) {
            throw exception(QYS_CONFIG_AUTH_ERROR);
        }
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(qysConfigDO.getId());
        Contract contract = client.defaultContractDetail(contractDO.getContractId()).getCheckedData();
        if (StrUtil.equals(contract.getStatus(), QysContractStatus.DRAFT.value())) {
            client.defaultContractInvalid(contractDO.getContractId());
        }
        if (StrUtil.equals(contract.getStatus(), QysContractStatus.SIGNING.value())) {
            client.defaultContractInvalid(contractDO.getContractId(),reason);
        }
        if (StrUtil.equals(contract.getStatus(), QysContractStatus.COMPLETE.value())) {
            client.defaultContractInvalid(contractDO.getContractId(),null,reason);
        }
        //修改合同状态为已作废
        contractDO.setStatus(3);
        contractMapper.updateById(contractDO);
        CarInfoDO carInfo = carInfoService.getCarInfo(contractDO.getCarId());
//        carInfo.setStatusThree();
    }

    @Override
    public ContractDO getCollectDraft(Long carId, Integer contractType) {
        return contractMapper.findCollectDraft(carId, contractType);
    }

    @Override
    public void update(ContractDO contractDO) {
        if (ObjectUtil.isNotNull(contractDO)) {
            contractMapper.updateById(contractDO);
        }
    }

    @Override
    public ContractDO getByContractId(Long contractId) {
        return contractMapper.selectOne("CONTRACT_ID",contractId);
    }


    /**
     * 将合同的url放到实体中
     */
    public AppContractarVO setContractUrl(AppContractarVO appContractarVO){

        CommonResult<List<FileRespDTO>> listContractar =null;

        List<Long> contractList=new ArrayList<>();
        List<CarDCVo> contractIds= getContractIds(appContractarVO.getContractID()) ;//一条合同数据的ids;正常情况一个合同只会有一个pdf文件
        for (CarDCVo contractId : contractIds) {
            contractList.add(contractId.getLongId());
        }
        listContractar= fileApi.fileList(contractList);
        if(listContractar.getData()!=null) {
            for (FileRespDTO datum : listContractar.getData()) {

                appContractarVO.setUrl(datum.getUrl());
            }
        }
        return appContractarVO;
    }

    public String GenerateCode(Integer type){
        String busiTypeCode = "";
        switch (type){
//            1收车委托合同   2收车合同  3卖车委托合同  4卖车合同
            case 1:
                busiTypeCode = "BCV";
                break;
            case 2:
                busiTypeCode = "BC";
                break;
            case 3:
                busiTypeCode = "CSV";
                break;
            case 4:
                busiTypeCode = "CS";
                break;
            default:
                throw exception(CONTRACT_TYPE_UNKNOWN);
        }
        String serialNoPrefix = busiTypeCode.concat(DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now()));
        Long index = this.redisTemplate.opsForValue().increment(serialNoPrefix, 1L);
        return serialNoPrefix.concat(String.format("%04d", index.intValue()));
    }

    @Transactional
    @Override
    public void draft(Long carId) {
        //目前发起方已确认为翼龙一家企业
        QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
        CarInfoDO carInfo = carInfoService.getCarInfo(carId);
        CarInfoDetailsDO carInfoDetailDO = carInfoDetailsService.getCarInfoDetailsByCarId(carId);
        if (ObjectUtil.isNull(carInfo) || ObjectUtil.isNull(carInfoDetailDO)) {
            throw exception(CAR_INFO_EXIST_OTHER);
        }
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        AdminUserRespDTO usersDTO = adminUserApi.getUser(loginUser.getId()).getCheckedData();
        UserExtDO userExtDO = userExtMapper.selectOne("USER_ID", usersDTO.getId());
        if (ObjectUtil.isNull(userExtDO)) {
            throw exception(USER_EXT_NOT_EXISTS);
        }
        //商户
        DeptRespDTO merchantDeptRespDTO = deptApi.getDept(usersDTO.getDeptId()).getCheckedData();
        if (ObjectUtil.isNull(merchantDeptRespDTO)) {
            throw exception(DEPT_NOT_FOUND);
        }
        //商户-主账号
        AdminUserRespDTO merchantMasterUserDTO = adminUserApi.getMasterUser(merchantDeptRespDTO.getId()).getCheckedData();
        //平台方
        DeptRespDTO platformDept = deptApi.getPlatformDept().getCheckedData();
        if (ObjectUtil.isNull(platformDept)) {
            throw exception(PLATFORM_DEPT_NOT_FOUND);
        }
        //平台方-主账号
        AdminUserRespDTO platformMasterUserDTO = adminUserApi.getMasterUser(merchantDeptRespDTO.getId()).getCheckedData();
        //平台方发票抬头信息
        InvoiceTitleDO invoiceTitleDO = invoiceTitleMapper.selectOne("dept_id", platformDept.getId());
        if (ObjectUtil.isNull(invoiceTitleDO)) {
            throw exception(INVOICE_TITLE_NOT_EXISTS);
        }
        Contract entrustContract = this.entrustContractDraft("两方-二手车-收车委托合同", 3078145859615985671L,
                platformDept.getName(), platformMasterUserDTO.getMobile(),
                 merchantDeptRespDTO.getName(), merchantMasterUserDTO.getMobile(),"" );
        //查询委托合同
        ContractDO entrustContractDO = contractMapper.selectOne("CAR_ID", carId,"CONTRACT_TYPE",1);
        if (ObjectUtil.isNotNull(entrustContractDO)) {
            entrustContract.setId(entrustContractDO.getContractId());
        }else {
            entrustContractDO = new ContractDO();
        }
        //发起委托草稿
        entrustContract = client.defaultDraftSend(entrustContract).getCheckedData();
        //合同模板
        List<TemplateParam> entrustTemplateParams = this.buildTemplateParam(carInfo, carInfoDetailDO, merchantDeptRespDTO, platformDept, merchantMasterUserDTO, platformMasterUserDTO, "1");
        //下载合同文档，并存入本地文件
        String entrustTitle = "二手车委托收购协议.pdf";
        DocumentAddResult entrustDoc = client.defaultDocumentAddByTemplate(entrustContract.getId(), 3089851249420403111L, entrustTemplateParams, entrustTitle).getCheckedData();
        this.documentDownload(entrustDoc.getDocumentId(),entrustTitle);
        //发起收车/卖车合同
        Contract contract = this.contractDraft("三方-二手车-收车合同", 3083237961123238073L,
                carInfoDetailDO.getSellerName(), carInfoDetailDO.getSellerTel(),
                platformDept.getName(), platformMasterUserDTO.getMobile(),
                merchantDeptRespDTO.getName(), merchantMasterUserDTO.getMobile(),
                "");
        //查询收车/卖车合同
        ContractDO contractDO = contractMapper.selectOne("CAR_ID", carId,"CONTRACT_TYPE",2);
        if (ObjectUtil.isNotNull(contractDO)) {
            entrustContract.setId(contractDO.getContractId());
        }else {
            contractDO = new ContractDO();
        }
        contract = client.defaultDraftSend(contract).getCheckedData();
        //合同模板
        List<TemplateParam> templateParams = this.buildTemplateParam(carInfo, carInfoDetailDO, merchantDeptRespDTO, platformDept, merchantMasterUserDTO, platformMasterUserDTO, "1");
        //下载合同文档，并存入本地文件
        String title = "二手车收购协议.pdf";
        DocumentAddResult doc = client.defaultDocumentAddByTemplate(entrustContract.getId(), 3089851249420403111L, entrustTemplateParams, entrustTitle).getCheckedData();
        this.documentDownload(doc.getDocumentId(),title);

        //保存数据到合同表
        this.qysSave(entrustContract.getId(), carId, entrustTitle, usersDTO.getDeptId(),
                "123", 1);
        this.qysSave(contract.getId(), carId, title, usersDTO.getDeptId(),
                "456", 2);
    }

    @Override
    public void documentDownload(Long documentId,String fileName) {
        if (ObjectUtil.isNull(documentId)) {
            throw exception(QYS_CONFIG_DOCUMENT_DOWNLOAD_FAIL);
        }
        File tempFile = null;
        FileOutputStream fos = null;
        try {
            QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
            tempFile = File.createTempFile("temp","temp.pdf");
            fos = new FileOutputStream(tempFile);
            client.defaultDocumentDownload(fos, documentId).getCheckedData();
            FileReader reader = new FileReader(tempFile);
            //将委托合同写入远程服务器以及中间表
            FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
            fileCreateReqDTO.setContent(reader.readBytes());
            fileCreateReqDTO.setName(fileName);
            fileCreateReqDTO.setPath(null);
            FileDTO fileDTO = fileApi.createFile(fileCreateReqDTO).getCheckedData();
            if (ObjectUtil.isNull(fileDTO)) {
                throw exception(FILE_SAVE_ERROR);
            }

        }catch (Exception e){
            log.error("契约锁合同下载失败",e);
            throw exception(QYS_CONFIG_DOCUMENT_DOWNLOAD_FAIL);
        }finally {
            FileUtil.del(tempFile);
            IOUtils.safeClose(fos);
        }
    }

    @Override
    public void contractDownload(Long contractId,String fileName) {
        if (ObjectUtil.isNull(contractId)) {
            throw exception(QYS_CONFIG_DOCUMENT_DOWNLOAD_FAIL);
        }
        File tempFile = null;
        FileOutputStream fos = null;
        try {
            QiyuesuoClient client = qiyuesuoClientFactory.getQiyuesuoClient(2L);
            tempFile = File.createTempFile("contractTemp","temp.pdf");
            fos = new FileOutputStream(tempFile);
            client.defaultContractDownload(fos, contractId, ListUtil.of("CONTRACT"), Boolean.FALSE);
            FileReader reader = new FileReader(tempFile);
            //将委托合同写入远程服务器以及中间表
            FileCreateReqDTO fileCreateReqDTO = new FileCreateReqDTO();
            fileCreateReqDTO.setContent(reader.readBytes());
            fileCreateReqDTO.setName(fileName);
            fileCreateReqDTO.setPath(null);
            FileDTO fileDTO = fileApi.createFile(fileCreateReqDTO).getCheckedData();
            if (ObjectUtil.isNull(fileDTO)) {
                throw exception(FILE_SAVE_ERROR);
            }
            System.out.println(JSONUtil.toJsonStr(fileDTO));
            List<BusinessFileDO> businessFileDOS = businessFileService.getByMainId(contractId);
            if (CollUtil.isEmpty(businessFileDOS)) {
                //这里收/卖车时，已经存入了数据
                log.warn("没找到关联的合同文件,contractId:{}",contractId);
            }
            BusinessFileDO businessFileDO = businessFileDOS.get(0);
            businessFileDO.setId(fileDTO.getId());
            //删除中间表business的数据
            businessFileService.deleteByMainId(contractId);
            businessFileService.insert(businessFileDO);
        }catch (Exception e){
            log.error("契约锁合同下载失败",e);
            throw exception(QYS_CONFIG_DOCUMENT_DOWNLOAD_FAIL);
        }finally {
//            FileUtil.del(tempFile);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            IOUtils.safeClose(fos);
        }
    }

    @Override
    public Integer qysSave(Long contractId,Long carId,
                           String contractName,Long deptId,
                           String code,Integer contractType) {
        ContractDO contractDO = new ContractDO();
        contractDO.setContractId(contractId);
        contractDO.setCarId(carId);
        contractDO.setContractName(contractName);
        contractDO.setStatus(0);
        contractDO.setContractType(contractType);
        contractDO.setTenantId(TenantContextHolder.getTenantId());
        contractDO.setBusinessId(deptId);
        contractDO.setCode(code);
        return contractMapper.insert(contractDO);
    }


    /**
     * 构建委托合同数据
     *
     * @param subject            合同主题（合同名称）
     * @param categoryId         业务分类Id
     * @param platformTenantName 平台方公司名称
     * @param platformContact    平台方公司接收人联系方式
     * @param merchantTenantName 商户公司名称
     * @param merchantContact    商户公司接收人联系方式
     * @param creatorContact     创建人联系方式；默认为虚拟用户， 创建人必须已经加入对接方的公司
     * @return
     */
    private Contract entrustContractDraft(String subject, Long categoryId,
                                          String platformTenantName, String platformContact,
                                          String merchantTenantName, String merchantContact,
                                          String creatorContact) {
        Contract entrustDraft = new Contract();
        entrustDraft.setSubject(subject);
        //商户
        Signatory merchantSignatory = new Signatory();
        merchantSignatory.setTenantType("COMPANY");
        merchantSignatory.setTenantName(merchantTenantName);
        merchantSignatory.setReceiver(new User(merchantContact, "MOBILE"));
//        initiator2.setSerialNo(2);
        entrustDraft.addSignatory(merchantSignatory);
        //平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformTenantName);
        platformSignatory.setReceiver(new User(platformContact, "MOBILE"));
//        platformSignatory.setSerialNo(1);
        entrustDraft.addSignatory(platformSignatory);

        entrustDraft.setCategory(new Category(categoryId));//业务分类配置
        entrustDraft.setSend(false); // 发起合同
        if (StrUtil.isNotBlank(creatorContact)) {
            entrustDraft.setCreator(new User(creatorContact, "MOBILE"));
        }
        return entrustDraft;
    }

    /**
     * 构建合同数据
     *
     * @param subject            合同主题（合同名称）
     * @param categoryId         业务分类Id
     * @param sellTenantName     个人名字
     * @param sellContact        个人联系方式
     * @param platformTenantName 平台方公司名称
     * @param platformContact    平台方公司接收人联系方式
     * @param merchantTenantName 商户公司名称
     * @param merchantContact    商户公司接收人联系方式
     * @param creatorContact     创建人联系方式；默认为虚拟用户， 创建人必须已经加入对接方的公司
     * @return
     */
    private Contract contractDraft(String subject, Long categoryId,
                                          String sellTenantName, String sellContact,
                                          String platformTenantName, String platformContact,
                                          String merchantTenantName, String merchantContact,
                                          String creatorContact) {
        Contract entrustDraft = new Contract();
        entrustDraft.setSubject(subject);
        //商户
        Signatory sellSignatory = new Signatory();
        sellSignatory.setTenantType("PERSONAL");
        sellSignatory.setTenantName(sellTenantName);
        sellSignatory.setReceiver(new User(sellContact, "MOBILE"));
//        initiator2.setSerialNo(1);
        entrustDraft.addSignatory(sellSignatory);
        //商户
        Signatory merchantSignatory = new Signatory();
        merchantSignatory.setTenantType("COMPANY");
        merchantSignatory.setTenantName(merchantTenantName);
        merchantSignatory.setReceiver(new User(merchantContact, "MOBILE"));
//        initiator2.setSerialNo(2);
        entrustDraft.addSignatory(merchantSignatory);
        //平台
        Signatory platformSignatory = new Signatory();
        platformSignatory.setTenantType("COMPANY");
        platformSignatory.setTenantName(platformTenantName);
        platformSignatory.setReceiver(new User(platformContact, "MOBILE"));
//        platformSignatory.setSerialNo(3);
        entrustDraft.addSignatory(platformSignatory);

        entrustDraft.setCategory(new Category(categoryId));//业务分类配置
        entrustDraft.setSend(false); // 发起合同
        if (StrUtil.isNotBlank(creatorContact)) {
            entrustDraft.setCreator(new User(creatorContact, "MOBILE"));
        }
        return entrustDraft;
    }

    private List<TemplateParam>  buildTemplateParam(CarInfoDO carInfo,
                                                    CarInfoDetailsDO carInfoDetailsDO,
                                                    DeptRespDTO userDept,
                                                    DeptRespDTO platformDept,
                                                    AdminUserRespDTO pUserDO,
                                                    AdminUserRespDTO platformUserDO,
                                                    String type) {
        InvoiceTitleDO invoiceTitleDO = invoiceTitleMapper.selectOne("dept_id", platformDept.getId());
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
                params.add(new TemplateParam("合同编号", this.GenerateCode(1)));
                if (StringUtils.isEmpty(platformDept.getName()))
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

                String firstRegistDate = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfoDetailsDO.getFirstRegistDate()),
                        false, false);
                params.add(new TemplateParam("初次登记日期", firstRegistDate));
                params.add(new TemplateParam("登记证号", carInfoDetailsDO.getCertificateNo()));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                String annualInspectionDate = DateUtil.format(carInfo.getAnnualInspectionDate(), CHINESE_DATE_PATTERN);
                params.add(new TemplateParam("年检签证有效期", annualInspectionDate));
                params.add(new TemplateParam("保险险种", carInfo.getInsurance()));
                String insuranceEndData = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfo.getInsuranceEndData()),
                        false, false);
                params.add(new TemplateParam("保险有效期", insuranceEndData));
                params.add(new TemplateParam("收车金额大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("收车金额小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                params.add(new TemplateParam("甲方收款银行", invoiceTitleDO.getBank()));
                params.add(new TemplateParam("甲方收款账号", invoiceTitleDO.getBankAccount()));
                params.add(new TemplateParam("甲方收款名称", invoiceTitleDO.getCompanyName()));
                //params.add(new TemplateParam("甲方收款银行", "工商"));
                //params.add(new TemplateParam("甲方收款账号", "6228 4804 8172 3886 810"));
            } else if ("2".equals(type)) {
                //卖车委托合同-直接付款
                params.add(new TemplateParam("合同编号", this.GenerateCode(Integer.valueOf(type))));

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
                String firstRegistDate = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfoDetailsDO.getFirstRegistDate()),
                        false, false);
                params.add(new TemplateParam("初次登记日期", firstRegistDate));
                params.add(new TemplateParam("登记证号", carInfoDetailsDO.getCertificateNo()));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                String annualInspectionDate = DateUtil.format(carInfo.getAnnualInspectionDate(), CHINESE_DATE_PATTERN);
                params.add(new TemplateParam("年检签证有效期", annualInspectionDate));
                params.add(new TemplateParam("保险险种", carInfo.getInsurance()));
                String insuranceEndData = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfo.getInsuranceEndData()),
                        false, false);
                params.add(new TemplateParam("保险有效期", insuranceEndData));
                params.add(new TemplateParam("卖车金额小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                params.add(new TemplateParam("卖车金额大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                params.add(new TemplateParam("定金大写", UppercaseUtil.convert(carInfo.getDeposit())));
                params.add(new TemplateParam("定金小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getDeposit()))));
                params.add(new TemplateParam("剩余车款大写", UppercaseUtil.convert(carInfo.getBalancePayment())));
                params.add(new TemplateParam("剩余车款小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getBalancePayment()))));
            } else if ("21".equals(type)) {
                //卖车委托合同-按揭付款
                params.add(new TemplateParam("合同编号", this.GenerateCode(4)));

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
                String firstRegistDate = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfoDetailsDO.getFirstRegistDate()),
                        false, false);
                params.add(new TemplateParam("初次登记日期", firstRegistDate));
                params.add(new TemplateParam("登记证号", carInfoDetailsDO.getCertificateNo()));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                String annualInspectionDate = DateUtil.format(carInfo.getAnnualInspectionDate(), CHINESE_DATE_PATTERN);
                params.add(new TemplateParam("年检签证有效期", annualInspectionDate));
                params.add(new TemplateParam("保险险种", carInfo.getInsurance()));
                String insuranceEndData = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfo.getInsuranceEndData()),
                        false, false);
                params.add(new TemplateParam("保险有效期", insuranceEndData));
                params.add(new TemplateParam("卖车金额小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                params.add(new TemplateParam("卖车金额大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));

                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                params.add(new TemplateParam("定金大写", UppercaseUtil.convert(carInfo.getDeposit())));
                params.add(new TemplateParam("定金小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getDeposit()))));
//            params.add(new TemplateParam("尾款大写", "0"));
//            params.add(new TemplateParam("尾款小写", "0"));
            } else if ("3".equals(type)) {
                //s收车合同
                params.add(new TemplateParam("合同编号", this.GenerateCode(Integer.valueOf(type))));
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
                String firstRegistDate = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfoDetailsDO.getFirstRegistDate()),
                        false, false);
                params.add(new TemplateParam("首次登记日期", firstRegistDate));
                params.add(new TemplateParam("发动机号码", carInfo.getEngineNum()));
                params.add(new TemplateParam("车架号码", carInfo.getVin()));
                params.add(new TemplateParam("行驶里程", NullReplaceUtil.nullReplace(String.valueOf(carInfoDetailsDO.getMileage()))));
                params.add(new TemplateParam("车辆手续及备件", getProceduresAndSpareParts(proceduresAndSpareParts)));

                params.add(new TemplateParam("车辆总价大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("车辆总价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
                //  params.add(new TemplateParam("付款方式", "全款"));
            } else if ("4".equals(type)) {
                //卖车合同-直接付款
                params.add(new TemplateParam("合同编号", this.GenerateCode(Integer.valueOf(type))));

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

                String firstRegistDate = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfoDetailsDO.getFirstRegistDate()),
                        false, false);
                params.add(new TemplateParam("首次登记日期",firstRegistDate));
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
                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
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
                params.add(new TemplateParam("合同编号", this.GenerateCode(4)));

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

                String firstRegistDate = DateUtil.formatChineseDate(
                        DateUtil.parseDate(carInfoDetailsDO.getFirstRegistDate()),
                        false, false);
                params.add(new TemplateParam("首次登记日期", firstRegistDate));
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
                params.add(new TemplateParam("车价大写", UppercaseUtil.convert(carInfo.getVehicleReceiptAmount())));
                params.add(new TemplateParam("车价小写", NullReplaceUtil.nullReplace(String.valueOf(carInfo.getVehicleReceiptAmount()))));
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

    private String getProceduresAndSpareSell(ProceduresAndSpareSell parts) {
        String result = "";
        if (parts != null) {
            if (parts.getDrivingLicense() != null && parts.getDrivingLicense()) {
                result += "有行驶证(正/副)本";
            }
            if (parts.getCarInvoice() != null && parts.getCarInvoice()) {
                result += "、有购车发票";
            }
            if (parts.getRegistrationCertificate() != null && parts.getRegistrationCertificate()) {
                result += "、有机动车登记证";
            }
            if (parts.getPurchaseTax() != null && parts.getPurchaseTax()) {
                result += "、有购置税完税凭证";
            }
            if (parts.getSpareTire() != null && parts.getSpareTire()) {
                result += "、有备胎";
            }
            if (parts.getCarShipTax() != null && parts.getCarShipTax()) {
                result += "、有车船使用税完税凭证";
            }
            if (parts.getHeavyTrafficInsurance() != null && parts.getHeavyTrafficInsurance()) {
                result += "、有交强险保单";
            }
            if (parts.getCommercialInsurance() != null && parts.getCommercialInsurance()) {
                result += "、有商业险保单";
            }
            if (parts.getJack() != null && parts.getJack()) {
                result += "、有千斤顶";
            }
            if (parts.getSpecification() != null && parts.getSpecification()) {
                result += "、有说明书";
            }
            if (parts.getVehicleKey() != null) {
                result += "、钥匙数量:" + parts.getVehicleKey()+"组";
            }
            if (parts.getAccidentVehicle() != null) {
                result += "、" + parts.getAccidentVehicle();
            }
        }
        return result;
    }
    private String getProceduresAndSpareParts(ProceduresAndSpareParts parts){
        String result="";
        if (parts!=null) {
            if (parts.getDrivingLicense() != null && parts.getDrivingLicense()) {
                result += "有行驶证(正/副)本";
            }
            if (parts.getCarInvoice() != null && parts.getCarInvoice()) {
                result += "、有购车发票";
            }
            if (parts.getRegistrationCertificate() != null && parts.getRegistrationCertificate()) {
                result += "、有机动车登记证";
            }
            if (parts.getPurchaseTax() != null && parts.getPurchaseTax()) {
                result += "、有购置税完税凭证";
            }
            if (parts.getSpareTire() != null && parts.getSpareTire()) {
                result += "、有备胎";
            }
            if (parts.getCarShipTax() != null && parts.getCarShipTax()) {
                result += "、有车船使用税完税凭证";
            }
            if (parts.getHeavyTrafficInsurance() != null && parts.getHeavyTrafficInsurance()) {
                result += "、有交强险保单";
            }
            if (parts.getCommercialInsurance() != null && parts.getCommercialInsurance()) {
                result += "、有商业险保单";
            }
            if (parts.getJack() != null && parts.getJack()) {
                result += "、有千斤顶";
            }
            if (parts.getSpecification() != null && parts.getSpecification()) {
                result += "、有说明书";
            }
            if (parts.getVehicleKey() != null) {
                result += "、钥匙数量:" + parts.getVehicleKey()+"组";
            }
            if (parts.getAccidentVehicle() != null) {
                result += "、" + parts.getAccidentVehicle();
            }
        }
        return result;
    }

    private String getFeesAndCommitments(String vehicle,String type){
        String result="";
        if (vehicle!=null) {
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

}


