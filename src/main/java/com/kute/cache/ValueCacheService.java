package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/03/15 16:04
 */
@Service
public class ValueCacheService extends BaseCacheService {


    @Override
    public ValueOperations<String, String> opsForValue() {
        return lettuceRedisTemplate.opsForValue();
    }


}
