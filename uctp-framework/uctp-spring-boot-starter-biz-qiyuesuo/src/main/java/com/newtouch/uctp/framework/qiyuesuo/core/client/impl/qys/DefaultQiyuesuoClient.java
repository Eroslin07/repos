package com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys;

import cn.hutool.core.lang.Assert;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.AbstractQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.ContractDraftRequest;
import com.qiyuesuo.sdk.v2.request.SaaSUserAuthPageRequest;
import com.qiyuesuo.sdk.v2.request.SaasCompanyAuthPageUrlRequest;
import com.qiyuesuo.sdk.v2.request.SaasPrivilegeUrlRequest;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SaaSPrivilegeUrlResult;
import com.qiyuesuo.sdk.v2.response.SaaSUserAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SdkResponse;

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
    protected QiyuesuoCommonResult<Contract> doDraft(Contract contract){
        ContractDraftRequest request = new ContractDraftRequest(contract);
        String response = this.client.service(request);
        System.out.println(response);
        SdkResponse<Contract> sdkResponse = JSONUtils.toQysResponse(response, Contract.class);
        return QiyuesuoCommonResult.build(sdkResponse.getCode().toString()
                , sdkResponse.getMessage()
                , sdkResponse.getResult()
                , codeMapping);
    }

    @Override
    protected QiyuesuoCommonResult<SaaSPrivilegeUrlResult> doPrivilegeUrl(SaasPrivilegeUrlRequest request) throws Throwable {
        return null;
    }

    @Override
    protected QiyuesuoCommonResult<SaaSUserAuthPageResult> doUserAuthPage(SaaSUserAuthPageRequest request) throws Throwable {
        return null;
    }

    @Override
    protected QiyuesuoCommonResult<SaaSCompanyAuthPageResult> doCompanyAuthPageUrl(SaasCompanyAuthPageUrlRequest request) throws Throwable {
        return null;
    }

    @Override
    public QiyuesuoCommonResult<SaaSCompanyAuthPageResult> companyAuthPageUrl(String companyName, String legalPerson, String registerNo, StreamFile license, String applicantInfo) {
        return null;
    }

    @Override
    public QiyuesuoCommonResult<SaaSUserAuthPageResult> userAuthPage(String contact, String contactType) {
        return null;
    }

    @Override
    public QiyuesuoCommonResult<SaaSPrivilegeUrlResult> privilegeUrl(Long companyId, String contact, String contactType) {
        return null;
    }
}
