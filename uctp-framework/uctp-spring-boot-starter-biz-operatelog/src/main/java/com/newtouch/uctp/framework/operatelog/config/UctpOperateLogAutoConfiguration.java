package com.newtouch.uctp.framework.operatelog.config;

import com.newtouch.uctp.framework.operatelog.core.aop.OperateLogAspect;
import com.newtouch.uctp.framework.operatelog.core.service.OperateLogFrameworkService;
import com.newtouch.uctp.framework.operatelog.core.service.OperateLogFrameworkServiceImpl;
import com.newtouch.uctp.module.system.api.logger.OperateLogApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class UctpOperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    public OperateLogFrameworkService operateLogFrameworkService(OperateLogApi operateLogApi) {
        return new OperateLogFrameworkServiceImpl(operateLogApi);
    }

}
