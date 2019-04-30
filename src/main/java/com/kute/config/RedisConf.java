package com.kute.config;

import com.kute.config.properties.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by bailong001 on 2019/04/29 12:10
 */
@Configuration
public class RedisConf {

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

}
