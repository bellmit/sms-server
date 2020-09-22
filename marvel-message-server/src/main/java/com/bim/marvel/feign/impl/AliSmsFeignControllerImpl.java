/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsFeignControllerImpl
 * Author:   xao
 * Date:     2020/9/11 15:08
 * Description: 阿里云短信平台controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.feign.impl;

import com.bim.marvel.message.sms.client.SmsRequestClient;
import com.bim.marvel.message.sms.client.SmsUser;
import com.bim.marvel.message.sms.config.AliSmsConfig;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * 〈阿里云短信平台controller〉
 *
 * @author xao
 * @date 2020/9/14
 * @since 1.0.0
 */
@RestController
public class AliSmsFeignControllerImpl {

    /**
     * smsRequestClient
     */
    @Autowired
    private AliSmsConfig aliSmsConfig;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 发送短信
     *
     * @param aliSmsNoticeDTO 短信参数
     * @throws Exception Exception
     */
//    @Override
//    public void sendSmsFeign() throws Exception {
//        aliSmsConfig.smsUser().sendSmsNotice(SmsEnum.Valid_Code_Sms_01, null);
//    }

    /**
     * 发送短信
     *
     * @param aliSmsValidCodeDTO 短信参数
     * @throws Exception Exception
     */
    public void sendSmsFeign(@Valid AliSmsValidCodeDTO aliSmsValidCodeDTO) throws Exception {
    }

    @GetMapping("/sendNoticeSms")
    public void sendNoticeSms(@Valid AliSmsNoticeDTO aliSmsNoticeDTO) throws Exception {
        aliSmsConfig.smsUser().sendRequestSmsNotice(SmsEnum.Valid_Code_Sms_01, aliSmsNoticeDTO, applicationContext);
    }
}