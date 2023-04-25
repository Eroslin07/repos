package com.newtouch.uctp.cloud;

import cn.hutool.core.lang.Assert;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas.SaasQiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.newtouch.uctp.module.business.service.qys.QysConfigService;
import com.qiyuesuo.sdk.v2.request.SaasPrivilegeUrlRequest;
import com.qiyuesuo.sdk.v2.response.SaaSPrivilegeUrlResult;
import com.qiyuesuo.sdk.v2.response.SaaSUserAuthPageResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileNotFoundException;

@SpringBootTest
public class UctpQIyuesuoSaasTests {

    @Resource
    private QysConfigService qysConfigService;
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
//        String applicanInfo = "{\"name\":\"阿卡丽\",\"contact\": \"17396202169\",\"contactType\": \"MOBILE\"}";
//        QiyuesuoCommonResult<SaaSCompanyAuthPageResult> result = client.saasCompanyAuthPageUrl("拳头科技公司", applicanInfo);
//        System.out.println(result.getData().getPageUrl());
//        Assert.equals(result.getCode(),0);
        try {
            qysConfigService.companyAuth(267L);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void personAuth() {
        QiyuesuoCommonResult<SaaSUserAuthPageResult> result = client.saasUserAuthPage("17396202169");
        System.out.println(result.getData().getAuthUrl());
        Assert.equals(result.getCode(),0);
    }

    @Test
    void privilegeUrl() {
        SaasPrivilegeUrlRequest urlRequest = new SaasPrivilegeUrlRequest();
        QiyuesuoCommonResult<SaaSPrivilegeUrlResult> result = client.saasPrivilegeUrl(3088322841008022468L, "17380123816");
        System.out.println(result.getData().getPageUrl());
        Assert.equals(result.getCode(),0);
    }


}
