package com.newtouch.uctp.module.business.framework.rpc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import com.newtouch.uctp.module.infra.api.file.FileApi;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = FileApi.class)
public class RpcConfiguration {
}
