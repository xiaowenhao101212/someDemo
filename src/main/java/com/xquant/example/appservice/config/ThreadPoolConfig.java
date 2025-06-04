package com.xquant.example.appservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author 05429
 */
@Configuration
@RequiredArgsConstructor
public class ThreadPoolConfig {

    private final AppProperties appProperties;

    @Bean
    public ExecutorService executorService() {
        int corePoolSize = appProperties.getAppDemoThreadPool().getCorePoolSize();
        int maxPoolSize = appProperties.getAppDemoThreadPool().getMaxPoolSize();
        long keepAliveTime = appProperties.getAppDemoThreadPool().getKeepAliveTime();
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(appProperties.getAppDemoThreadPool().getQueueCapacity());

        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime, TimeUnit.SECONDS,
                workQueue,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
