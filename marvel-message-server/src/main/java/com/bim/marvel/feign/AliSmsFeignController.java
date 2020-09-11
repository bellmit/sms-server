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

import com.bim.marvel.message.sms.dto.AliSmsRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈阿里云短信平台接口〉
 *
 * @author xao
 * @date 2020/9/11
 * @since 1.0.0
 */
@RestController
@FeignClient(name = "aliSms", url = "http://dysmsapi.aliyuncs.com")
public interface AliSmsFeignController {

    /**
     * 阿里云accessKey
     */
    String ALI_SMS_ACCESS_KEY = "";

    /**
     * 阿里云accessSecret
     */
    String ALI_SMS_ACCESS_SECRET = "";

    /**
     * 发送短信
     */
    String SEND_ALI_SMS = "/api/sendSms";

    /**
     * 发送短信
     * @param aliSmsRequestDTO 短信参数
     * @return ResponseEntity<Map> 返回短信发送结果
     * @exception Exception 异常信息
     */
    @ApiOperation(
            value = "查询用户反馈信息分页列表",
            notes = "查询用户反馈信息分页列表",
            protocols = "http,https", httpMethod = "GET")
    @GetMapping(value = SEND_ALI_SMS, produces = {MediaType.APPLICATION_JSON_VALUE})
    void sendSmsFeign(AliSmsRequestDTO aliSmsRequestDTO) throws Exception;
}