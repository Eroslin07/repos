package com.newtouch.uctp.module.business.framework.rpc.config;

import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.system.api.dict.DictDataApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, DictDataApi.class})
public class RpcConfiguration {
}
