/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsParam
 * Author:   xao
 * Date:     2020/9/11 17:08
 * Description: 短信参数
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.enums;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈短信参数〉
 *
 * @author Administrator
 * @date 2020/9/11
 * @since 1.0.0
 */
public enum SmsParam {
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

    /**
     * 日期
     * @return String 日期
     */
    private static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return simpleDateFormat.format(Calendar.getInstance(new SimpleTimeZone(0, "GMT")).getTime());
    }
}