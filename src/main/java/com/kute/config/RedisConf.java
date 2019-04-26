package com.kute.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kute.config.listener.RedisExpirationListener;
import com.kute.config.properties.RedisProperties;
import com.kute.config.properties.cluster.RedisLettuceClusterProperties;
import com.kute.config.properties.normal.RedisLettuceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;

/**
 * created by bailong001 on 2019/02/18 15:37
 */
@Configuration
@EnableRedisRepositories
public class RedisConf implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConf.class);

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    /**
     * lettuce redis-cluster
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = {"nodes", "maxRedirects"}, prefix = "spring.redis.lettucecluster")
    @ConfigurationProperties(prefix = "spring.redis.lettucecluster")
    public RedisLettuceClusterProperties lettuceClusterProperties() {
        return new RedisLettuceClusterProperties();
    }

    /**
     * lettuce master-slave
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = {"host", "port"}, prefix = "spring.redis.lettuce")
    @ConfigurationProperties(prefix = "spring.redis.lettuce")
    public RedisLettuceProperties lettuceProperties() {
        return new RedisLettuceProperties();
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

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("redisClusterConfiguration", map));

        if (!Strings.isNullOrEmpty(redisLettuceClusterProperties.getPassword())) {
            redisClusterConfiguration.setPassword(RedisPassword.of(redisLettuceClusterProperties.getPassword()));
        }

        LettuceClientConfiguration clientPollConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisLettuceClusterProperties.getTimeout()))
                .poolConfig(redisLettuceClusterProperties.getPoolConfig())
                .build();

        return new LettuceConnectionFactory(redisClusterConfiguration, clientPollConfig);
    }

    @Bean
    public MessageListener redisExpirationListener() {
        return new RedisExpirationListener();
    }

    public void test() {
        redisTemplateForLettuceCluster().execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                LOGGER.info("Connection pSubscribe .....");
                connection.pSubscribe(redisExpirationListener(), new String("__key*__:*").getBytes());
                return null;
            }
        });
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

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(lettuceClusterConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
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

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        test();
    }
}
