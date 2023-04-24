package com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.AbstractQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.DefaultCodeMapping;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SaaSSdkClient;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.bean.TemplateParam;
import com.qiyuesuo.sdk.v2.bean.User;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.DocumentAddByTemplateRequest;
import com.qiyuesuo.sdk.v2.request.SaaSUserAuthPageRequest;
import com.qiyuesuo.sdk.v2.request.SaasCompanyAuthPageUrlRequest;
import com.qiyuesuo.sdk.v2.request.SaasPrivilegeUrlRequest;
import com.qiyuesuo.sdk.v2.response.*;

import java.util.Arrays;
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
    protected QiyuesuoCommonResult<Contract> doDefaultSend(Contract contract) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

    @Override
    protected QiyuesuoCommonResult<DocumentAddResult> doDefaultDocumentAddByTemplate(DocumentAddByTemplateRequest request) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
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
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact, String contactType) {
        Assert.notBlank(contact, "contact 不能为空");
        Assert.notBlank(contactType, "contactType 不能为空");
        SaaSUserAuthPageRequest request = new SaaSUserAuthPageRequest();
        User user = new User(contact, contactType);
        request.setUser(user);
        return this.saasUserAuthPage(request);
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact) {
        return this.saasUserAuthPage(contact, "MOBILE");
    }

    @Override
    public QiyuesuoCommonResult<SaaSPrivilegeUrlResult> saasPrivilegeUrl(Long companyId, String contact) {
        Assert.notNull(companyId, "companyId 不能为空");
        Assert.notBlank(contact, "contact 不能为空");
        SaasPrivilegeUrlRequest request = new SaasPrivilegeUrlRequest();
        User user = new User(contact, "MOBILE");
        request.setUser(user);
        request.setCompanyId(companyId);
        request.setCreateToken(true);
        request.setCallbackUrl("https://fssc.cloud:28000/app-api/uctp/qys/callback/certification");
        //TODO 成功后的地址需要商量
        request.setSuccessUrl("https://fssc.cloud:28000/");
        //目前只授权印章
        List<String> privilegeModules = Arrays.asList("SEAL");
        request.setPrivilegeModules(privilegeModules);
        return this.saasPrivilegeUrl(request);
    }

    @Override
    public QiyuesuoCommonResult<DocumentAddResult> defaultDocumentAddByTemplate(Long contractId, Long templateId, List<TemplateParam> params, String title) {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }

//    @Override
//    public QiyuesuoCommonResult<Object> defaultContractSend(Long contractId) {
//        throw new UnsupportedOperationException("saas的client不支持调用此方法");
//    }

    @Override
    protected QiyuesuoCommonResult<Object> doDefaultContractSend(Long contractId) throws Throwable {
        throw new UnsupportedOperationException("saas的client不支持调用此方法");
    }
}
