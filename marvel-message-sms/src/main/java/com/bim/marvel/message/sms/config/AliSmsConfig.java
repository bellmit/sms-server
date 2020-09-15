/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsConfig
 * Author:   xao
 * Date:     2020/9/8 11:37
 * Description: 阿里云短信工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.config;

import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.query.AliSmsQuery;
import com.bim.marvel.message.sms.util.MongodbLog;
import com.bim.marvel.message.sms.util.SmsLog;
import com.bim.marvel.message.sms.util.aliSms.AliSmsFactory;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 阿里短信配置
 * @author xao
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms.ali")
public class AliSmsConfig extends SmsConfig implements InitializingBean {

    /**
     * aliSmsConfig
     */
    @Autowired
    private AliSmsConfig aliSmsConfig;

    /**
     * 短信平台
     */
    public static final String RESPONSE_CODE = "OK";

    /**
     * ALI_SMS_PRODUCT
     */
    public static final String ALI_SMS_PRODUCT = "Dysmsapi";

    /**
     * ALI_SMS_URL
     */
    public static final String ALI_SMS_URL = "http://dysmsapi.aliyuncs.com";

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * accessSecret
     */
    private String accessSecret;

    /**
     * signatureMethod
     */
    private String signatureMethod = "HMAC-SHA1";

    /**
     * 短信参数 accessKeyId
     */
    private String accessKeyId;

    /**
     * 短信参数 signatureVersion
     */
    private String signatureVersion = "1.0";

    /**
     * 短信参数 regionId
     */
    private String regionId = "cn-hangzhou";

    /**
     * 短信参数 version
     */
    private String version = "2017-05-25";

    /**
     * 短信参数 format
     */
    private String format = "XML";

    /**
     * 短信参数 action
     */
    private String action = "SendSms";

    /**
     * 短信模板
     */
    private List<Map<String, String>> formatters;

    /**
     * smsLogList
     */
    private List<SmsLog> smsLogList;

    @Override
    public void afterPropertiesSet() throws Exception {
        Arrays.asList(SmsEnum.values()).stream().forEach(v -> {
            String smsValue = v.getValue();
            Map<String, String> smsFormatter = formatters.stream().filter(v1 -> smsValue.equals(v1.get("id"))).collect(Collectors.toList()).get(0);
            AliSmsQuery aliSmsQuery = new AliSmsQuery(){{
                setSignName(smsFormatter.get("signName"));
                setTemplateCode(smsFormatter.get("templateCode"));
                setTemplateParam(smsFormatter.get("templateParam"));
                setAliSmsConfig(aliSmsConfig);
            }};
            AliSmsFactory.putAliSmsQuery(v, aliSmsQuery);
            if(getLogMongodbEnable()) {
                smsLogList.add(new MongodbLog(getLogMongodbUrl(), mongoTemplate));
            }
        });
    }
}