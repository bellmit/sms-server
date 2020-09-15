/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsTypeEnum
 * Author:   xao
 * Date:     2020/9/14 17:09
 * Description: 短信类型
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.enums;

/**
 * 〈短信类型〉
 *
 * @author xao
 * @date 2020/9/11
 * @since 1.0.0
 */
public enum SmsTypeEnum {

    /**
     * 验证码短信
     */
    SmsValidCode("1", "验证码短息"),

    /**
     * 通知短信
     */
    SmsNotice("2", "通知短信");

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String desc;

    SmsTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}