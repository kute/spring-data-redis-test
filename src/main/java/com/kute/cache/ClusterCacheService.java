package com.kute.cache;

import com.kute.support.BaseCacheService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2019/02/21 11:40
 *
 * 地理空间
 */
@Service
public class ClusterCacheService extends BaseCacheService {

    @Override
    public ClusterOperations<String, String> opsForCluster() {
        return lettuceRedisTemplate.opsForCluster();
    }

    public void test() {



        lettuceRedisTemplate.getConnectionFactory().getClusterConnection().clusterGetNodes().forEach(redisClusterNode -> {

        });

    }

}
