package com.xquant.example.appservice.config;

import com.xquant.example.appservice.controller.filter.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthTokenFilter authTokenFilter;

    @Bean
    public FilterRegistrationBean<AuthTokenFilter> authFilterRegistration() {
        FilterRegistrationBean<AuthTokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authTokenFilter);
        // 拦截所有路径
        registration.addUrlPatterns("/*");
        registration.setName("authTokenFilter");
        // 设置过滤器顺序
        registration.setOrder(1);
        return registration;
    }
}
