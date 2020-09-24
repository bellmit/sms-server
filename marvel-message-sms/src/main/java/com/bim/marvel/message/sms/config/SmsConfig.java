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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bim.marvel.common.util.SimpleConverter;
import com.bim.marvel.message.sms.client.SmsRequestClient;
import com.bim.marvel.message.sms.client.SmsSender;
import com.bim.marvel.message.sms.client.SmsUser;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEventEnum;
import com.bim.marvel.message.sms.enums.SmsLogTypeEnum;
import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.enums.SmsLogEnum;
import com.bim.marvel.message.sms.query.LogSaveQuery;
import com.bim.marvel.message.sms.query.SmsQuery;
import com.bim.marvel.message.sms.util.MongodbLog;
import com.bim.marvel.message.sms.util.ProxyEntry;
import com.bim.marvel.message.sms.util.ProxyFactory;
import com.bim.marvel.message.sms.util.SmsLog;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.retry.ImmediateRequeueMessageRecoverer;
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
import org.springframework.lang.Nullable;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.NoBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Value("${sms.mq.rabbitmq.exchange.direct.send}")
    private String rabbitmqExchangeDirectSendName;

    @Value("${sms.mq.rabbitmq.exchange.direct.retrieve}")
    private String rabbitmqExchangeTopicRetrieveName;

    @Value("${sms.mq.rabbitmq.exchange.direct.query}")
    private String rabbitmqExchangeDirectQueryName;

    @Value("${sms.mq.rabbitmq.exchange.topic.log}")
    private String rabbitmqExchangeTopicLogName;

    @Value("${sms.mq.rabbitmq.exchange.fanout.name:}")
    private String rabbitmqExchangeFanoutName;

    @Value("${sms.mq.rabbitmq.queue.log}")
    private String rabbitmqQueueLogName;

    @Value("${sms.mq.rabbitmq.queue.send}")
    private String rabbitmqQueueSendName;

    @Value("${sms.mq.rabbitmq.queue.query}")
    private String rabbitmqQueueQueryName;

    @Value("${sms.retrieve.maxCount}")
    private Integer smsRetrieveMaxCount;

    /**
     * topicRetrieveExchange
     */
    private Exchange topicRetrieveExchange = null;

    private static final String _SPLIT = ",";

    /**
     * SIMPLEDATEFORMAT
     */
    public static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

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

    protected static List<SmsLog> getLogList() {
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
            Exchange exchangeSend = new DirectExchange(rabbitmqExchangeDirectSendName, false, false);
            rabbitAdmin.declareExchange(exchangeSend);
            Exchange exchangeQuery = new DirectExchange(rabbitmqExchangeDirectQueryName, false, false);
            rabbitAdmin.declareExchange(exchangeQuery);
            Exchange exchangeLog = new TopicExchange(rabbitmqExchangeTopicLogName, false, false);
            rabbitAdmin.declareExchange(exchangeLog);
            topicRetrieveExchange = ExchangeBuilder.topicExchange(rabbitmqExchangeTopicRetrieveName).durable(false).build();
            rabbitAdmin.declareExchange(topicRetrieveExchange);
            // declareQueue
            Queue queueLog = new Queue(rabbitmqQueueLogName);
            rabbitAdmin.declareQueue(queueLog);
            Queue queueSend = new Queue(rabbitmqQueueSendName);
            // rabbitAdmin.declareQueue(queueSend);
            Queue queueRetrieve = declareRetrieveQueue(rabbitAdmin, queueSend, rabbitmqQueueSendName, rabbitmqExchangeDirectSendName);
            rabbitAdmin.declareQueue(queueRetrieve);
            Queue queueQuery = new Queue(rabbitmqQueueQueryName);
            rabbitAdmin.declareQueue(queueQuery);
            // declareBinding
            rabbitAdmin.declareBinding(BindingBuilder.bind(queueSend).to(exchangeSend).with("send").noargs());
            // rabbitAdmin.declareBinding(BindingBuilder.bind(queueLog).to(exchangeQuery).with("query").noargs());
            rabbitAdmin.declareBinding(BindingBuilder.bind(queueLog).to(exchangeLog).with("log.#").noargs());
            // rabbitAdmin
            return rabbitAdmin;
        }catch(Exception ex){
            // log
            log.error(ex.getMessage());
            return null;
        }
    }

    public Queue declareRetrieveQueue(RabbitAdmin rabbitAdmin, Queue queue, String queueName, String exchangeName) {
        queue.addArgument("x-dead-letter-exchange", rabbitmqExchangeTopicRetrieveName);
        rabbitAdmin.declareQueue(queue);
        Queue retrieveQueue = new Queue(queueName + "RetrieveQueue", true, false, false, new HashMap(){{
            put("x-dead-letter-exchange", exchangeName);
            put("x-message-ttl", 10000);
        }});
        rabbitAdmin.declareQueue(retrieveQueue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(retrieveQueue).to(topicRetrieveExchange).with("send").noargs());
        return retrieveQueue;
    }

    private void getExchange(String exchangeName){
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

        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, @Nullable String cause)->{
            log.info("setConfirmCallback");
        });
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey)->{
            log.info("setReturnCallback");
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
    public void sendRabbitMessage(String exchange, String routingKey, @NotNull Object message) {
        log.info("sendRabbitMessage");
        try{
            getRabbitTemplate().convertAndSend(exchange, routingKey, JSON.toJSONString(message));
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        log.info(new HashMap(){{
            put("exchange", exchange);
            put("routingKey", routingKey);
            put("message", message);
        }}.toString());
    }

    /**
     * pushEvent
     *
     * @param smsEventEnum
     * @param message
     */
    public void pushEvent(SmsEventEnum smsEventEnum, Object message) {
        String exchange = "";
        String routingKey = "";
        switch (smsEventEnum) {
            case SEND_SMS:
                exchange = rabbitmqExchangeDirectSendName;
                routingKey = "send";
                break;
            case SEND_SMS_TRUE:
                exchange = rabbitmqExchangeTopicLogName;
                routingKey = "log.sms.send.true";
                break;
            case SEND_SMS_FALSE:
                exchange = rabbitmqExchangeTopicRetrieveName;
                routingKey = "retrieve.send";
                break;
            default:
                break;
        }
        sendRabbitMessage(exchange, routingKey, message);
    }

    /**
     * SmsRequestClient
     *
     * @return SmsRequestClient
     */
    public @Bean SmsRequestClient smsUser() throws NoSuchMethodException {
        return ProxyFactory.genProxy(
            (SmsRequestClient) new SmsUser(),
            new ProxyEntry[]{
                new ProxyEntry(){{
                    setClazz(SmsUser.class);
                    setMethod(SmsUser.class.getDeclaredMethod("sendSmsNotice", SmsEnum.class, AliSmsNoticeDTO.class));
                    // setProBefAspect

                    setProAftAspect((proxyEntry)->{
                        // 短信发送状态
                        String smsResultData = (String) ((ProxyEntry) proxyEntry).getResultData();

                        log.info("短信发送状态: " + smsResultData);
                        SmsQuery smsQuery = JSON.parseObject(JSON.toJSONString(((ProxyEntry) proxyEntry).getArgs()), SmsQuery.class);
                        // 记录短信发送日志
                        final LogSaveQuery logSaveQuery = new LogSaveQuery(){{
                            setDate(SIMPLEDATEFORMAT.format(Calendar.getInstance().getTime()));
                            // setSmsLogTypeEnum(SmsLogTypeEnum.SMS_SEND_RESULT);
                            setSmsQuery(JSON.toJSONString(((ProxyEntry) proxyEntry).getArgs()));
                        }};
                        // ((SmsConfig) applicationContext.getBean("smsConfig")).getLogList().stream().forEach(v->v.log(logSaveQuery));
                        boolean smsResult = smsResultData != null;

                        // 短信发送异常
                        if(!smsResult) {
                            return SmsEventEnum.SEND_SMS_FALSE;
                        }else{
                            return SmsEventEnum.SEND_SMS_TRUE;
                        }
                    });
                }},
                new ProxyEntry(){{
                    setClazz(SmsUser.class);
                    setMethod(SmsUser.class.getDeclaredMethod("sendSmsValidCode", SmsEnum.class, AliSmsValidCodeDTO.class));
                    setProAftAspect((proxyEntry)->{
                        // 短信发送状态
                        Map smsResultData = (Map) ((ProxyEntry) proxyEntry).getResultData();
                        log.info("短信发送状态: " + smsResultData);
                        // 记录短信发送日志
                        getLogList().stream().forEach(v->v.log(new LogSaveQuery(
                                SIMPLEDATEFORMAT.format(Calendar.getInstance().getTime()),
                                SmsLogTypeEnum.SMS_SEND_RESULT,
                                JSON.toJSONString(smsResultData))));
                        // smsResult
                        boolean smsResult = smsResultData != null && !String.valueOf(smsResultData.get("Message")).contains("invalid");
                        // 短信发送异常
                        if(!smsResult) {
                            return SmsEventEnum.SEND_SMS_FALSE;
                        }else{
                            return SmsEventEnum.SEND_SMS_TRUE;
                        }
                    });
                }}
            }
        )
        .genProxy();
    }

    public void rabbitmqQueueLogNameListener() {
    }

    /**
     * messageListenerSendSms
     *
     * @param message
     */
    private SmsEventEnum messageListenerSendSms(Message message) throws NoSuchMethodException {
        // 短信发送的重试次数小于 maxCount
        int count = getSmsRetrieveCount(message);
        if(smsRetrieveMaxCount <= count) {
            return SmsEventEnum.SEND_SMS_FALSE_RETRIEVE_MAX_COUNT;
        }
        SmsRequestClient smsRequestClient = applicationContext.getBean(SmsRequestClient.class);
        AliSmsValidCodeDTO aliSmsValidCodeDTO = JSON.parseObject(message.getBody(), AliSmsValidCodeDTO.class);
        SmsEventEnum smsEventEnum = null;
        try{
            smsEventEnum = smsRequestClient.sendSmsValidCode(SmsEnum.Valid_Code_Sms_01, aliSmsValidCodeDTO);
        }catch(Exception ex) {
            if(smsRetrieveMaxCount > count) {
                return SmsEventEnum.SEND_SMS_FALSE_RETRIEVE;
            }
            return SmsEventEnum.SEND_SMS_FALSE;
        }
        if(smsEventEnum == SmsEventEnum.SEND_SMS_FALSE && smsRetrieveMaxCount > count){
            return SmsEventEnum.SEND_SMS_FALSE_RETRIEVE;
        }else{
            return smsEventEnum;
        }
    }

    /**
     * 短信发送的重试次数
     *
     * @param message
     * @return
     */
    private int getSmsRetrieveCount(Message message) {
        return message.getMessageProperties().getXDeathHeader() == null ? 0 : message.getMessageProperties().getXDeathHeader().size();
    }

    /**
     * messageListener
     *
     * @param channel
     * @param consumerQueue
     * @param deliveryTag
     * @param message
     */
    public void messageListener(Channel channel, String consumerQueue, long deliveryTag, Message message) throws Exception {
        // rabbitmqQueueLogName
        if(rabbitmqQueueLogName.equals(consumerQueue)){
            throw new RuntimeException("consumerQueue" + message.toString());
        }
        // rabbitmqQueueQueryName
        if(rabbitmqQueueQueryName.equals(consumerQueue)){
        }
        // rabbitmqQueueSendName
        if(rabbitmqQueueSendName.equals(consumerQueue)){
            log.info(rabbitmqQueueSendName + message.getBody().toString());
            SmsEventEnum smsEventEnum = messageListenerSendSms(message);
            SmsLogTypeEnum smsLogTypeEnum = null;
            if(smsEventEnum == SmsEventEnum.SEND_SMS_TRUE){
                smsLogTypeEnum = SmsLogTypeEnum.SMS_SEND_RESULT_TRUE;
                channel.basicAck(deliveryTag, false);
            }else if(smsEventEnum == SmsEventEnum.SEND_SMS_FALSE_RETRIEVE){
                smsLogTypeEnum = SmsLogTypeEnum.SMS_SEND_RETRIEVE;
                channel.basicNack(deliveryTag, false, false);
            }else if(smsEventEnum == SmsEventEnum.SEND_SMS_FALSE_RETRIEVE_MAX_COUNT){
                channel.basicAck(deliveryTag, false);
                smsLogTypeEnum = SmsLogTypeEnum.SMS_SEND_RESULT_FALSE;
            }
            final SmsLogTypeEnum smsLogType = smsLogTypeEnum;
            // 保存短信发送日志
            getLogList().stream().forEach(v->{
                v.log(new LogSaveQuery(
                        SIMPLEDATEFORMAT.format(Calendar.getInstance().getTime()),
                        smsLogType,
                        JSON.toJSONString(message)
                ));
            });
        }
    }

    public @Bean SimpleMessageListenerContainer simpleMessageListenerContainer(SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory) {
        SimpleRabbitListenerEndpoint simpleRabbitListenerEndpoint = new SimpleRabbitListenerEndpoint();
        // setMessageListener
        simpleRabbitListenerEndpoint.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            String consumerQueue = message.getMessageProperties().getConsumerQueue();
            messageListener(channel, consumerQueue, deliveryTag, message);
            log.info(new String(message.getBody(), Charsets.UTF_8));
        });
        // setId
        simpleRabbitListenerEndpoint.setId(String.valueOf(UUID.randomUUID()));
        simpleRabbitListenerEndpoint.setAckMode(AcknowledgeMode.MANUAL);
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(5);
        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(10);
        // simpleMessageListenerContainer
        SimpleMessageListenerContainer simpleMessageListenerContainer = simpleRabbitListenerContainerFactory.createListenerContainer(simpleRabbitListenerEndpoint);
        // setQueueNames
        simpleMessageListenerContainer.setQueueNames(rabbitmqQueueLogName, rabbitmqQueueQueryName, rabbitmqQueueSendName);
        // setAdviceChain
        simpleMessageListenerContainer.setAdviceChain(retrieve());
        return simpleMessageListenerContainer;
    }

    private RetryOperationsInterceptor retrieve() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(2));
        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy(){{
            setInitialInterval(10000);
            setMaxInterval(100 * 1000L);
            setMultiplier(2);
        }});
        retryTemplate.registerListener(new RetryListenerSupport() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                log.info("Retrieve：1");
                log.info(String.valueOf(System.currentTimeMillis()));
                return super.open(context, callback);
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.info("Retrieve：close");
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.info("Retrieve：onError");
            }
        });
        return RetryInterceptorBuilder.stateless().retryOperations(retryTemplate).recoverer(new ImmediateRequeueMessageRecoverer()).build();
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

    public String getRabbitmqQueueQueryName() {
        return rabbitmqQueueQueryName;
    }

    public void setRabbitmqQueueQueryName(String rabbitmqQueueQueryName) {
        this.rabbitmqQueueQueryName = rabbitmqQueueQueryName;
    }

    public void setLogList(List<SmsLog> logList) {
        SmsConfig.logList = logList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}