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

import com.bim.marvel.message.sms.client.SmsRequestClient;
import com.bim.marvel.message.sms.client.SmsUser;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEventEnum;
import com.bim.marvel.message.sms.enums.SmsLogTypeEnum;
import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.enums.SmsLogEnum;
import com.bim.marvel.message.sms.query.LogSaveQuery;
import com.bim.marvel.message.sms.util.MongodbLog;
import com.bim.marvel.message.sms.util.ProxyFactory;
import com.bim.marvel.message.sms.util.SmsLog;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xao
 *
 * 记录日志
 * 异常处理
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
@Slf4j
public class SmsConfig {

    /**
     * logMode
     */
    @Value("log.mode")
    private String logMode;

    /**
     * mongodbEnable
     */
    @Value("log.mongodb.enabled")
    private Boolean logMongodbEnable;

    /**
     * mongodbUrl
     */
    @Value("log.mongodb.url")
    private String logMongodbUrl;

    /**
     * mqMode
     */
    @Value("${mq.mode}")
    private String mqMode;

    /**
     * rabbitmqHost
     */
    @Value("${mq.rabbitmq.host}")
    private String rabbitmqHost;

    /**
     * rabbitmqPort
     */
    @Value("${mq.rabbitmq.port}")
    private String rabbitmqPort;

    @Value("${mq.rabbitmq.virtualhost}")
    private String rabbitmqVirtualhost;

    @Value("${mq.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${mq.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${mq.rabbitmq.exchange.direct.retrieve}")
    private String rabbitmqExchangeDirectRetrieveName;

    @Value("${mq.rabbitmq.exchange.direct.query}")
    private String rabbitmqExchangeDirectQueryName;

    @Value("${mq.rabbitmq.exchange.topic.log}")
    private String rabbitmqExchangeTopicLogName;

    @Value("${mq.rabbitmq.exchange.fanout.name}")
    private String rabbitmqExchangeFanoutName;

    @Value("${mq.rabbitmq.queue.log}")
    private String rabbitmqQueueLogName;

    @Value("${mq.rabbitmq.queue.retrieve}")
    private String rabbitmqQueueRetrieveName;

    @Value("${mq.rabbitmq.queue.query}")
    private String rabbitmqQueueQueryName;

    private static final String _SPLIT = ",";

    /**
     * logList
     */
    public List<SmsLog> logList = new ArrayList();

    /**
     * mongoClient
     *
     * @return MongoClient
     */
    public @Bean MongoClient mongoClient() {
        return MongoClients.create(getLogMongodbUrl());
    }

    /**
     * getLogList
     */
    protected void getLogList() {
        boolean enableMongodbLog = Arrays.asList(logMode.split(",")).indexOf(SmsLogEnum.Mongodb.getValue()) > 0;
        if(enableMongodbLog){
            logList.add(new MongodbLog());
        }
    }

    /**
     * mongoTemplate
     *
     * @return MongoTemplate
     */
    public @Bean MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "SMS_LOG_MONGODB");
    }

    /**
     * rabbitAdmin
     *
     * @return RabbitAdmin 配置
     */
    @Bean
    @Profile("local")
    public RabbitAdmin rabbitAdmin() {
        try{
            RabbitAdmin rabbitAdmin = new RabbitAdmin(getRabbitTemplate());
            // declareExchange
            Exchange exchangeRetrieve = new DirectExchange(rabbitmqExchangeDirectRetrieveName, false, false);
            rabbitAdmin.declareExchange(exchangeRetrieve);
            Exchange exchangeQuery = new DirectExchange(rabbitmqExchangeDirectQueryName, false, false);
            rabbitAdmin.declareExchange(exchangeQuery);
            Exchange exchangeLog = new TopicExchange(rabbitmqExchangeTopicLogName, false, false);
            rabbitAdmin.declareExchange(exchangeLog);
            // declareQueue
            Queue queueLog = new Queue(rabbitmqQueueLogName);
            rabbitAdmin.declareQueue(queueLog);
            Queue queueRetrieve = new Queue(rabbitmqQueueRetrieveName);
            rabbitAdmin.declareQueue(queueRetrieve);
            Queue queueQuery = new Queue(rabbitmqQueueQueryName);
            rabbitAdmin.declareQueue(queueQuery);
            // declareBinding
            rabbitAdmin.declareBinding(BindingBuilder.bind(queueRetrieve).to(exchangeRetrieve).with("retrieve").noargs());
            rabbitAdmin.declareBinding(BindingBuilder.bind(queueLog).to(exchangeQuery).with("query").noargs());
            rabbitAdmin.declareBinding(BindingBuilder.bind(queueLog).to(exchangeLog).with("log.#").noargs());
            return rabbitAdmin;
        }catch(Exception ex){
            // log
            log.error(ex.getMessage());
            return null;
        }
    }

    /**
     * getRabbitTemplate
     *
     * @return RabbitTemplate
     */
    public RabbitTemplate getRabbitTemplate() {
        // connectionFactory
        CachingConnectionFactory connectionFactory = connectionFactory();
        // rabbitTemplate
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // retryTemplate
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);

        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey)->{
            System.out.println("sendmessageCallback");
        });
        return rabbitTemplate;
    }


    public @Bean CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        connectionFactory.setHost(rabbitmqHost);
        connectionFactory.setPort(Integer.valueOf(rabbitmqPort));
        connectionFactory.setVirtualHost(rabbitmqVirtualhost);
        return connectionFactory;
    }

    /**
     * 发送消息
     *
     * @param message 发送消息
     */
    public void sendRabbitMessage(@NotNull String message) {
        getRabbitTemplate().convertAndSend(message);
    }

    /**
     * pushEvent
     *
     * @param smsEventEnum
     */
    public void pushEvent(SmsEventEnum smsEventEnum) {
        String message = "";
        switch (smsEventEnum) {
            case SEND_SMS:
                message = "log.sms.send";
                break;
            case SEND_SMS_TRUE:
                message = "log.sms.send.true";
                break;
            case SEND_SMS_FALSE:
                message = "retrieve";
                break;
            default:
                break;
        }
        sendRabbitMessage(message);
    }

    /**
     * SmsRequestClient
     *
     * @return SmsRequestClient
     */
    public @Bean SmsRequestClient smsUser() throws NoSuchMethodException {
        return ProxyFactory.genProxy(
            (SmsRequestClient) new SmsUser(),
            new ProxyFactory.ProxyEntry[]{
                new ProxyFactory.ProxyEntry(){{
                    setClazz(SmsUser.class);
                    setMethod(SmsUser.class.getDeclaredMethod("sendSmsNotice", SmsEnum.class, AliSmsNoticeDTO.class));
                    setProAftAspect((proxyEntry)->{
                        // 短信发送状态
                        String smsResultData = (String) ((ProxyFactory.ProxyEntry) proxyEntry).getResultData();
                        log.info(smsResultData);
                        // 记录日志
                        logList.stream().forEach(v->v.log(new LogSaveQuery(){{
                            setDate(new Date());
                            setSmsLogTypeEnum(SmsLogTypeEnum.SMS_SEND_RESULT_TRUE);
                        }}));
                        return SmsLogTypeEnum.SMS_SEND_RESULT_TRUE;
                    });
                }},
                new ProxyFactory.ProxyEntry(){{
                    setClazz(SmsUser.class);
                    setMethod(SmsUser.class.getDeclaredMethod("sendSmsValidCode", SmsEnum.class, AliSmsValidCodeDTO.class));
                }}
        })
        .genProxy();
    }
}