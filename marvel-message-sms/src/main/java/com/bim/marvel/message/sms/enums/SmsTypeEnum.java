/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsTypeEnum
 * Author:   xao
 * Date:     2020/9/11 17:09
 * Description: 短信类型
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.enums;

import com.bim.marvel.message.sms.entity.AliSmsConfig;

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
    SmsValidCode("1", "验证码短息", null),

    /**
     * 通知短信
     */
    SmsNotice("2", "通知短信", null);

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String desc;

    /**
     * 短息配置参数
     */
    private AliSmsConfig aliSmsConfig;

    SmsTypeEnum(String value, String desc, AliSmsConfig aliSmsConfig) {
        this.value = value;
        this.desc = desc;
        this.aliSmsConfig = aliSmsConfig;
    }
}