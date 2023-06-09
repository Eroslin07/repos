package com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.common.pojo.CommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.AbstractQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.param.SignParam;
import com.qiyuesuo.sdk.v2.request.*;
import com.qiyuesuo.sdk.v2.response.*;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

public class DefaultQiyuesuoClient extends AbstractQiyuesuoClient {
    /**
     * 契约锁客户端
     */
    private volatile SdkClient client;
    /**
     * 错误码枚举类
     */
//    protected final QiyuesuoCodeMapping codeMapping;

    public DefaultQiyuesuoClient(QiyuesuoChannelProperties properties) {
        super(properties, new DefaultCodeMapping());
//        this.codeMapping = codeMapping;
        Assert.notEmpty(properties.getAccessKey(), "apiKey 不能为空");
        Assert.notEmpty(properties.getAccessSecret(), "apiSecret 不能为空");
    }

    @Override
    protected void doInit() {
        client = new SdkClient(properties.getServerUrl(), properties.getAccessKey(), properties.getAccessSecret());
    }

    @Override
    protected QiyuesuoCommonResult<EmployeeListResult> doDefaultEmployeeList(EmployeeListRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<EmployeeListResult> sdkResponse = JSONUtils.toQysResponse(response,EmployeeListResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Contract> doDefaultContractInvalidSign(ContractSignInvalidRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<Contract> sdkResponse = JSONUtils.toQysResponse(response,Contract.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Boolean> doDefaultContractDownload(ContractDownloadRequest request, FileOutputStream fos) throws Throwable {
        this.client.download(request,fos);
        return QiyuesuoCommonResult.build("0".toString()
                , ""
                , true
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Boolean> doDefaultDocumentDownload(DocumentDownloadRequest request, FileOutputStream fos) throws Throwable {
        this.client.download(request,fos);
        return QiyuesuoCommonResult.build("0".toString()
                , ""
                , true
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Seal> doDefaultSealAutoCreate(SealAutoCreateRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<Seal> sdkResponse = JSONUtils.toQysResponse(response,Seal.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Object> doDefaultRoleManage(RoleManagementRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<Object> sdkResponse = JSONUtils.toQysResponse(response,Object.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Employee> doDefaultEmployeeRemove(EmployeeRemoveRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<Employee> sdkResponse = JSONUtils.toQysResponse(response,Employee.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Employee> doDefaultEmployeeCreate(EmployeeCreateRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<Employee> sdkResponse = JSONUtils.toQysResponse(response,Employee.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SealListResult> doDefaultSealList(SealListRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<SealListResult> sdkResponse = JSONUtils.toQysResponse(response,SealListResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<ContractPageResult> doDefaultdeContractPage(ContractPageRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<ContractPageResult> sdkResponse = JSONUtils.toQysResponse(response,ContractPageResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Contract> doDefaultContractDetail(ContractDetailRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<Contract> sdkResponse = JSONUtils.toQysResponse(response,Contract.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<String> doDefaultContractInvalid(ContractInvalidRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<String> sdkResponse = JSONUtils.toQysResponse(response);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Object> doDefaultCompanysign(ContractSignCompanyRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<Contract> sdkResponse = JSONUtils.toQysResponse(response, Contract.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<Contract> doDefaultSend(Contract contract){
        ContractDraftRequest request = new ContractDraftRequest(contract);
        String response = this.client.service(request);
        SdkResponse<Contract> sdkResponse = JSONUtils.toQysResponse(response, Contract.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<DocumentAddResult> doDefaultDocumentAddByTemplate(DocumentAddByTemplateRequest request) throws Throwable {
        String response = this.client.service(request);
        SdkResponse<DocumentAddResult> sdkResponse = JSONUtils.toQysResponse(response, DocumentAddResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSUserAuthResult> doSaasUserAuthResult(SaaSUserAuthResultRequest request) throws Throwable {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<SaaSSealSignAuthUrlResult> doSaasSealSignAuthUrl(SaaSSealSignAuthUrlRequest request) throws Throwable {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<SaaSPrivilegeUrlResult> doSaasPrivilegeUrl(SaasPrivilegeUrlRequest request) throws Throwable {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<SaaSUserAuthPageResult> doSaasUserAuthPage(SaaSUserAuthPageRequest request) throws Throwable {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<SaaSCompanyAuthPageResult> doSaasCompanyAuthPageUrl(SaasCompanyAuthPageUrlRequest request) throws Throwable {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSCompanyAuthPageResult> saasCompanyAuthPageUrl(String companyName,String applicantInfo, String legalPerson, String registerNo, StreamFile license) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSCompanyAuthPageResult> saasCompanyAuthPageUrl(String companyName, String applicantInfo) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact, String username, String idCardNo) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSPrivilegeUrlResult> saasPrivilegeUrl(Long companyId, String contact,List<String> privilegeModules) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSSealSignAuthUrlResult> saasSealSignAuthUrl(String sealAdminContract, Long companyId, String authDeadline, String remark) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthResult> saasUserAuthResult(String contact) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<DocumentAddResult> defaultDocumentAddByTemplate(Long contractId, Long templateId, List<TemplateParam> params, String title) {
                DocumentAddByTemplateRequest request =
                        new DocumentAddByTemplateRequest(contractId,templateId,params,title);
        return this.defaultDocumentAddByTemplate(request);
    }

    @Override
    public QiyuesuoCommonResult<Object> defaultCompanysign(Long contractId) {
        SignParam param = new SignParam();
        param.setContractId(contractId);
        ContractSignCompanyRequest request = new ContractSignCompanyRequest(param);
        return this.defaultCompanysign(request);
    }

    @Override
    public QiyuesuoCommonResult<Object> defaultCompanysign(Long contractId,
                                                           Long documentId,
                                                           Long seaLId,
                                                           List<String> keywords,
                                                           String dateKeyword) {
        Assert.notNull(contractId, "contractId不能为空");
        Assert.notNull(documentId, "documentId不能为空");
        Assert.notNull(seaLId, "seaLId不能为空");
        Assert.notEmpty(keywords, "keywords不能为空");
        SignParam param = new SignParam();
        param.setContractId(contractId);
        List<Stamper> stampers = ListUtil.list(false);
        for (int i = 0; i < keywords.size(); i++) {
            String keyword = keywords.get(i);
            Stamper stamper = new Stamper();
            stamper.setKeyword(keyword);
            stamper.setDocumentId(documentId);
            //目前默认公章
            stamper.setType("COMPANY");
            stamper.setSealId(seaLId);
            //纵坐标 / 关键字偏移量
            stamper.setOffsetY(-0.02 );
            stampers.add(stamper);
        }
        if (StrUtil.isNotBlank(dateKeyword)) {
            //这里签署日期直接写死，业务是这样
            Stamper dateSstamper = new Stamper();
//            dateSstamper.setKeyword("签订时间：");
            dateSstamper.setKeyword(dateKeyword);
            dateSstamper.setDocumentId(documentId);
            //所有关键字
            dateSstamper.setKeywordIndex(0);
            //时间戳
            dateSstamper.setType("TIMESTAMP");
            stampers.add(dateSstamper);
        }
        param.setSealId(seaLId);
        param.setStampers(stampers);
        ContractSignCompanyRequest request = new ContractSignCompanyRequest(param);
        return this.defaultCompanysign(request);
    }

    @Override
    public QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId, String reason) {
        ContractInvalidRequest request = new ContractInvalidRequest(contractId, reason);
        return this.defaultContractInvalid(request);
    }

    @Override
    public QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId, Long sealId, String reason) {
        ContractInvalidRequest request = new ContractInvalidRequest(contractId,sealId, reason,false);
        return this.defaultContractInvalid(request);
    }

    @Override
    public QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId) {
        ContractInvalidRequest request = new ContractInvalidRequest(contractId);
        return this.defaultContractInvalid(request);
    }

    @Override
    public QiyuesuoCommonResult<Contract> defaultContractDetail(Long contractId) {
        ContractDetailRequest request = new ContractDetailRequest(contractId);
        return this.defaultContractDetail(request);
    }

    @Override
    public QiyuesuoCommonResult<ContractPageResult> defaultdeContractPage(Long contractId, String contact) {
        ContractPageRequest request = new ContractPageRequest(contractId,
                new User(contact, "MOBILE"), "");
        return this.defaultdeContractPage(request);
    }

    @Override
    public QiyuesuoCommonResult<SealListResult> defaultSealList(String tenantName) {
        SealListRequest request = new SealListRequest();
        if (StrUtil.isNotBlank(tenantName)) {
            request.setTenantName(tenantName);
        }
        return this.defaultSealList(request);
    }

    @Override
    public QiyuesuoCommonResult<Employee> defaultEmployeeCreate(String name, String contact) {
        User user = new User(name, contact, "MOBILE");
        EmployeeCreateRequest request = new EmployeeCreateRequest(user, null);
        return this.defaultEmployeeCreate(request);
    }

    @Override
    public QiyuesuoCommonResult<Employee> defaultEmployeeRemove(String name, String contact) {
        User user = new User(name, contact, "MOBILE");
        EmployeeRemoveRequest request = new EmployeeRemoveRequest(user);
        return this.defaultEmployeeRemove(request);
    }

    @Override
    public QiyuesuoCommonResult<Object> defaultRoleManage(List<String> contacts) {
        Assert.notEmpty(contacts, "contacts不能为空");
        List<User> list = ListUtil.list(false);
        for (String contact : contacts) {
            list.add(new User(contact, "MOBILE"));
        }
        RoleManagementRequest request = new RoleManagementRequest("SEAL_ADMIN",list);
        return this.defaultRoleManage(request);
    }

    @Override
    public QiyuesuoCommonResult<Seal> defaultSealAutoCreate(String name, String enterpriseCode) {
        SealAutoCreateRequest request = new SealAutoCreateRequest();
        SealImageInfo sealInfo = new SealImageInfo();
        //目前只有公章
        sealInfo.setStyle("UNIVERSAL_SEAL");
        sealInfo.setEnterpriseCode(enterpriseCode);
        request.setName(name);
        request.setSealImageInfo(sealInfo);
        return this.defaultSealAutoCreate(request);
    }

    @Override
    public QiyuesuoCommonResult<Contract> defaultContractInvalidSign(Long contractId) {
        ContractSignInvalidRequest request = new ContractSignInvalidRequest();
        request.setContractId(contractId);
        return this.defaultContractInvalidSign(request);
    }

    @Override
    public CommonResult<Object> defaultRoleManageRemove(List<String> contacts) {
        Assert.notEmpty(contacts, "contacts不能为空");
        List<User> list = ListUtil.list(false);
        for (String contact : contacts) {
            list.add(new User(contact, "MOBILE"));
        }
        RoleManagementRequest request = new RoleManagementRequest("SEAL_ADMIN",list);
        request.setOperate("REMOVE");
        return this.defaultRoleManage(request);
    }


    @Override
    protected QiyuesuoCommonResult<Object> doDefaultContractSend(Long contractId) throws Throwable {
        ContractSendRequest request = new ContractSendRequest(contractId, Arrays.asList());
        String response = this.client.service(request);
        SdkResponse<Object> sdkResponse = JSONUtils.toQysResponse(response, Object.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }
}
