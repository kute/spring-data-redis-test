package com.kute.config;

import com.google.common.base.Strings;
import com.kute.config.properties.normal.RedisLettuceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.Duration;

/**
 * created by bailong001 on 2019/04/29 12:05
 */
@Configuration
@ConditionalOnProperty(name = {"host", "port"}, prefix = "spring.redis.lettuce")
@EnableRedisRepositories
public class RedisMaterConf implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMaterConf.class);

    /**
     * lettuce master-slave
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce")
    public RedisLettuceProperties lettuceProperties() {
        return new RedisLettuceProperties();
    }

    @Bean
    @ConditionalOnBean(value = RedisLettuceProperties.class)
    public LettuceConnectionFactory lettuceConnectionFactory() {

        RedisLettuceProperties redisLettuceProperties = lettuceProperties();

        LOGGER.info("Init lettuceConnectionFactory ...");

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisLettuceProperties.getHost());
        configuration.setPort(redisLettuceProperties.getPort());
        if (!Strings.isNullOrEmpty(redisLettuceProperties.getPassword())) {
            configuration.setPassword(RedisPassword.of(redisLettuceProperties.getPassword()));
        }

        LettuceClientConfiguration clientPollConfig = LettucePoolingClientConfiguration.builder()
//                .readFrom()  // 设置  主写 从读
                .commandTimeout(Duration.ofMillis(redisLettuceProperties.getTimeout()))
                .poolConfig(redisLettuceProperties.getPoolConfig())
                .build();

        return new LettuceConnectionFactory(configuration, clientPollConfig);
    }

    /**
     * 线程安全
     */
    @Bean(name = "lettuceRedisTemplate")
    @ConditionalOnBean(value = RedisLettuceProperties.class)
    public RedisTemplate<String, String> redisTemplateForLettuce() {
        LOGGER.info("Init redisTemplateForLettuce ...");
        return new StringRedisTemplate(lettuceConnectionFactory());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
