package com.kute.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kute.config.listener.RedisExpirationListener;
import com.kute.config.properties.RedisProperties;
import com.kute.config.properties.cluster.RedisLettuceClusterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.Duration;
import java.util.Map;

/**
 * created by bailong001 on 2019/02/18 15:37
 */
@Configuration
@ConditionalOnProperty(name = {"nodes", "maxRedirects"}, prefix = "spring.redis.lettucecluster")
@EnableRedisRepositories
public class RedisClusterConf implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterConf.class);

    /**
     * lettuce redis-cluster
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettucecluster")
    public RedisLettuceClusterProperties lettuceClusterProperties() {
        return new RedisLettuceClusterProperties();
    }

    @Bean
    @ConditionalOnBean(value = RedisLettuceClusterProperties.class)
    public LettuceConnectionFactory lettuceClusterConnectionFactory() {
        RedisLettuceClusterProperties redisLettuceClusterProperties = lettuceClusterProperties();
        LOGGER.info("Init redis lettuceClusterConnectionFactory with properties:{}", JSONObject.toJSONString(redisLettuceClusterProperties));

        Map<String, Object> map = Maps.newHashMap(
                ImmutableMap.of("spring.redis.cluster.nodes", redisLettuceClusterProperties.getNodes(),
                        "spring.redis.cluster.max-redirects", redisLettuceClusterProperties.getMaxRedirects())
        );

        RedisClusterConfiguration redisClusterConf = new RedisClusterConfiguration(new MapPropertySource("redisClusterConfiguration", map));

        if (!Strings.isNullOrEmpty(redisLettuceClusterProperties.getPassword())) {
            redisClusterConf.setPassword(RedisPassword.of(redisLettuceClusterProperties.getPassword()));
        }

        LettuceClientConfiguration clientPollConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisLettuceClusterProperties.getTimeout()))
                .poolConfig(redisLettuceClusterProperties.getPoolConfig())
                .build();

        return new LettuceConnectionFactory(redisClusterConf, clientPollConfig);
    }

    @Bean
    public MessageListener redisExpirationListener() {
        return new RedisExpirationListener();
    }

    @Bean
    @ConditionalOnBean(value = RedisLettuceClusterProperties.class)
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceClusterConnectionFactory());

        LOGGER.info("Init RedisMessageListenerContainer ...");
        /**
         * add listener
         */
        // 监听 0 号数据库的所有过期事件
//        container.addMessageListener(redisExpirationListener(), new PatternTopic("kuteC"));
        container.addMessageListener(redisExpirationListener(), new PatternTopic("__key*__:*"));
//        container.addMessageListener(redisExpirationListener(), new PatternTopic("__keyevent@*__:expired"));
        container.setErrorHandler(e -> LOGGER.error("redisMessageListenerContainer listener error", e));
        container.afterPropertiesSet();
        return container;
    }

    @Bean
    @ConditionalOnBean(value = RedisLettuceClusterProperties.class)
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener() {
        LOGGER.info("Init KeyExpirationEventMessageListener ......");
        return new KeyExpirationEventMessageListener(redisMessageListenerContainer());
    }


    /**
     * 线程安全
     */
    @Bean(name = "lettuceRedisTemplate")
    @ConditionalOnBean(value = RedisLettuceClusterProperties.class)
    public RedisTemplate<String, String> redisTemplateForLettuceCluster() {
        LOGGER.info("Init redisTemplateForLettuceCluster ...");
        return new StringRedisTemplate(lettuceClusterConnectionFactory());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
