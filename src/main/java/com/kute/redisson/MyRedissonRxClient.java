package com.kute.redisson;

import org.redisson.RedissonRx;
import org.redisson.config.Config;

/**
 * created by bailong001 on 2019/04/30 17:16
 */
public class MyRedissonRxClient extends RedissonRx {
    protected MyRedissonRxClient(Config config) {
        super(config);
    }
}
