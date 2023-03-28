package com.newtouch.uctp.framework.env.config;

import com.newtouch.uctp.framework.common.enums.WebFilterOrderEnum;
import com.newtouch.uctp.framework.env.core.web.EnvWebFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 多环境的 Web 组件的自动配置
 *
 * @author 芋道源码
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(EnvProperties.class)
public class UctpEnvWebAutoConfiguration {

    /**
     * 创建 {@link EnvWebFilter} Bean
     */
    @Bean
    public FilterRegistrationBean<EnvWebFilter> envWebFilterFilter() {
        EnvWebFilter filter = new EnvWebFilter();
        FilterRegistrationBean<EnvWebFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(WebFilterOrderEnum.ENV_TAG_FILTER);
        return bean;
    }

}
