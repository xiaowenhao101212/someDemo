package com.xquant.example.appservice.config;

import com.xquant.example.appservice.client.WebApiClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * restTemplate 初始化
 *
 * @author 05429
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(15))
                .build();
    }

    @Bean
    public WebApiClient webApiClient(RestTemplate restTemplate, AppProperties appProperties) {
        return new WebApiClient(restTemplate, appProperties.getWebApiIp());
    }
}
