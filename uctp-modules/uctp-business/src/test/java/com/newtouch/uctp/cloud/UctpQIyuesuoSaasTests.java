package com.newtouch.uctp.cloud;

import cn.hutool.core.lang.Assert;
import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoCommonResult;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.QiyuesuoClientFactoryImpl;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.saas.SaasQiyuesuoSaasClient;
import com.newtouch.uctp.framework.qiyuesuo.core.enums.QiyuesuoChannelEnum;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(value = QiyuesuoClientFactoryImpl.class)
@SpringBootTest(classes = UctpQIyuesuoSaasTests.class)
public class UctpQIyuesuoSaasTests {
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
        String applicanInfo = "{\"name\":\"韩立\",\"contact\": \"17396202169\",\"contactType\": \"MOBILE\"}";
        QiyuesuoCommonResult<SaaSCompanyAuthPageResult> result = client.saasCompanyAuthPageUrl("大米科技", applicanInfo);
        System.out.println(result);
        Assert.equals(result.getCode(),"0");
    }

}