package com.kute.redisson;

import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by bailong001 on 2019/04/30 15:47
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
public class RedissonConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonConf.class);

    @Bean
    @ConditionalOnMissingBean(RedissonReactiveClient.class)
    public MyRedissonReactiveClient myRedissonReactiveClient(RedissonClient redissonClient) {
        LOGGER.info("Init redissonReactiveClient ...");
        Config config = redissonClient.getConfig();
        return new MyRedissonReactiveClient(config);
    }

    @Bean
    @ConditionalOnMissingBean(RedissonReactiveClient.class)
    public MyRedissonRxClient myRedissonRxClient(RedissonClient redissonClient) {
        LOGGER.info("Init redissonRxClient ...");
        Config config = redissonClient.getConfig();
        return new MyRedissonRxClient(config);
    }

}
