package com.newtouch.uctp.cloud;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas.SaasQiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.SaaSSdkClient;
import com.qiyuesuo.sdk.v2.bean.Company;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.SaaSCompanyDetailRequest;
import com.qiyuesuo.sdk.v2.response.*;
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
    void personAuth() {
        SaaSUserAuthPageResult data = client.saasUserAuthPage("19950180091").getCheckedData();
        System.out.println(data.getAuthUrl());
        System.out.println(data.getAuthId());
    }

    @Test
    void companyAuth() {
        String applicanInfo = "{\"name\":\"蒋嵩巍\",\"contact\": \"18080072255\",\"contactType\": \"MOBILE\"}";
        SaaSCompanyAuthPageResult checkedData = client.saasCompanyAuthPageUrl("666专业合作社", applicanInfo).getCheckedData();
        System.out.println(checkedData.getPageUrl());
    }


    @Test
    void privilegeUrl() {
        SaaSPrivilegeUrlResult checkedData = client.saasPrivilegeUrl(3093441290910712532L, "15037580053",
                ListUtil.of("SEAL","TEMPLATE","CONTRACT","COMPANY_EMPLOYEE","ROLE_PERMISSION",
                        "BASE_INFO","FILE_STATISTICS","CATEGORY","FEE","COMPANY_SETUP")).getCheckedData();
        System.out.println(checkedData.getPageUrl());
    }

    @Test
    void sealSignAuthUrl() {
        DateTime dateTime = DateUtil.offset(DateUtil.date(), DateField.YEAR, 5);
        String date = DateUtil.formatDate(dateTime);
        SaaSSealSignAuthUrlResult checkedData = client.saasSealSignAuthUrl("15196636618", 3091676548563157410L, date, "啊哈").getCheckedData();
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

    @Test
    void test(){
        DateTime authDeadline = DateUtil.offset(DateUtil.date(), DateField.YEAR, 5);
        String formatDate = DateUtil.formatDate(authDeadline);
        System.out.println(formatDate);

    }

}
