package com.newtouch.uctp.module.business.framework.rpc.config;

import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dict.DictDataApi;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, DictDataApi.class, DeptApi.class, AdminUserApi.class})
public class RpcConfiguration {
}
