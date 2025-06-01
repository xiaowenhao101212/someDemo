package com.xquant.example.appservice.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(RedisProperties props) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + props.getHost() + ":" + props.getPort())
                .setPassword(props.getPassword())
                .setDatabase(props.getDatabase())
                // 连接池大小
                .setConnectionPoolSize(64)
                // 最小空闲连接:cite[4]
                .setConnectionMinimumIdleSize(10);
        // Jackson 序列化
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
