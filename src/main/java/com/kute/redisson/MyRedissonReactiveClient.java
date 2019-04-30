package com.kute.redisson;

import org.redisson.RedissonReactive;
import org.redisson.config.Config;

/**
 * created by bailong001 on 2019/04/30 15:50
 */
public class MyRedissonReactiveClient extends RedissonReactive {
    protected MyRedissonReactiveClient(Config config) {
        super(config);
    }
}
