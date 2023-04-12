package com.newtouch.uctp.module.system.framework.rpc.config;

import com.newtouch.uctp.module.business.api.file.BusinessFileApi;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, BusinessFileApi.class})
public class RpcConfiguration {
}
