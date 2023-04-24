package com.newtouch.uctp.framework.qiyuesuo.config;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.QiyuesuoClientFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class UctpQiyuesuoAutoConfiguration {

    @Value("${qys.server-url}")
    private String serverUrl;
    @Bean
    public QiyuesuoClientFactory smsClientFactory() {
        return new QiyuesuoClientFactoryImpl(serverUrl);
    }

}
