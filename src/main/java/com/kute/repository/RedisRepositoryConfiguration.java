package com.kute.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * created by bailong001 on 2019/04/26 18:15
 */
@EnableRedisRepositories(basePackages = {"com.kute.repository.details"})
@Configuration
public class RedisRepositoryConfiguration {
}
