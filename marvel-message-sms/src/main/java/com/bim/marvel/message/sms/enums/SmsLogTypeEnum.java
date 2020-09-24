/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: LogTypeEnum
 * Author:   xao
 * Date:     2020/9/16 9:32
 * Description: 日志类型
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.enums;

/**
 * 〈日志类型〉
 *
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
public enum SmsLogTypeEnum {

    /**
     * 短信发送正常
     */
    SMS_SEND_RESULT_TRUE("SMS_SEND_RESULT_TRUE", "短信发送正常"),

    /**
     * 短信平台返回数据
     */
    SMS_SEND_RESULT("SMS_SEND_RESULT", "短信平台返回数据"),

    /**
     * 短信发送异常
     */
    SMS_SEND_RESULT_FALSE("SMS_SEND_RESULT_FALSE", "短信发送异常"),

    /**
     * 短信发送异常，重新发送
     */
    SMS_SEND_RETRIEVE("SMS_SEND_RETRIEVE", "短信发送异常，重新发送");

    /**
     * value
     */
    private String value;

    /**
     * desc
     */
    private String desc;

    SmsLogTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SmsLogTypeEnum{" +
                "value='" + value + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}