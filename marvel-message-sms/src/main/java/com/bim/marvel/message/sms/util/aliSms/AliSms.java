/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: CommonSms
 * Author:   xao
 * Date:     2020/9/15 14:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.util.aliSms;

import com.bim.marvel.message.sms.config.SmsConfig;
import com.bim.marvel.message.sms.query.SmsQuery;

/**
 * CommonSms
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
public abstract class AliSms {

    /**
     * 发送短信
     */
    public abstract void sendMessage(SmsQuery smsQuery, SmsConfig smsConfig);
}