/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsFeignController
 * Author:   Administrator
 * Date:     2020/9/11 14:24
 * Description: 阿里云短信平台接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.feign;

import com.bim.marvel.message.sms.dto.AliSmsNoticeDTO;
import com.bim.marvel.message.sms.dto.AliSmsValidCodeDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * 〈阿里云短信平台接口〉
 *
 * @author xao
 * @date 2020/9/11
 * @since 1.0.0
 */
@RestController
public interface AliSmsFeignController {

    /**
     * 发送短信
     */
    String SEND_ALI_SMS_NOTICE = "/api/sendNoticeSms/";

    /**
     * 发送短信
     */
    String SEND_ALI_SMS_VALID_CODE = "/api/sendValidCodeSms/";

    /**
     * 发送通知短信
     *
     * @param aliSmsNoticeDTO 短信参数
     * @return ResponseEntity<Map> 返回短信发送结果
     * @exception Exception 异常信息
     */
    @ApiOperation(
            value = "发送通知短信",
            notes = "发送通知短信",
            protocols = "http,https", httpMethod = "GET")
    @GetMapping(value = SEND_ALI_SMS_NOTICE, produces = {MediaType.APPLICATION_JSON_VALUE})
    void sendSmsFeign(@Valid AliSmsNoticeDTO aliSmsNoticeDTO) throws Exception;

    /**
     * 发送验证码短信
     *
     * @param aliSmsValidCodeDTO 短信参数
     * @return ResponseEntity<Map> 返回短信发送结果
     * @exception Exception 异常信息
     */
    @ApiOperation(
            value = "发送验证码短信",
            notes = "发送验证码短信",
            protocols = "http,https", httpMethod = "GET")
    @GetMapping(value = SEND_ALI_SMS_VALID_CODE, produces = {MediaType.APPLICATION_JSON_VALUE})
    void sendSmsFeign(@Valid AliSmsValidCodeDTO aliSmsValidCodeDTO) throws Exception;
}