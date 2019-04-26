package com.kute.config.properties;

import com.kute.config.enums.RedisClientType;
import com.kute.config.enums.RedisType;

/**
 * created by bailong001 on 2019/02/18 14:58
 */
public class RedisProperties {

    /**
     * redis-cluster、master-slave
     */
    private RedisType type = RedisType.MASTER_SLAVE;

    /**
     * redis client type: jedis lettuce
     */
    private RedisClientType clientType = RedisClientType.JEDIS;

//    private RedisLettuceClusterProperties lettuceCluster;
//
//    private RedisLettuceProperties lettuce;

    public RedisType getType() {
        return type;
    }

    public RedisProperties setType(RedisType type) {
        this.type = type;
        return this;
    }

    public RedisClientType getClientType() {
        return clientType;
    }

    public RedisProperties setClientType(RedisClientType clientType) {
        this.clientType = clientType;
        return this;
    }

//    public RedisLettuceClusterProperties getLettuceCluster() {
//        return lettuceCluster;
//    }
//
//    public RedisProperties setLettuceCluster(RedisLettuceClusterProperties lettuceCluster) {
//        this.lettuceCluster = lettuceCluster;
//        return this;
//    }
//
//    public RedisLettuceProperties getLettuce() {
//        return lettuce;
//    }
//
//    public RedisProperties setLettuce(RedisLettuceProperties lettuce) {
//        this.lettuce = lettuce;
//        return this;
//    }
}
