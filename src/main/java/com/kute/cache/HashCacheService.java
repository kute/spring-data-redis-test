package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/03/15 16:04
 */
@Service
public class HashCacheService extends BaseCacheService {

    @Override
    public HashOperations<String, String, String> opsForHash() {
        return lettuceRedisTemplate.opsForHash();
    }

}
