/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsRequestDTO
 * Author:   xao
 * Date:     2020/9/11 17:05
 * Description: 阿里云短信参数
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.dto;

import com.bim.marvel.message.sms.enums.SmsEnum;
import com.bim.marvel.message.sms.enums.SmsTypeEnum;
import lombok.Data;

/**
 * 〈阿里云短信参数〉
 *
 * @author xao
 * @date 2020/9/11
 * @since 1.0.0
 */
@Data
public class AliSmsNoticeDTO {

    /**
     * 号码
     */
    private String phoneNumbers;

    /**
     * 参数
     */
    private String templateParam;

    /**
     * smsEnum
     */
    private SmsEnum smsEnum;
}