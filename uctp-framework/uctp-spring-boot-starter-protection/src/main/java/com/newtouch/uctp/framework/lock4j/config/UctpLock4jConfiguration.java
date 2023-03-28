package com.newtouch.uctp.framework.lock4j.config;

import cn.hutool.core.util.ClassUtil;
import com.newtouch.uctp.framework.lock4j.core.DefaultLockFailureStrategy;
import com.newtouch.uctp.framework.lock4j.core.Lock4jRedisKeyConstants;
import com.baomidou.lock.spring.boot.autoconfigure.LockAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureBefore(LockAutoConfiguration.class)
public class UctpLock4jConfiguration {

    static {
        // 手动加载 Lock4jRedisKeyConstants 类，因为它不会被使用到
        // 如果不加载，会导致 Redis 监控，看到它的 Redis Key 枚举
        ClassUtil.loadClass(Lock4jRedisKeyConstants.class.getName());
    }

    @Bean
    public DefaultLockFailureStrategy lockFailureStrategy() {
        return new DefaultLockFailureStrategy();
    }

}
