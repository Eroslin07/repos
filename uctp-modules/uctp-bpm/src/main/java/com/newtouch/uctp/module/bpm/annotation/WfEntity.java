package com.newtouch.uctp.module.bpm.annotation;

import java.lang.annotation.*;

/**
 * @author he long
 * @date 2023/4/8 16:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WfEntity {
    /**
     * 流程表实体类映射别名
     * @return
     */
    String alias() default "";

    /**
     * 指定实体类对应的Mapper类
     * @return
     */
    Class mapperClass();

    /**
     * 是否流程主实体
     * @return
     */
    boolean isMainEntity() default false;
}
