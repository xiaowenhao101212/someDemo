package com.xquant.example.appservice.annotation;

import java.lang.annotation.*;

/**
 * 防重复点击注解
 * @author 05429
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventDuplicateClick {

    /**
     * 重复点击的间隔时间（毫秒），默认1秒内不允许重复点击
     */
    long interval() default 1000;

    /**
     * 提示信息
     */
    String message() default "请勿重复点击";
}
