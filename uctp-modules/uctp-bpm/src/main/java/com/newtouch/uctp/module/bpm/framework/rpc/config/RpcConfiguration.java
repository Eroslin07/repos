package com.newtouch.uctp.module.bpm.framework.rpc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import com.newtouch.uctp.module.business.api.account.AccountApi;
import com.newtouch.uctp.module.business.api.account.AccountProfitApi;
import com.newtouch.uctp.module.business.api.contract.MerchantMoneyApi;
import com.newtouch.uctp.module.business.api.file.notice.NoticeApi;
import com.newtouch.uctp.module.business.api.qys.QysConfigApi;
import com.newtouch.uctp.module.business.api.transfer.CarTransferApi;
import com.newtouch.uctp.module.system.api.dept.DeptApi;
import com.newtouch.uctp.module.system.api.dept.PostApi;
import com.newtouch.uctp.module.system.api.dict.DictDataApi;
import com.newtouch.uctp.module.system.api.permission.RoleApi;
import com.newtouch.uctp.module.system.api.sms.SmsSendApi;
import com.newtouch.uctp.module.system.api.tenant.TenantApi;
import com.newtouch.uctp.module.system.api.user.AdminUserApi;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {RoleApi.class, DeptApi.class, PostApi.class, AdminUserApi.class,
        SmsSendApi.class, DictDataApi.class, NoticeApi.class, QysConfigApi.class,
        CarTransferApi.class, TenantApi.class, AccountProfitApi.class, AccountApi.class,
        MerchantMoneyApi.class})
public class RpcConfiguration {

}
