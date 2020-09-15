/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: CommonSmsConfig
 * Author:   xao
 * Date:     2020/9/8 11:37
 * Description: 阿里云短信工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author xao
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms.log")
public class SmsConfig {

    @Autowired
    protected MongoTemplate mongoTemplate;

    // 记录日志
    // 异常处理

    /**
     * mongodbEnable
     */
    @Value("mongodb.enabled")
    private Boolean logMongodbEnable;

    /**
     * mongodbUrl
     */
    @Value("mongodb.url")
    private String logMongodbUrl;
}