package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/03/15 16:04
 */
@Service
public class ZSetCacheService extends BaseCacheService {

    @Override
    public ZSetOperations<String, String> opsForZSet() {
        return lettuceRedisTemplate.opsForZSet();
    }

}
