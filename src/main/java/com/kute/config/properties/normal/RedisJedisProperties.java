package com.kute.config.properties.normal;

import com.kute.config.properties.RedisProperties;
import org.springframework.beans.factory.InitializingBean;

/**
 * created by bailong001 on 2019/02/18 17:14
 */
public class RedisJedisProperties extends RedisProperties implements InitializingBean {

    private String hostName;

    private Integer port;

    private Integer timeout;

    private String password;

    private Boolean usePool;

    private Boolean testWhileIdle = true;

    private Boolean testOnCreate = false;

    private Boolean testOnBorrow = false;

    private Boolean testOnReturn = false;

    private Integer maxTotal;

    private Integer minIdle;

    private Integer maxIdle;

    private Long maxWaitMillis = -1L;

    private Long minEvictableIdleTimeMillis = 1800000L;

    private Long softMinEvictableIdleTimeMillis = 1800000L;

    private boolean jmxEnabled = true;

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
