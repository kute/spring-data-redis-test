package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/03/15 16:04
 */
@Service
public class PubSubCacheService extends BaseCacheService {

    public void convertAndSend(String channel, Object message) {
        lettuceRedisTemplate.convertAndSend(channel, message);
    }

}
