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

import com.bim.marvel.message.sms.config.SmsConfig;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.enums.SmsEventEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SmsUser
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
@Slf4j
public class SmsUser implements SmsRequestClient{

    @Autowired
    private SmsConfig smsConfig;

    /**
     * 发送通知短息
     *
     * @param smsEnum
     */
    @Override
    public void sendSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO) throws NoSuchMethodException {
        smsConfig.pushEvent(SmsEventEnum.SEND_SMS, aliSmsNoticeDTO);
        log.info("pushEvent" + SmsEventEnum.SEND_SMS);
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