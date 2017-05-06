package com.sysu.yizhu.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
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

    public boolean sendSmsCode(String phoneNum) {
        try {
            String body = new ReqSmsParam(phoneNum).toJSON();
            HttpEntity<String> reqEntity = new HttpEntity<String>(body, headers);
            String res = restTemplate.postForObject(URL_SMS_SEND, reqEntity, String.class);
            LOG.info("sendSmsCode() res: " + res);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error("sendSmsCode() res: " + e.getMessage());
            LOG.error("sendSmsCode() res: " + e.getResponseBodyAsString());
            return false;
        }
    }

    public boolean checkSmsCode(String phoneNum, String code) {
        try {
            String url = URL_SMS_CHECK + "/" + code + "?mobilePhoneNumber=" + phoneNum;
            HttpEntity<String> reqEntity = new HttpEntity<String>(headers);
            String res = restTemplate.postForObject(url, reqEntity, String.class);
            LOG.info("checkSmsCode() res: " + res);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error("checkSmsCode() res: " + e.getMessage());
            LOG.error("checkSmsCode() res: " + e.getResponseBodyAsString());
            return false;
        }
    }

    /**
     * 发送短信参数设置
     */
    private class ReqSmsParam {
        private String mobilePhoneNumber;
        private int ttl = 2;
        private String op = "手机号验证";

        private ReqSmsParam(String phone) {
            mobilePhoneNumber = phone;
        }

        private String toJSON() {
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
    }

}
