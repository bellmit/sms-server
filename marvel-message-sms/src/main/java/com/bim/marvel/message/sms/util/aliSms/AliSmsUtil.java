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

import com.bim.marvel.common.util.SimpleConverter;
import com.bim.marvel.message.sms.config.AliSmsConfig;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.query.AliSmsQuery;
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
     * 短信验证码
     *
     * @param aliSmsConfig 配置
     * @param aliSmsQuery 参数
     * @exception Exception Exception
     */
    private static void sendAliSmsValidCode(AliSmsConfig aliSmsConfig, AliSmsQuery aliSmsQuery) throws Exception {
        String urlParam = getAliSmsParam(aliSmsConfig, aliSmsQuery);
        String urlSendAliSms = AliSmsConfig.ALI_SMS_URL + "?Signature=" + sign(aliSmsConfig.getAccessSecret(), urlParam) + urlParam;
    }

    /**
     * 短信验证码
     *
     * @param aliSmsValidCodeDTO 短信参数
     * @param smsEnum 短信类型
     * @throws Exception Exception
     */
    public static void sendAliSmsValidCode(AliSmsValidCodeDTO aliSmsValidCodeDTO, SmsEnum smsEnum) throws Exception {
        AliSmsQuery aliSmsQuery = AliSmsFactory.getAliSmsQuery(smsEnum);
        aliSmsQuery.setPhone(aliSmsValidCodeDTO.getPhoneNumbers());
        sendAliSmsValidCode(aliSmsQuery.getAliSmsConfig(), aliSmsQuery);
    }

    /**
     * 短信通知
     *
     * @param aliSmsNoticeDTO 短信参数
     * @param smsEnum 短信类型
     * @throws Exception Exception
     */
    public static void sendAliSmsNotice(AliSmsNoticeDTO aliSmsNoticeDTO, SmsEnum smsEnum) throws Exception {
        AliSmsQuery aliSmsQuery = AliSmsFactory.getAliSmsQuery(smsEnum);
        aliSmsQuery.setPhone(aliSmsNoticeDTO.getPhoneNumbers());
        aliSmsQuery.setTemplateParam(aliSmsNoticeDTO.getTemplateParam());
        sendAliSmsValidCode(aliSmsQuery.getAliSmsConfig(), aliSmsQuery);
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
        Map smsParamMap = SimpleConverter.convert(aliSmsQuery, TreeMap.class);
        Map aliSmsConfigMap = SimpleConverter.convert(aliSmsConfig, TreeMap.class);
        smsParamMap.putAll(aliSmsConfigMap);
        return urlParam(smsParamMap);
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
        return simpleDateFormat.format(Calendar.getInstance(new SimpleTimeZone(0, "GMT")).getTime());
    }
}