package com.xquant.example.appservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private AuthProperties auth;

    @Data
    public static class AuthProperties {
        // token key
        private String tokenName;
        // 过滤地址
        private String excludePaths;
    }
}
