package com.kute.config.enums;

/**
 * created by kute on 2019/02/18 15:45
 */
public enum RedisClientType {

    JEDIS("JEDIS"),
    LETTUCE("LETTUCE"),;

    private String type;

    RedisClientType(String type) {
        this.type = type;
    }
}
