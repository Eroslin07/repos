package com.newtouch.uctp.framework.idempotent.config;

import com.newtouch.uctp.framework.idempotent.core.aop.IdempotentAspect;
import com.newtouch.uctp.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import com.newtouch.uctp.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.newtouch.uctp.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.newtouch.uctp.framework.idempotent.core.redis.IdempotentRedisDAO;
import com.newtouch.uctp.framework.redis.config.UctpRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration
@AutoConfigureAfter(UctpRedisAutoConfiguration.class)
public class UctpIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
