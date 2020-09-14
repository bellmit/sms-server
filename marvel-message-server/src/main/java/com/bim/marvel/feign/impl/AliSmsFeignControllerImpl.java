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

import com.bim.marvel.feign.AliSmsFeignController;
import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import com.bim.marvel.message.sms.util.AliSmsUtil;
import javax.validation.Valid;

/**
 * 〈阿里云短信平台controller〉
 *
 * @author xao
 * @date 2020/9/14
 * @since 1.0.0
 */
public class AliSmsFeignControllerImpl implements AliSmsFeignController {

    /**
     * 发送短信
     *
     * @param aliSmsNoticeDTO 短信参数
     * @throws Exception Exception
     */
    @Override
    public void sendSmsFeign(AliSmsNoticeDTO aliSmsNoticeDTO) throws Exception {
        AliSmsUtil.sendAliSmsNotice(aliSmsNoticeDTO, aliSmsNoticeDTO.getSmsEnum());
    }

    /**
     * 发送短信
     *
     * @param aliSmsValidCodeDTO 短信参数
     * @throws Exception Exception
     */
    @Override
    public void sendSmsFeign(@Valid AliSmsValidCodeDTO aliSmsValidCodeDTO) throws Exception {
        AliSmsUtil.sendAliSmsValidCode(aliSmsValidCodeDTO, aliSmsValidCodeDTO.getSmsEnum());
    }
}