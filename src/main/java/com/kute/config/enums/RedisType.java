package com.kute.config.enums;

/**
 * created by kute on 2019/02/18 15:45
 */
public enum RedisType {

    REDIS_CLUSTER("REDIS-CLUSTER"),
    MASTER_SLAVE("MASTER_SLAVE"),
    ;

    private String type;

    RedisType(String type) {
        this.type = type;
    }
}
