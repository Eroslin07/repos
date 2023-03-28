package com.newtouch.uctp.framework.sms.config;

import com.newtouch.uctp.framework.sms.core.client.SmsClientFactory;
import com.newtouch.uctp.framework.sms.core.client.impl.SmsClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 短信配置类
 *
 * @author 芋道源码
 */
@AutoConfiguration
public class UctpSmsAutoConfiguration {

    @Bean
    public SmsClientFactory smsClientFactory() {
        return new SmsClientFactoryImpl();
    }

}
