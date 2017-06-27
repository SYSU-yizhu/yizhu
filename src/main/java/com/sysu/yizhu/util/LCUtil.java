package com.sysu.yizhu.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

public class LCUtil {
    private static final Logger LOG = LoggerFactory.getLogger(LCUtil.class);

    private static final String URL_BASE = "https://api.leancloud.cn/1.1";
    private static final String URL_SMS_SEND = URL_BASE + "/requestSmsCode";
    private static final String URL_SMS_CHECK = URL_BASE + "/verifySmsCode";
    private static final String URL_INSTALL = URL_BASE + "/installations";
    private static final String URL_PUSH = URL_BASE + "/push";

    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    private HttpHeaders headers;

    public LCUtil(String id, String key) {
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

    public boolean checkObjectId(String objectId) {
        try {
            HttpEntity<String> reqEntity = new HttpEntity<String>(headers);
            ResponseEntity<String> res = restTemplate.exchange(URL_INSTALL + "/{objectId}", HttpMethod.GET, reqEntity, String.class, objectId);
            LOG.info("checkObjectId() res: " + res);
            if (res.getBody().equals("{}")) return false;
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error("checkObjectId() res: " + e.getMessage());
            LOG.error("checkObjectId() res: " + e.getResponseBodyAsString());
            return false;
        }
    }

    public boolean putLocation(String objectId, Double latitude, Double longitude) {
        try {
            HttpEntity<String> reqEntity = new HttpEntity<String>(getPutLocationJSON(latitude, longitude), headers);
            ResponseEntity<String> res = restTemplate.exchange(URL_INSTALL + "/{objectId}", HttpMethod.PUT, reqEntity, String.class, objectId);
            LOG.info("putLocation() res: " + res);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error("putLocation() res: " + e.getMessage());
            LOG.error("putLocation() res: " + e.getResponseBodyAsString());
            return false;
        }
    }

    public boolean pushSOS(Double latitude, Double longitude) {
        try {
            HttpEntity<String> reqEntity = new HttpEntity<String>(
                    getPushSOSJSON(latitude, longitude, 100.0, "一条新的求救消息！"),
                    headers);
            String res = restTemplate.postForObject(URL_PUSH, reqEntity, String.class);
            LOG.info("pushSOS() res: " + res);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error("pushSOS() res: " + e.getMessage());
            LOG.error("pushSOS() res: " + e.getResponseBodyAsString());
            return false;
        }
    }

    public boolean pushHelp(Double latitude, Double longitude) {
        try {
            HttpEntity<String> reqEntity = new HttpEntity<String>(
                    getPushHelpJSON(latitude, longitude, 100.0, "一条新的求助消息！"),
                    headers);
            String res = restTemplate.postForObject(URL_PUSH, reqEntity, String.class);
            LOG.info("pushSOS() res: " + res);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error("pushSOS() res: " + e.getMessage());
            LOG.error("pushSOS() res: " + e.getResponseBodyAsString());
            return false;
        }
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

    private static final String REQ_PUT_LOCATION_TEMPLATE =
            "{\n"+
            "    \"location\": {\n"+
            "      \"__type\": \"GeoPoint\",\n"+
            "      \"latitude\": {latitude},\n"+
            "      \"longitude\": {longitude}\n"+
            "    }\n"+
            "}";

    private String getPutLocationJSON(Double latitude, Double longitude) {
        return REQ_PUT_LOCATION_TEMPLATE.replace("{latitude}", latitude.toString())
                                .replace("{longitude}", longitude.toString());
    }

    private static final String REQ_PUSH_SOS_TEMPLATE =
            "{\n" +
            "    \"where\": {\n" +
            "          \"location\": {\n" +
            "            \"$nearSphere\": {\n" +
            "              \"__type\": \"GeoPoint\",\n" +
            "              \"latitude\": {latitude},\n" +
            "              \"longitude\": {longitude}\n" +
            "            },\n" +
            "            \"$maxDistanceInMiles\": {maxDistance}\n" +
            "          }\n" +
            "    },\n" +
            "    \"data\": {\n" +
            "        \"alert\": \"{alertContent}\"\n" +
            "    }\n" +
            "}";

    private String getPushSOSJSON(Double latitude, Double longitude, Double maxDistance, String alertContent) {
        return REQ_PUSH_SOS_TEMPLATE.replace("{latitude}", latitude.toString())
                .replace("{longitude}", longitude.toString())
                .replace("{maxDistance}", maxDistance.toString())
                .replace("{alertContent}", alertContent);
    }

    private static final String REQ_PUSH_HELP_TEMPLATE =
            "{\n" +
                    "    \"where\": {\n" +
                    "          \"location\": {\n" +
                    "            \"$nearSphere\": {\n" +
                    "              \"__type\": \"GeoPoint\",\n" +
                    "              \"latitude\": {latitude},\n" +
                    "              \"longitude\": {longitude}\n" +
                    "            },\n" +
                    "            \"$maxDistanceInMiles\": {maxDistance}\n" +
                    "          }\n" +
                    "    },\n" +
                    "    \"data\": {\n" +
                    "        \"alert\": \"{alertContent}\"\n" +
                    "    }\n" +
                    "}";

    private String getPushHelpJSON(Double latitude, Double longitude, Double maxDistance, String alertContent) {
        return REQ_PUSH_SOS_TEMPLATE.replace("{latitude}", latitude.toString())
                .replace("{longitude}", longitude.toString())
                .replace("{maxDistance}", maxDistance.toString())
                .replace("{alertContent}", alertContent);
    }
}
