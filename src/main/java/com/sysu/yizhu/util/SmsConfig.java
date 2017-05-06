package com.sysu.yizhu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by CrazeWong on 2017/5/6.
 */
@Configuration
@ComponentScan({ "com.sysu.yizhu" })
@PropertySource({ "classpath:leancloud.properties" })
public class SmsConfig {

    @Autowired
    private Environment env;

    public SmsConfig() {
        super();
    }

    @Bean
    public SmsUtil smsUtil() {
        String id = env.getProperty("X-LC-Id");
        String key = env.getProperty("X-LC-Key");
        final SmsUtil smsUtil = new SmsUtil(id, key);
        return smsUtil;
    }
}
