package com.newtouch.uctp.module.business.framework.rpc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import com.newtouch.uctp.module.bpm.api.openinvoice.BpmOpenInvoiceApi;
import com.newtouch.uctp.module.bpm.api.payfailed.PayFailedApi;
import com.newtouch.uctp.module.bpm.api.task.BpmProcessInstanceApi;
import com.newtouch.uctp.module.bpm.api.task.BpmTaskApi;
import com.newtouch.uctp.module.bpm.api.transfer.BpmCarTransferApi;
import com.newtouch.uctp.module.infra.api.file.FileApi;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dict.DictDataApi;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {FileApi.class, DictDataApi.class, DeptApi.class,
        AdminUserApi.class, BpmOpenInvoiceApi.class, BpmCarTransferApi.class,
        PayFailedApi.class, BpmTaskApi.class, BpmProcessInstanceApi.class})
public class RpcConfiguration {

}
