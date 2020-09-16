/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsUser
 * Author:   Administrator
 * Date:     2020/9/15 14:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.client;

import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;

/**
 * SmsUser
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
public class SmsUser implements SmsRequestClient{

    /**
     * 发送通知短息
     *
     * @param smsEnum
     */
    @Override
    public void sendSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO) {
    }

    /**
     * 发送通知短息
     *
     * @param smsEnum
     */
    @Override
    public void sendSmsValidCode(SmsEnum smsEnum, AliSmsValidCodeDTO aliSmsValidCodeDTO) {
    }
}