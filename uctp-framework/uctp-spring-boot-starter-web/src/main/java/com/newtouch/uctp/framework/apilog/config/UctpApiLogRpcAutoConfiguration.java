package com.newtouch.uctp.framework.apilog.config;

import com.newtouch.uctp.module.infra.api.logger.ApiAccessLogApi;
import com.newtouch.uctp.module.infra.api.logger.ApiErrorLogApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * API 日志使用到 Feign 的配置项
 *
 * @author 芋道源码
 */
@AutoConfiguration
@EnableFeignClients(clients = {ApiAccessLogApi.class, // 主要是引入相关的 API 服务
        ApiErrorLogApi.class})
public class UctpApiLogRpcAutoConfiguration {
}
