package com.kute.config.properties.cluster;

import com.kute.config.properties.RedisProperties;
import org.springframework.beans.factory.InitializingBean;

/**
 * created by bailong001 on 2019/02/18 17:35
 */
public class RedisJedisClusterProperties extends RedisProperties implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
