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
import com.qiyuesuo.sdk.v2.bean.User;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.SaaSUserAuthPageRequest;
import com.qiyuesuo.sdk.v2.request.SaasCompanyAuthPageUrlRequest;
import com.qiyuesuo.sdk.v2.request.SaasPrivilegeUrlRequest;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SaaSPrivilegeUrlResult;
import com.qiyuesuo.sdk.v2.response.SaaSUserAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SdkResponse;

public class SaasQiyuesuoClient extends AbstractQiyuesuoClient {
    /**
     * 契约锁SaaS模式客户端
     */
    private volatile SaaSSdkClient client;
    /**
     * 错误码枚举类
     */
//    protected final QiyuesuoCodeMapping codeMapping;

    public SaasQiyuesuoClient(QiyuesuoChannelProperties properties) {
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
    protected QiyuesuoCommonResult<Contract> doDraft(Contract contract) throws Throwable {
        return null;
    }

    @Override
    protected QiyuesuoCommonResult<SaaSPrivilegeUrlResult> doPrivilegeUrl(SaasPrivilegeUrlRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSPrivilegeUrlResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSPrivilegeUrlResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSUserAuthPageResult> doUserAuthPage(SaaSUserAuthPageRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSUserAuthPageResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSUserAuthPageResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSCompanyAuthPageResult> doCompanyAuthPageUrl(SaasCompanyAuthPageUrlRequest request) throws Throwable {
        String response = client.service(request);
        SdkResponse<SaaSCompanyAuthPageResult> sdkResponse = JSONUtils.toQysResponse(response, SaaSCompanyAuthPageResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }


    @Override
    public QiyuesuoCommonResult<SaaSCompanyAuthPageResult> companyAuthPageUrl(String companyName,
                                                                              String legalPerson,
                                                                              String registerNo,
                                                                              StreamFile license,
                                                                              String applicantInfo) {
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
        return this.companyAuthPageUrl(request);
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> userAuthPage(String contact, String contactType) {
        Assert.notBlank(contact, "contact 不能为空");
        Assert.notBlank(contactType, "contactType 不能为空");
        SaaSUserAuthPageRequest request = new SaaSUserAuthPageRequest();
        User user = new User(contact, contactType);
        request.setUser(user);
        return this.userAuthPage(request);
    }

    @Override
    public QiyuesuoCommonResult<SaaSPrivilegeUrlResult> privilegeUrl(Long companyId, String contact, String contactType) {
        Assert.notNull(companyId, "companyId 不能为空");
        Assert.notBlank(contact, "contact 不能为空");
        Assert.notBlank(contactType, "contactType 不能为空");
        SaasPrivilegeUrlRequest  request = new SaasPrivilegeUrlRequest ();
        User user = new User(contact, contactType);
        request.setUser(user);
        return this.privilegeUrl(request);
    }
}
