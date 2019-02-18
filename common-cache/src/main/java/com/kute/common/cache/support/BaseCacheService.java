package com.kute.common.cache.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * created by bailong001 on 2018/10/08 18:22
 */
public class BaseCacheService {


    @Autowired(required = false)
    protected RedisTemplate<String, String> redisTemplate;

    public String getKV(String key) {
        return redisTemplate.opsForValue().get(key);
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
        return redisTemplate.delete(keyList);
    }

    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void setNullKey(String nullKey) {
        opsForValue().set(nullKey, "1", 7, TimeUnit.DAYS);
    }

    public void setNullKey(String nullKey, Long time, TimeUnit timeUnit) {
        opsForValue().set(nullKey, "1", time, timeUnit);
    }

    public ValueOperations<String, String> opsForValue() {
        return redisTemplate.opsForValue();
    }

    public HashOperations<String, String, String> opsForHash() {
        return redisTemplate.opsForHash();
    }

    public ZSetOperations<String, String> opsForZSet() {
        return redisTemplate.opsForZSet();
    }

    public SetOperations<String, String> opsForSet() {
        return redisTemplate.opsForSet();
    }

    public ClusterOperations<String, String> opsForCluster() {
        return redisTemplate.opsForCluster();
    }

    public RedisSerializer<?> getKeySerializer() {
        return redisTemplate.getKeySerializer();
    }

    public RedisSerializer<?> getHashKeySerializer() {
        return redisTemplate.getHashKeySerializer();
    }

    public RedisSerializer<?> getHashValueSerializer() {
        return redisTemplate.getHashValueSerializer();
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
        final RedisSerializer serializer = redisTemplate.getValueSerializer();
        if (serializer == null && (value instanceof byte[])) {
            return (byte[]) (byte[]) value;
        }
        return serializer.serialize(value);
    }

    public void expired(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

}
