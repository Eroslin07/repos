package com.newtouch.uctp.framework.qiyuesuo;

import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.qys.DefaultQiyuesuoClient;
import com.newtouch.uctp.framework.qiyuesuo.core.property.QiyuesuoChannelProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UctpSpringBootStarterBizQiyuesuoApplicationTests {
    private static DefaultQiyuesuoClient client;

    @BeforeAll
    public static void before() {
        // 创建配置类
        QiyuesuoChannelProperties properties1 = new QiyuesuoChannelProperties();
//        SmsChannelProperties properties = new SmsChannelProperties();
//        properties.setId(1L);
//        properties.setSignature("Ballcat");
//        properties.setCode(SmsChannelEnum.ALIYUN.getCode());
//        properties.setApiKey(System.getenv("ALIYUN_ACCESS_KEY"));
//        properties.setApiSecret(System.getenv("ALIYUN_SECRET_KEY"));
        // 创建客户端
//        smsClient = new AliyunSmsClient(properties);
//        smsClient.init();
    }
	@Test
	void contextLoads() {
	}

}
