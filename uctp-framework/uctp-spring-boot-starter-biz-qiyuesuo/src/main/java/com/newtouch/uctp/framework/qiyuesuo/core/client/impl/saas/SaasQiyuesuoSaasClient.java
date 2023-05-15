package com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.AbstractQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.DefaultCodeMapping;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SaaSSdkClient;
import com.qiyuesuo.sdk.v2.bean.*;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.*;
import com.qiyuesuo.sdk.v2.response.*;

import java.io.FileOutputStream;
import java.util.List;
public class SaasQiyuesuoSaasClient extends AbstractQiyuesuoClient {
    /**
     * 契约锁SaaS模式客户端
     */
    private volatile SaaSSdkClient client;
    /**
     * 错误码枚举类
     */
//    protected final QiyuesuoCodeMapping codeMapping;

    public SaasQiyuesuoSaasClient(QiyuesuoChannelProperties properties) {
        super(properties, new DefaultCodeMapping());
//        this.codeMapping = codeMapping;
        Assert.notEmpty(properties.getAccessKey(), "key 不能为空");
        Assert.notEmpty(properties.getAccessSecret(), "secret 不能为空");
    }

    @Override
    protected void doInit() {
        client = new SaaSSdkClient(properties.getAccessKey(), properties.getAccessSecret(),properties.getServerUrl());
    }

    @Override
    protected QiyuesuoCommonResult<Boolean> doDefaultContractDownload(ContractDownloadRequest request, FileOutputStream fos) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Boolean> doDefaultDocumentDownload(DocumentDownloadRequest request, FileOutputStream fos) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }


    @Override
    protected QiyuesuoCommonResult<Seal> doDefaultSealAutoCreate(SealAutoCreateRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Employee> doDefaultEmployeeRemove(EmployeeRemoveRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Employee> doDefaultEmployeeCreate(EmployeeCreateRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<SealListResult> doDefaultSealList(SealListRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<ContractPageResult> doDefaultdeContractPage(ContractPageRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Contract> doDefaultContractDetail(ContractDetailRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<String> doDefaultContractInvalid(ContractInvalidRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Object> doDefaultCompanysign(ContractSignCompanyRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Contract> doDefaultSend(Contract contract) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<DocumentAddResult> doDefaultDocumentAddByTemplate(DocumentAddByTemplateRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<SaaSUserAuthResult> doSaasUserAuthResult(SaaSUserAuthResultRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSUserAuthResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSUserAuthResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSSealSignAuthUrlResult> doSaasSealSignAuthUrl(SaaSSealSignAuthUrlRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSSealSignAuthUrlResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSSealSignAuthUrlResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSPrivilegeUrlResult> doSaasPrivilegeUrl(SaasPrivilegeUrlRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSPrivilegeUrlResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSPrivilegeUrlResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSUserAuthPageResult> doSaasUserAuthPage(SaaSUserAuthPageRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSUserAuthPageResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSUserAuthPageResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSCompanyAuthPageResult> doSaasCompanyAuthPageUrl(SaasCompanyAuthPageUrlRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSCompanyAuthPageResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSCompanyAuthPageResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }


    @Override
    public QiyuesuoCommonResult<SaaSCompanyAuthPageResult> saasCompanyAuthPageUrl(String companyName,
                                                                                  String applicantInfo,
                                                                              String legalPerson,
                                                                              String registerNo,
                                                                              StreamFile license) {
        SaasCompanyAuthPageUrlRequest request = new SaasCompanyAuthPageUrlRequest();
        Assert.notBlank(companyName, "companyName 不能为空");
        Assert.notBlank(applicantInfo, "applicantInfo 不能为空");
        request.setCompanyName(companyName);
        request.setApplicantInfo(applicantInfo);
        if (StrUtil.isNotBlank(legalPerson)) {
            request.setLegalPerson(legalPerson);
        }
        if (ObjectUtil.isNotNull(license)) {
            request.setLicense(license);
        }
        if (StrUtil.isNotBlank(registerNo)) {
            request.setRegisterNo(registerNo);
        }
        request.setCallbackUrl("https://fssc.cloud:28000/app-api/uctp/qys/callback/verification");
        return this.saasCompanyAuthPageUrl(request);
    }

    @Override
    public QiyuesuoCommonResult<SaaSCompanyAuthPageResult> saasCompanyAuthPageUrl(String companyName, String applicantInfo) {
        return this.saasCompanyAuthPageUrl(companyName,applicantInfo,null,null,null);
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact,
                                                                         String username,
                                                                         String idCardNo) {
        Assert.notBlank(contact, "contact 不能为空");
        SaaSUserAuthPageRequest request = new SaaSUserAuthPageRequest();
        User user = new User(contact, "MOBILE");
        request.setUser(user);
        request.setIdCardNo(idCardNo);
        request.setUsername(username);
        request.setCallbackUrl("https://fssc.cloud:28000/app-api/uctp/qys/callback/certification/person");
        return this.saasUserAuthPage(request);
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact) {
        return this.saasUserAuthPage(contact,null,null);
    }

    @Override
    public QiyuesuoCommonResult<SaaSPrivilegeUrlResult> saasPrivilegeUrl(Long companyId,
                                                                         String contact,
                                                                         List<String> privilegeModules) {
        Assert.notNull(companyId, "companyId 不能为空");
        Assert.notBlank(contact, "contact 不能为空");
        SaasPrivilegeUrlRequest request = new SaasPrivilegeUrlRequest();
        User user = new User(contact, "MOBILE");
        request.setUser(user);
        request.setCompanyId(companyId);
        request.setCreateToken(true);
        request.setCallbackUrl("https://fssc.cloud:28000/app-api/uctp/qys/callback/privilege");
        //TODO 成功后的地址需要商量
        request.setSuccessUrl("");
        //授权印章,合同
        request.setPrivilegeModules(privilegeModules);
        //很关键，这里要重新生成，不然后合同收不到回调
        request.setCreateToken(Boolean.TRUE);
        return this.saasPrivilegeUrl(request);
    }

    @Override
    public QiyuesuoCommonResult<SaaSSealSignAuthUrlResult> saasSealSignAuthUrl(String sealAdminContract, Long companyId, String authDeadline, String remark) {
        Assert.notNull(companyId, "companyId 不能为空");
        Assert.notBlank(sealAdminContract, "sealAdminContract 不能为空");
        Assert.notBlank(authDeadline, "authDeadline 不能为空");
        Assert.notBlank(remark, "remark 不能为空");
        SaaSSealSignAuthUrlRequest request = new SaaSSealSignAuthUrlRequest();
        request.setSealAdmin(new User(sealAdminContract, "MOBILE"));
        request.setCompany(new Company(companyId));
        request.setAuthDeadline(authDeadline);
        request.setRemark(remark);
        request.setCallbackUrl("https://fssc.cloud:28000/app-api/uctp/qys/callback/privilege");
//        request.setAppId();
        request.setReturnUrl("https://fssc.cloud:28000/");
        return this.saasSealSignAuthUrl(request);
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthResult> saasUserAuthResult(String contact) {
        Assert.notBlank(contact, "contact 不能为空");
        User user = new User(contact, "MOBILE");
        SaaSUserAuthResultRequest request = new SaaSUserAuthResultRequest(user);
        return this.saasUserAuthResult(request);
    }

    @Override
    public QiyuesuoCommonResult<DocumentAddResult> defaultDocumentAddByTemplate(Long contractId, Long templateId, List<TemplateParam> params, String title) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<Object> defaultCompanysign(Long contractId) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<Object> defaultCompanysign(Long contractId,
                                                           Long documentId,
                                                           Long seaLId,
                                                           List<String> keywords,
                                                           String dateKeyword) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId, String reason) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId, Long sealId, String reason) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<String> defaultContractInvalid(Long contractId) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<Contract> defaultContractDetail(Long contractId) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<ContractPageResult> defaultdeContractPage(Long contractId, String contact) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SealListResult> defaultSealList(String tenantName) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<Employee> defaultEmployeeCreate(String name, String contact) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<Employee> defaultEmployeeRemove(String name, String contact) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<Object> defaultRoleManage(List<String> contacts) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<Seal> defaultSealAutoCreate(String name, String enterpriseCode) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Object> doDefaultRoleManage(RoleManagementRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<Object> doDefaultContractSend(Long contractId) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }
}
