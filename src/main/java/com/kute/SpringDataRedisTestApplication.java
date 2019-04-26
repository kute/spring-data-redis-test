package com.kute;

import com.kute.cache.ValueCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@ComponentScan(value = {"com.kute"})
@SpringBootApplication
public class SpringDataRedisTestApplication implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataRedisTestApplication.class);

    private static ApplicationContext applicationContext;

    @Resource
    private ValueCacheService valueCacheService;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRedisTestApplication.class, args);
        LOGGER.info("");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringDataRedisTestApplication.applicationContext = applicationContext;
    }


    @GetMapping("/test")
    public String test(@RequestParam String key) {
        return valueCacheService.getKV(key);
//        return "test";
    }


}

