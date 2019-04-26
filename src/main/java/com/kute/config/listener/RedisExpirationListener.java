package com.kute.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * created by bailong001 on 2019/03/15 16:38
 */
public class RedisExpirationListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisExpirationListener.class);
    @Override
    public void onMessage(Message message, byte[] pattern) {

        LOGGER.info("==========={}={}", new String(message.getBody()), new String(pattern));

    }
}
