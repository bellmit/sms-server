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
import com.bim.marvel.message.sms.AliSmsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * 〈阿里云短信平台controller〉
 *
 * @author Administrator
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
    public static AliSmsUtil.AliSmsConfig aliSmsConfig = null;

    /**
     * AliSmsFeignControllerImpl
     */
    public AliSmsFeignControllerImpl(){
        aliSmsConfig = new AliSmsUtil.AliSmsConfig(aliSmsAccessKey, aliSmsAccessSecret);
    }

    /**
     * 发送短信
     * @param aliSmsRequestDTO 短信参数
     */
    @Override
    public ResponseEntity<Map> sendSmsFeign(AliSmsUtil.AliSmsRequestDTO aliSmsRequestDTO) throws Exception {
        return AliSmsUtil.sendAliSms(aliSmsConfig, aliSmsRequestDTO);
    }
}