/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsEventEnum
 * Author:   xao
 * Date:     2020/9/16
 * Description: 短信事件类型
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.enums;

/**
 * 〈短信事件类型〉
 *
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
public enum  SmsEventEnum {

    /**
     * 发送短信
     */
    SEND_SMS("sendSms", "发送短信"),

    /**
     * 发送短信正常
     */
    SEND_SMS_TRUE("sendSmsTrue", "发送短信正常"),

    /**
     * 发送短信异常
     */
    SEND_SMS_FALSE("sendSmsFalse", "发送短信异常");

    /**
     * value
     */
    private String value;

    /**
     * desc
     */
    private String desc;

    SmsEventEnum(String value, String desc) {
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
}