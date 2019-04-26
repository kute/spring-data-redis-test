package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/03/15 16:04
 */
@Service
public class ListCacheService extends BaseCacheService {

    public ListOperations<String, String> opsForList() {
        return lettuceRedisTemplate.opsForList();
    }
}
