package com.kute.redisson;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/04/30 15:27
 */
@Service
public class RedissonCacheService {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MyRedissonReactiveClient myRedissonReactiveClient;

    public void test() {
    }

}
