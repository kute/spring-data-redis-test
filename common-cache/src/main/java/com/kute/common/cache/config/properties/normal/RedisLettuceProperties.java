package com.kute.common.cache.config.properties.normal;

import com.kute.common.cache.config.properties.RedisProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;

/**
 * created by bailong001 on 2018/10/08 15:55
 */
public class RedisLettuceProperties extends RedisProperties implements InitializingBean {

    private String host;

    private Integer port;

    private String password;

    private Long timeout;

    private GenericObjectPoolConfig poolConfig;

    public String getHost() {
        return host;
    }

    public RedisLettuceProperties setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public RedisLettuceProperties setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisLettuceProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public RedisLettuceProperties setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public RedisLettuceProperties setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
