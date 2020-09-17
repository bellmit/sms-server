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
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
@Configuration
@Slf4j
public class SmsConfig implements ApplicationContextAware {

    /**
     * applicationContext
     */
    private ApplicationContext applicationContext;

    /**
     * logMode
     */
    @Value("${sms.log.mode}")
    private String logMode;

    /**
     * mongodbEnable
     */
    @Value("${sms.log.mongodb.enabled}")
    private Boolean logMongodbEnable;

    /**
     * mongodbUrl
     */
    @Value("${sms.log.mongodb.url}")
    private String logMongodbUrl;

    /**
     * mqMode
     */
    @Value("${sms.mq.mode}")
    private String mqMode;

    /**
     * rabbitmqHost
     */
    @Value("${sms.mq.rabbitmq.host}")
    private String rabbitmqHost;

    /**
     * rabbitmqPort
     */
    @Value("${sms.mq.rabbitmq.port}")
    private String rabbitmqPort;

    @Value("${sms.mq.rabbitmq.virtualhost}")
    private String rabbitmqVirtualhost;

    @Value("${sms.mq.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${sms.mq.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${sms.mq.rabbitmq.exchange.direct.retrieve}")
    private String rabbitmqExchangeDirectRetrieveName;

    @Value("${sms.mq.rabbitmq.exchange.direct.query}")
    private String rabbitmqExchangeDirectQueryName;

    @Value("${sms.mq.rabbitmq.exchange.topic.log}")
    private String rabbitmqExchangeTopicLogName;

    @Value("${sms.mq.rabbitmq.exchange.fanout.name:}")
    private String rabbitmqExchangeFanoutName;

    @Value("${sms.mq.rabbitmq.queue.log}")
    private String rabbitmqQueueLogName;

    @Value("${sms.mq.rabbitmq.queue.retrieve}")
    private String rabbitmqQueueRetrieveName;

    @Value("${sms.mq.rabbitmq.queue.query}")
    private String rabbitmqQueueQueryName;

    private static final String _SPLIT = ",";

    /**
     * logList
     */
    public static List<SmsLog> logList = new ArrayList();

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
    protected void setLogList() {
        boolean enableMongodbLog = Arrays.asList(logMode.split(",")).indexOf(SmsLogEnum.Mongodb.getValue()) >= 0;
        if(enableMongodbLog) {
            logList.add(new MongodbLog(logMongodbUrl));
        }
    }

    protected List<SmsLog> getLogList() {
        return logList;
    }

    /**
     * rabbitAdmin
     *
     * @return RabbitAdmin 配置
     */
    @Bean
    @ConditionalOnProperty(value = "sms.mq.mode", havingValue = "rabbitmq")
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
    public void sendRabbitMessage(Exchange exchange, @NotNull String message) {
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
        // sendRabbitMessage(message);
    }

    /**
     * SmsRequestClient
     *
     * @return SmsRequestClient
     */
    public @Bean SmsRequestClient smsUser(@Autowired SmsConfig smsConfig) throws NoSuchMethodException {
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
                        ((SmsConfig) applicationContext.getBean("smsConfig")).getLogList().stream().forEach(v->v.log(new LogSaveQuery(){{
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
            }
        )
        .genProxy();
    }

    public String getLogMode() {
        return logMode;
    }

    public void setLogMode(String logMode) {
        this.logMode = logMode;
    }

    public Boolean getLogMongodbEnable() {
        return logMongodbEnable;
    }

    public void setLogMongodbEnable(Boolean logMongodbEnable) {
        this.logMongodbEnable = logMongodbEnable;
    }

    public String getLogMongodbUrl() {
        return logMongodbUrl;
    }

    public void setLogMongodbUrl(String logMongodbUrl) {
        this.logMongodbUrl = logMongodbUrl;
    }

    public String getMqMode() {
        return mqMode;
    }

    public void setMqMode(String mqMode) {
        this.mqMode = mqMode;
    }

    public String getRabbitmqHost() {
        return rabbitmqHost;
    }

    public void setRabbitmqHost(String rabbitmqHost) {
        this.rabbitmqHost = rabbitmqHost;
    }

    public String getRabbitmqPort() {
        return rabbitmqPort;
    }

    public void setRabbitmqPort(String rabbitmqPort) {
        this.rabbitmqPort = rabbitmqPort;
    }

    public String getRabbitmqVirtualhost() {
        return rabbitmqVirtualhost;
    }

    public void setRabbitmqVirtualhost(String rabbitmqVirtualhost) {
        this.rabbitmqVirtualhost = rabbitmqVirtualhost;
    }

    public String getRabbitmqUsername() {
        return rabbitmqUsername;
    }

    public void setRabbitmqUsername(String rabbitmqUsername) {
        this.rabbitmqUsername = rabbitmqUsername;
    }

    public String getRabbitmqPassword() {
        return rabbitmqPassword;
    }

    public void setRabbitmqPassword(String rabbitmqPassword) {
        this.rabbitmqPassword = rabbitmqPassword;
    }

    public String getRabbitmqExchangeDirectRetrieveName() {
        return rabbitmqExchangeDirectRetrieveName;
    }

    public void setRabbitmqExchangeDirectRetrieveName(String rabbitmqExchangeDirectRetrieveName) {
        this.rabbitmqExchangeDirectRetrieveName = rabbitmqExchangeDirectRetrieveName;
    }

    public String getRabbitmqExchangeDirectQueryName() {
        return rabbitmqExchangeDirectQueryName;
    }

    public void setRabbitmqExchangeDirectQueryName(String rabbitmqExchangeDirectQueryName) {
        this.rabbitmqExchangeDirectQueryName = rabbitmqExchangeDirectQueryName;
    }

    public String getRabbitmqExchangeTopicLogName() {
        return rabbitmqExchangeTopicLogName;
    }

    public void setRabbitmqExchangeTopicLogName(String rabbitmqExchangeTopicLogName) {
        this.rabbitmqExchangeTopicLogName = rabbitmqExchangeTopicLogName;
    }

    public String getRabbitmqExchangeFanoutName() {
        return rabbitmqExchangeFanoutName;
    }

    public void setRabbitmqExchangeFanoutName(String rabbitmqExchangeFanoutName) {
        this.rabbitmqExchangeFanoutName = rabbitmqExchangeFanoutName;
    }

    public String getRabbitmqQueueLogName() {
        return rabbitmqQueueLogName;
    }

    public void setRabbitmqQueueLogName(String rabbitmqQueueLogName) {
        this.rabbitmqQueueLogName = rabbitmqQueueLogName;
    }

    public String getRabbitmqQueueRetrieveName() {
        return rabbitmqQueueRetrieveName;
    }

    public void setRabbitmqQueueRetrieveName(String rabbitmqQueueRetrieveName) {
        this.rabbitmqQueueRetrieveName = rabbitmqQueueRetrieveName;
    }

    public String getRabbitmqQueueQueryName() {
        return rabbitmqQueueQueryName;
    }

    public void setRabbitmqQueueQueryName(String rabbitmqQueueQueryName) {
        this.rabbitmqQueueQueryName = rabbitmqQueueQueryName;
    }

    public void setLogList(List<SmsLog> logList) {
        this.logList = logList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}