package com.sysu.yizhu.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Created by CrazeWong on 2017/5/6.
 */
public class SmsUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SmsUtil.class);

    private static final String URL_BASE = "https://api.leancloud.cn/1.1";
    private static final String URL_SMS_SEND = URL_BASE + "/requestSmsCode";
    private static final String URL_SMS_CHECK = URL_BASE + "/verifySmsCode";

    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    private HttpHeaders headers;

    public SmsUtil(String id, String key) {
        LOG.info("id=" + id);
        LOG.info("key=" + key);

        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converter.setWriteAcceptCharset(false);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, converter);
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        headers = new HttpHeaders();
        headers.add("X-LC-Id", id);
        headers.add("X-LC-Key", key);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

}
