package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/02/21 11:40
 *
 * 地理空间
 */
@Service
public class GeoCacheService extends BaseCacheService {

    public GeoOperations<String, String> opsForGeo() {
        return lettuceRedisTemplate.opsForGeo();
    }

}
