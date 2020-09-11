/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsFeignControllerImpl
 * Author:   Administrator
 * Date:     2020/9/11 15:08
 * Description: 阿里云短信平台controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.feign.impl;

import com.bim.marvel.feign.AliSmsFeignController;
import com.bim.marvel.message.sms.dto.AliSmsRequestDTO;
import com.bim.marvel.message.sms.entity.AliSmsConfig;
import com.bim.marvel.message.sms.util.AliSmsUtil;
import org.springframework.beans.factory.annotation.Value;

/**
 * 〈阿里云短信平台controller〉
 *
 * @author xao
 * @date 2020/9/11
 * @since 1.0.0
 */
public class AliSmsFeignControllerImpl implements AliSmsFeignController {

    /**
     * 阿里云accessKey
     */
    @Value("sms.aliSms.accessKey")
    public String aliSmsAccessKey;

    /**
     * 阿里云accessSecret
     */
    @Value("sms.aliSms.accessSecret")
    public String aliSmsAccessSecret;

    /**
     * 阿里云aliSmsConfig
     */
    public static AliSmsConfig aliSmsConfig = null;

    /**
     * AliSmsFeignControllerImpl
     */
    public AliSmsFeignControllerImpl(){
        aliSmsConfig = new AliSmsConfig(aliSmsAccessKey, aliSmsAccessSecret);
    }

    /**
     * 发送短信
     * @param aliSmsRequestDTO 短信参数
     */
    @Override
    public void sendSmsFeign(AliSmsRequestDTO aliSmsRequestDTO) throws Exception {
        AliSmsUtil.sendAliSms(aliSmsConfig, aliSmsRequestDTO);
    }
}