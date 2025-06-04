package com.xquant.example.appservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 05429
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String webApiIp = "172.19.4.87";

    private AuthProperties auth;

    private AppDemoThreadPoolProperties appDemoThreadPool;



    @Data
    public static class AuthProperties {
        // token key
        private String tokenName;
        // 过滤地址
        private String excludePaths;
    }

    @Data
    public static class AppDemoThreadPoolProperties {
        private int corePoolSize;
        private int maxPoolSize;
        private long keepAliveTime;
        private int queueCapacity;
        private String rejectedExecutionHandler;
    }
}
