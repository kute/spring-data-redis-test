package com.kute.common.cache.support;

import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * created by bailong001 on 2018/10/08 18:22
 */
@Component
public class BaseCacheService {

    @Resource(name = "lettuceRedisTemplate")
    protected RedisTemplate<String, String> lettuceRedisTemplate;

    public String getKV(String key) {
        return lettuceRedisTemplate.opsForValue().get(key);
    }

    public void setKV(String key, String value) {
        opsForValue().set(key, value);
    }

    public void setKV(String key, String value, Long time, TimeUnit timeUnit) {
        opsForValue().set(key, value, time, timeUnit);
    }

    public Boolean zadd(String key, String value, Double score) {
        return opsForZSet().add(key, value, score);
    }

    public Long zaddBatch(String key, Set<ZSetOperations.TypedTuple<String>> tupleSet) {
        return opsForZSet().add(key, tupleSet);
    }

    public Long del(List<String> keyList) {
        return lettuceRedisTemplate.delete(keyList);
    }

    public Boolean del(String key) {
        return lettuceRedisTemplate.delete(key);
    }

    public Boolean exists(String key) {
        return lettuceRedisTemplate.hasKey(key);
    }

    public Set<String> keys(String pattern) {
        return lettuceRedisTemplate.keys(pattern);
    }

    public void setNullKey(String nullKey) {
        opsForValue().set(nullKey, "1", 7, TimeUnit.DAYS);
    }

    public void setNullKey(String nullKey, Long time, TimeUnit timeUnit) {
        opsForValue().set(nullKey, "1", time, timeUnit);
    }

    public ValueOperations<String, String> opsForValue() {
        return lettuceRedisTemplate.opsForValue();
    }

    public HashOperations<String, String, String> opsForHash() {
        return lettuceRedisTemplate.opsForHash();
    }

    public ZSetOperations<String, String> opsForZSet() {
        return lettuceRedisTemplate.opsForZSet();
    }

    public SetOperations<String, String> opsForSet() {
        return lettuceRedisTemplate.opsForSet();
    }

    public ClusterOperations<String, String> opsForCluster() {
        return lettuceRedisTemplate.opsForCluster();
    }

    public RedisSerializer<?> getKeySerializer() {
        return lettuceRedisTemplate.getKeySerializer();
    }

    public RedisSerializer<?> getHashKeySerializer() {
        return lettuceRedisTemplate.getHashKeySerializer();
    }

    public RedisSerializer<?> getHashValueSerializer() {
        return lettuceRedisTemplate.getHashValueSerializer();
    }

    public byte[] rawKey(Object key) {
        final RedisSerializer serializer = getKeySerializer();
        if (serializer == null && (key instanceof byte[])) {
            return (byte[]) (byte[]) key;
        }
        return serializer.serialize(key);
    }

    public byte[] rawHashKey(Object key) {
        final RedisSerializer serializer = getHashKeySerializer();
        if (serializer == null && (key instanceof byte[])) {
            return (byte[]) (byte[]) key;
        }
        return serializer.serialize(key);
    }

    public byte[] rawHashValue(Object key) {
        final RedisSerializer serializer = getHashValueSerializer();
        if (serializer == null && (key instanceof byte[])) {
            return (byte[]) (byte[]) key;
        }
        return serializer.serialize(key);
    }

    public byte[] rawValue(Object value) {
        final RedisSerializer serializer = lettuceRedisTemplate.getValueSerializer();
        if (serializer == null && (value instanceof byte[])) {
            return (byte[]) (byte[]) value;
        }
        return serializer.serialize(value);
    }

    public void expired(String key, long time, TimeUnit timeUnit) {
        lettuceRedisTemplate.expire(key, time, timeUnit);
    }

}
