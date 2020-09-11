/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsUtil
 * Author:   xao
 * Date:     2020/9/8 11:37
 * Description: 阿里云短信工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms;

import com.bim.marvel.common.util.SimpleConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 〈阿里云短信平台〉<br>
 * 〈阿里云短信工具类〉
 *
 * @author xao
 * @date 2020/9/8
 * @since 1.0.0
 */
public class AliSmsUtil {

    /**
     * 短信平台
     */
    private static final String RESPONSE_CODE = "OK";

    /**
     * ALI_SMS_PRODUCT
     */
    private static final String ALI_SMS_PRODUCT = "Dysmsapi";

    /**
     * ALI_SMS_URL
     */
    private static final String ALI_SMS_URL = "http://dysmsapi.aliyuncs.com";

    /**
     * ALI_SMS_REGION
     */
    private static final String ALI_SMS_REGION = "cn-hangzhou";

    /**
     * RESTTEMPLATE
     */
    private static final RestTemplate RESTTEMPLATE = new RestTemplate();

    /**
     * 发送短信
     *
     * @param aliSmsConfig 配置
     * @param aliSmsRequestDTO 参数
     * @exception Exception Exception
     */
    public static ResponseEntity<Map> sendAliSms(AliSmsConfig aliSmsConfig, AliSmsRequestDTO aliSmsRequestDTO) throws Exception {
        String urlParam = getAliSmsParam(aliSmsConfig, aliSmsRequestDTO);
        String urlSendAliSms = ALI_SMS_URL + "?Signature=" + sign(aliSmsConfig.getAccessSecret(), urlParam) + urlParam;
        ResponseEntity<Map> responseEntity = RESTTEMPLATE.getForEntity(urlSendAliSms, Map.class);
        return responseEntity;
    }

    /**
     * getAliSmsParam
     * @param aliSmsConfig 配置
     * @param aliSmsRequestDTO 参数
     * @return String 参数
     * @throws Exception Exception
     */
    public static String getAliSmsParam(AliSmsConfig aliSmsConfig, AliSmsRequestDTO aliSmsRequestDTO) throws Exception {
        SmsParam smsParam = SmsParam.AliSms;
        Map smsParamMap = SimpleConverter.convert(smsParam, TreeMap.class);
        Map aliSmsConfigMap = SimpleConverter.convert(aliSmsConfig, TreeMap.class);
        smsParamMap.putAll(aliSmsConfigMap);
        return urlParam(smsParamMap);
    }

    /**
     * urlParam
     * @param map 参数
     * @return String urlParam
     * @throws Exception Exception
     */
    private static String urlParam(Map<String, String> map) throws Exception {
        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("GET").append("&");
        stringBuilder.append(urlEncode("/"));

        while (iterator.hasNext()) {
            String key = iterator.next();
            stringBuilder.append("&").append(urlEncode(key)).append("=").append(urlEncode(map.get(key)));
        }

        return stringBuilder.toString();
    }

    /**
     * 签名
     *
     * @param accessSecret accessSecret
     * @param param 参数
     * @return String 签名
     * @throws NoSuchAlgorithmException Exception
     * @throws UnsupportedEncodingException Exception
     * @throws InvalidKeyException Exception
     */
    private static String sign(String accessSecret, String param) throws NoSuchAlgorithmException,
            UnsupportedEncodingException, InvalidKeyException {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(param.getBytes("UTF-8"));
        return new sun.misc.BASE64Encoder().encode(signData);
    }

    /**
     * urlEncode
     * @param param 参数
     * @return String urlEncode
     * @throws Exception Exception
     */
    private static String urlEncode(String param) throws Exception {
        return java.net.URLEncoder.encode(param, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    /**
     * AliSmsConfig
     */
    @Data
    @AllArgsConstructor
    public static class AliSmsConfig {
        /**
         * accessKey
         */
        private String accessKey;

        /**
         * accessSecret
         */
        private String accessSecret;
    }

    /**
     * AliSmsRequestDTO
     */
    @Data
    public class AliSmsRequestDTO {

        /**
         * 号码
         */
        private String phoneNumbers;

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 短信模板
         */
        private String templateCode;

        /**
         * 参数
         */
        private String templateParam;

        /**
         * 短信类型
         */
        private SmsTypeEnum smsTypeEnum;
    }

    /**
     * 短信类型
     */
    enum SmsTypeEnum {
        /**
         * 验证码短信
         */
        ValidCodeSms,

        /**
         * 通知短信
         */
        NoticeSms;
    }

    /**
     * 日期
     * @return String 日期
     */
    private static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return simpleDateFormat.format(Calendar.getInstance(new SimpleTimeZone(0, "GMT")).getTime());
    }

    /**
     * 短信参数
     */
    enum SmsParam {
        /**
         * 阿里云短信参数
         */
        AliSms("HMAC-SHA1",
                UUID.randomUUID().toString(),
                "",
                "1.0",
                getDate(),
                "XML",
                "cn-hangzhou",
                "2017-05-25",
                "SendSms");

        /**
         * 短信参数 signatureMethod
         */
        private String signatureMethod;

        /**
         * 短信参数 signatureNonce
         */
        private String signatureNonce;

        /**
         * 短信参数 accessKeyId
         */
        private String accessKeyId;

        /**
         * 短信参数 signatureVersion
         */
        private String signatureVersion;

        /**
         * 短信参数 timestamp
         */
        private String timestamp;

        /**
         * 短信参数 format
         */
        private String format;

        /**
         * 短信参数 regionId
         */
        private String regionId;

        /**
         * 短信参数 version
         */
        private String version;

        /**
         * 短信参数 action
         */
        private String action;

        SmsParam(String signatureMethod, String signatureNonce, String accessKeyId, String signatureVersion,
                 String timestamp, String format, String regionId, String version, String action) {
            signatureMethod = signatureMethod;
            signatureNonce = signatureNonce;
            accessKeyId = accessKeyId;
            signatureVersion = signatureVersion;
            timestamp = timestamp;
            format = format;
            regionId = regionId;
            version = version;
            action = action;
        }
    }
}