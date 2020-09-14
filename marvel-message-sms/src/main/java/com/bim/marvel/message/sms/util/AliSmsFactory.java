/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsUtil
 * Author:   xao
 * Date:     2020/9/8 11:37
 * Description: 阿里云短信工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.util;

import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.query.AliSmsQuery;
import java.util.HashMap;
import java.util.Map;

/**
 * AliSmsFactory
 * @author xao
 */
public class AliSmsFactory {

    /**
     * ALI_SMS_TYPE_MAP
     */
    private static final Map<SmsEnum, AliSmsQuery> ALI_SMS_TYPE_MAP = new HashMap();

    /**
     * getSmsByType
     * @param smsEnum
     * @return
     */
    public static AliSmsQuery getAliSmsQueryByType(SmsEnum smsEnum){
        AliSmsQuery aliSmsQuery = ALI_SMS_TYPE_MAP.get(smsEnum);
        return aliSmsQuery;
    }

    /**
     * putAliSmsQuery
     * @param smsEnum
     * @param aliSmsQuery
     */
    public static void putAliSmsQuery(SmsEnum smsEnum, AliSmsQuery aliSmsQuery){
        ALI_SMS_TYPE_MAP.put(smsEnum, aliSmsQuery);
    }
}