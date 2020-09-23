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
package com.bim.marvel.message.sms.util.aliSms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.bim.marvel.message.sms.config.AliSmsConfig;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.query.AliSmsQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URI;
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
@Slf4j
public class AliSmsUtil {

    private static final RestTemplate RESTTEMPLATE = new RestTemplate();

    private static final Random RANDOM = new Random();

    private static String getValidCode(int count){
        Set<Integer> set = new HashSet();
        while (set.size() < count) {
            set.add(RANDOM.nextInt(10));
        }
        StringBuilder stringBuilder = new StringBuilder();
        set.stream().forEach(v->{
            stringBuilder.append(v);
        });
        return stringBuilder.toString();
    }

    /**
     * 短信验证码
     *
     * @param aliSmsConfig 配置
     * @param aliSmsQuery 参数
     * @exception Exception Exception
     */
    private static ResponseEntity<Map> sendAliSmsValidCode(AliSmsConfig aliSmsConfig, AliSmsQuery aliSmsQuery) throws Exception {
        aliSmsQuery.setTemplateParam("{\"code\":\"" + getValidCode(4) + "\"}");
        String urlParam = getAliSmsParam(aliSmsConfig, aliSmsQuery);
        String urlSendAliSms = AliSmsConfig.ALI_SMS_URL + "?Signature=" + urlEncode(sign(aliSmsConfig.getAccessSecret(), urlParam)) + "&" + urlParam;
        URI uri = URI.create(urlSendAliSms);
        ResponseEntity<Map> responseEntity = RESTTEMPLATE.getForEntity(uri, Map.class);
        return responseEntity;
    }

    /**
     * 短信验证码
     *
     * @param aliSmsValidCodeDTO 短信参数
     * @param smsEnum 短信类型
     * @throws Exception Exception
     */
    public static String sendAliSmsValidCode(AliSmsValidCodeDTO aliSmsValidCodeDTO, SmsEnum smsEnum) throws Exception {
        AliSmsQuery aliSmsQuery = AliSmsFactory.getAliSmsQuery(smsEnum);
        aliSmsQuery.setPhoneNumbers(aliSmsValidCodeDTO.getPhoneNumbers());
        aliSmsQuery.setTimestamp(getDate());
        aliSmsQuery.setSignatureNonce(java.util.UUID.randomUUID().toString());
        sendAliSmsValidCode(aliSmsQuery.getAliSmsConfig(), aliSmsQuery);
        return null;
    }

    /**
     * 短信通知
     *
     * @param aliSmsNoticeDTO 短信参数
     * @param smsEnum 短信类型
     * @throws Exception Exception
     */
    public static String sendAliSmsNotice(AliSmsNoticeDTO aliSmsNoticeDTO, SmsEnum smsEnum) throws Exception {
        AliSmsQuery aliSmsQuery = AliSmsFactory.getAliSmsQuery(smsEnum);
        aliSmsQuery.setPhone(aliSmsNoticeDTO.getPhoneNumbers());
        // aliSmsQuery.setTemplateParam(aliSmsNoticeDTO.getTemplateParam());
        sendAliSmsValidCode(aliSmsQuery.getAliSmsConfig(), aliSmsQuery);
        return null;
    }

    /**
     * 验证参数格式
     *
     * @param templateParam 短信模板参数
     */
    public static void validateTemplateParam(String templateParam) {
    }

    /**
     * getAliSmsParam
     *
     * @param aliSmsConfig 配置
     * @param aliSmsQuery 参数
     * @return String 参数
     * @throws Exception Exception
     */
    public static String getAliSmsParam(AliSmsConfig aliSmsConfig, AliSmsQuery aliSmsQuery) throws Exception {
        PropertyPreFilters propertyPreFilters = new PropertyPreFilters();
        String[] propertyArray = new String[]{ "accessKeyId", "signatureMethod", "signatureVersion", "regionId", "version", "format", "action" };
        PropertyPreFilters.MySimplePropertyPreFilter simplePropertyPreFilter = propertyPreFilters.addFilter(propertyArray);
        Map<String, String> aliSmsConfigMap = JSON.parseObject(JSON.toJSONString(aliSmsConfig, simplePropertyPreFilter), TreeMap.class);
        PropertyPreFilters.MySimplePropertyPreFilter aliSmsQueryPropertyPreFilter = propertyPreFilters.addFilter();
        aliSmsQueryPropertyPreFilter.addExcludes("aliSmsConfig");
        Map aliSmsParamMap = JSON.parseObject(JSON.toJSONString(aliSmsQuery, aliSmsQueryPropertyPreFilter), TreeMap.class);
        aliSmsConfigMap.putAll(aliSmsParamMap);
        Set<String> keySet = aliSmsConfigMap.keySet();
        Map<String, String> map = new TreeMap();
        keySet.stream().forEach(v->{
            String value = aliSmsConfigMap.get(v);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(v.charAt(0)).toUpperCase());
            stringBuilder.append(v.substring(1));
            map.put(stringBuilder.toString(), value);
        });
        return urlParam(map);
    }

    /**
     * urlParam
     *
     * @param map 参数
     * @return String urlParam
     * @throws Exception Exception
     */
    private static String urlParam(Map<String, String> map) throws Exception {
        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();

        // stringBuilder.append("GET").append("&");
        // stringBuilder.append(urlEncode("/"));

        while (iterator.hasNext()) {
            String key = iterator.next();
            stringBuilder.append("&").append(urlEncode(key)).append("=").append(urlEncode(map.get(key)));
        }

        return stringBuilder.substring(stringBuilder.indexOf("&") + 1);
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
    private static String sign(String accessSecret, String param) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        accessSecret += "&";
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GET").append("&");
        stringBuilder.append(urlEncode("/")).append("&");
        stringBuilder.append(urlEncode(param));
        byte[] signData = mac.doFinal(stringBuilder.toString().getBytes("UTF-8"));
        return new sun.misc.BASE64Encoder().encode(signData);
    }

    /**
     * urlEncode
     *
     * @param param 参数
     * @return String urlEncode
     * @throws Exception Exception
     */
    private static String urlEncode(String param) throws Exception {
        return java.net.URLEncoder.encode(param, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    /**
     * 日期
     *
     * @return String 日期
     */
    private static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return simpleDateFormat.format(new Date());
    }
}