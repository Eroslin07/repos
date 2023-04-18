package com.newtouch.uctp.framework.qiyuesuo.config;

import com.newtouch.uctp.framework.qiyuesuo.core.client.QiyuesuoClientFactory;
import com.newtouch.uctp.framework.qiyuesuo.core.client.impl.QiyuesuoClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class UctpQiyuesuoAutoConfiguration {
    @Bean
    public QiyuesuoClientFactory smsClientFactory() {
        return new QiyuesuoClientFactoryImpl();
    }

}
