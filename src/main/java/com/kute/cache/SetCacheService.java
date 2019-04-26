package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/03/15 16:04
 */
@Service
public class SetCacheService extends BaseCacheService {

    @Override
    public SetOperations<String, String> opsForSet() {
        return lettuceRedisTemplate.opsForSet();
    }

}
