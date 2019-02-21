package com.kute.common.cache.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kute.common.cache.config.properties.RedisProperties;
import com.kute.common.cache.config.properties.cluster.RedisJedisClusterProperties;
import com.kute.common.cache.config.properties.cluster.RedisLettuceClusterProperties;
import com.kute.common.cache.config.properties.normal.RedisJedisProperties;
import com.kute.common.cache.config.properties.normal.RedisLettuceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * created by bailong001 on 2019/02/18 15:37
 */
@Configuration
public class RedisConf {

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

    /**
     * jedis redis-cluster
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = {}, prefix = "spring.redis.jediscluster")
    @ConfigurationProperties(prefix = "spring.redis.jediscluster")
    public RedisJedisClusterProperties jedisClusterProperties() {
        return new RedisJedisClusterProperties();
    }

    /**
     * jedis master-slave
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = {}, prefix = "spring.redis.jedis")
    @ConfigurationProperties(prefix = "spring.redis.jedis")
    public RedisJedisProperties jedisProperties() {
        return new RedisJedisProperties();
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
                .commandTimeout(Duration.ofMillis(redisLettuceProperties.getTimeout()))
                .poolConfig(redisLettuceProperties.getPoolConfig())
                .build();

        return new LettuceConnectionFactory(configuration, clientPollConfig);
    }

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

    @Bean
    @ConditionalOnBean(value = RedisJedisProperties.class)
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        return jedisConnectionFactory;
    }





}
