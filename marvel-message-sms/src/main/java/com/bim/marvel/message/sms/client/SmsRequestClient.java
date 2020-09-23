/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsRequest
 * Author:   xao
 * Date:     2020/9/16 11:10
 * Description: 短信服务客户端
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.client;

import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.enums.SmsEnum;
import org.springframework.context.ApplicationContext;

/**
 * 〈短信服务客户端〉
 *
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
public interface SmsRequestClient {

    /**
     * sendRequestSmsNotice
     *
     * @param smsEnum
     * @param aliSmsNoticeDTO
     * @return
     * @throws Exception
     */
    Long sendRequestSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO, ApplicationContext applicationContext) throws Exception;

    /**
     * sendRequestSmsValidCode
     *
     * @param smsEnum
     * @param aliSmsValidCodeDTO
     * @return
     */
    Long sendRequestSmsValidCode(SmsEnum smsEnum, AliSmsValidCodeDTO aliSmsValidCodeDTO, ApplicationContext applicationContext);

    /**
     * sendSmsNotice
     *
     * @param smsEnum
     * @param aliSmsNoticeDTO
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T sendSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO) throws Exception;

    /**
     *
     * @param smsEnum
     * @param aliSmsValidCodeDTO
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T sendSmsValidCode(SmsEnum smsEnum, AliSmsValidCodeDTO aliSmsValidCodeDTO) throws Exception;
}