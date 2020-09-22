/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsSender
 * Author:   Administrator
 * Date:     2020/9/22 15:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.client;

import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.util.aliSms.AliSmsUtil;
import org.springframework.context.ApplicationContext;

/**
 * SmsSender
 *
 * @author xao
 * @date 2020/9/22
 * @since 1.0.0
 */
public class SmsSender implements SmsRequestClient {

    @Override
    public <T> T sendSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO) throws Exception {
        return (T) AliSmsUtil.sendAliSmsNotice(aliSmsNoticeDTO, smsEnum);
    }

    @Override
    public Long sendRequestSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO, ApplicationContext applicationContext) throws Exception {
        return null;
    }

    @Override
    public Long sendRequestSmsValidCode(SmsEnum smsEnum, AliSmsValidCodeDTO aliSmsValidCodeDTO) {
        return null;
    }
}