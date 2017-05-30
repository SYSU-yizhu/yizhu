package com.sysu.yizhu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
@ComponentScan({ "com.sysu.yizhu" })
@PropertySource({ "classpath:leancloud.properties" })
public class LCConfig {

    @Autowired
    private Environment env;

    public LCConfig() {
        super();
    }

    @Bean
    public LCUtil smsUtil() {
        String id = env.getProperty("X-LC-Id");
        String key = env.getProperty("X-LC-Key");
        final LCUtil LCUtil = new LCUtil(id, key);
        return LCUtil;
    }
}
