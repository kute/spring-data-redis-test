package com.kute.config.listener;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

/**
 * created by bailong001 on 2019/03/17 09:57
 */
@Component
public class RedisKeyExpiredEventListener implements ApplicationListener<ApplicationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisKeyExpiredEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        LOGGER.info("======={}={}={}={}={}", event.getSource());
    }
}
