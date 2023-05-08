package com.newtouch.uctp.cloud;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas.SaasQiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SaaSSdkClient;
import com.qiyuesuo.sdk.v2.bean.Company;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.SaaSCompanyDetailRequest;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SaaSPrivilegeUrlResult;
import com.qiyuesuo.sdk.v2.response.SaaSUserAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SdkResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UctpQIyuesuoSaasTests.class)
public class UctpQIyuesuoSaasTests {

//    @Resource
//    private QysConfigService qysConfigService;
    private static SaasQiyuesuoSaasClient client;

    @BeforeAll
    public static void before() {
        // 创建配置类
//        APP Token （服务商接入令牌） yzFRkW26cb
//        APP Secret （服务商接入秘钥） eiTI3RA2yaMBpwmCAZPiXmzewEZstT
//        Secret            cJmnww1NgTUJwjvE
        QiyuesuoChannelProperties properties = new QiyuesuoChannelProperties();
        properties.setId(2L);
        properties.setCode(QiyuesuoChannelEnum.DEFAULT.getCode());
        properties.setAccessKey("yzFRkW26cb");
        properties.setAccessSecret("eiTI3RA2yaMBpwmCAZPiXmzewEZstT");
        properties.setSecret("cJmnww1NgTUJwjvE");
        properties.setServerUrl("https://openapi.qiyuesuo.cn");
        client = new SaasQiyuesuoSaasClient(properties);
        client.init();
    }

    @Test
    void companyAuth() {
        String applicanInfo = "{\"name\":\"蒋嵩巍\",\"contact\": \"18080072255\",\"contactType\": \"MOBILE\"}";
        SaaSCompanyAuthPageResult checkedData = client.saasCompanyAuthPageUrl("666专业合作社", applicanInfo).getCheckedData();
        System.out.println(checkedData.getPageUrl());
    }

    @Test
    void personAuth() {
        QiyuesuoCommonResult<SaaSUserAuthPageResult> result = client.saasUserAuthPage("15196636618");
        System.out.println(result.getData().getAuthUrl());
        Assert.equals(result.getCode(), 0);
    }

    @Test
    void privilegeUrl() {
        SaaSPrivilegeUrlResult checkedData = client.saasPrivilegeUrl(3093084876115751320L, "18080072255",
                ListUtil.of()).getCheckedData();
        System.out.println(checkedData.getPageUrl());
    }
    @Test
    void companyDetail(){
        SaaSSdkClient saaSSdkClient = new SaaSSdkClient("yzFRkW26cb", "eiTI3RA2yaMBpwmCAZPiXmzewEZstT", "https://openapi.qiyuesuo.cn");
//        SdkClient sdkClient = new SdkClient("https://openapi.qiyuesuo.cn", "yzFRkW26cb", "eiTI3RA2yaMBpwmCAZPiXmzewEZstT");
// 公司信息
        SaaSCompanyDetailRequest companyDetailRequest = new SaaSCompanyDetailRequest();
        companyDetailRequest.setCompanyName("666专业合作社");
        String response = saaSSdkClient.service(companyDetailRequest);
        SdkResponse<Company> responseObj = JSONUtils.toQysResponse(response, Company.class);
        if(responseObj.getCode() == 0) {
            Company result = responseObj.getResult();
            System.out.println(JSONUtils.toJson(result));
        }
    }


}
