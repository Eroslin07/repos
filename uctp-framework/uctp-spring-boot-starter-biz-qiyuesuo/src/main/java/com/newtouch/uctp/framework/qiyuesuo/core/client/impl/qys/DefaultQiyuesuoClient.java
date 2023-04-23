package com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys;

import cn.hutool.core.lang.Assert;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.AbstractQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.bean.TemplateParam;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.*;
import com.qiyuesuo.sdk.v2.response.*;

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
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact, String contactType) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> saasUserAuthPage(String contact) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<SaaSPrivilegeUrlResult> saasPrivilegeUrl(Long companyId, String contact) {
        throw new UnsupportedOperationException("default的client不支持调用此方法");
    }

    @Override
    public QiyuesuoCommonResult<DocumentAddResult> defaultDocumentAddByTemplate(Long contractId, Long templateId, List<TemplateParam> params, String title) {
                DocumentAddByTemplateRequest request =
                        new DocumentAddByTemplateRequest(contractId,templateId,params,title);
        return this.defaultDocumentAddByTemplate(request);
    }

    @Override
    protected QiyuesuoCommonResult<Object> doDefaultContractSend(Long contractId) throws Throwable {
        ContractSendRequest request = new ContractSendRequest(contractId, Arrays.asList());
        String response = this.client.service(request);
        SdkResponse<DocumentAddResult> sdkResponse = JSONUtils.toQysResponse(response, DocumentAddResult.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }
}
