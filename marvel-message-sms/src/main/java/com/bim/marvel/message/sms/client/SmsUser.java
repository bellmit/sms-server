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

import com.bim.marvel.message.sms.config.AliSmsConfig;
import com.bim.marvel.message.sms.config.SmsConfig;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.enums.SmsEventEnum;
import com.bim.marvel.message.sms.util.aliSms.AliSmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * SmsUser
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
@Slf4j
public class SmsUser implements SmsRequestClient {

    /**
     * 发送通知短息
     *
     * @param smsEnum
     */
    @Override
    public Long sendRequestSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO, ApplicationContext applicationContext) throws Exception {
        if(aliSmsNoticeDTO == null || StringUtils.isEmpty(aliSmsNoticeDTO.getPhoneNumbers())){
            throw new RuntimeException("号码不可为空值");
        }
        applicationContext.getBean(AliSmsConfig.class).pushEvent(SmsEventEnum.SEND_SMS, aliSmsNoticeDTO);
        log.info("pushEvent" + SmsEventEnum.SEND_SMS);
        return null;
    }

    @Override
    public <T> T sendSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO) throws Exception {
        return (T) AliSmsUtil.sendAliSmsNotice(aliSmsNoticeDTO, smsEnum);
    }

    /**
     * 发送通知短息
     *
     * @param smsEnum
     */
    @Override
    public Long sendRequestSmsValidCode(SmsEnum smsEnum, AliSmsValidCodeDTO aliSmsValidCodeDTO) {
        return null;
    }

    public void sendSms(){
    }
}