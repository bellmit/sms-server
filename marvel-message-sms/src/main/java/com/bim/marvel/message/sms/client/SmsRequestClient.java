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

/**
 * 〈短信服务客户端〉
 *
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
public interface SmsRequestClient {

    void sendSmsNotice(SmsEnum smsEnum, AliSmsNoticeDTO aliSmsNoticeDTO);

    void sendSmsValidCode(SmsEnum smsEnum, AliSmsValidCodeDTO aliSmsValidCodeDTO);
}