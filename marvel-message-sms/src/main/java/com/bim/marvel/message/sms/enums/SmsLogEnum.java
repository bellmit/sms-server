/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsLogEnum
 * Author:   xao
 * Date:     2020/9/15 15:31
 * Description: 短息日志类型
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.enums;

import com.bim.marvel.message.sms.util.MongodbLog;
import com.bim.marvel.message.sms.util.SmsLog;

/**
 * SmsLogEnum
 * 短信日志类型
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
public enum SmsLogEnum {

    Mongodb("mongodb", null);

    private String value;

    private SmsLog smsLog;

    SmsLogEnum(String value, SmsLog smsLog) {
        this.value = value;
        this.smsLog = smsLog;
    }
}