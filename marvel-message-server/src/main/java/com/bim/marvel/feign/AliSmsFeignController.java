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

import com.bim.marvel.message.sms.AliSmsUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

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
     * 阿里云accessKey
     */
    String aliSmsAccessKey = "";

    /**
     * 阿里云accessSecret
     */
    String aliSmsAccessSecret = "";

    /**
     * 发送短信
     */
    String SEND_ALI_SMS = "/api/sendSms";

    /**
     * 发送短信
     * @param aliSmsRequestDTO 短信参数
     */
    @ApiOperation(
            value = "查询用户反馈信息分页列表",
            notes = "查询用户反馈信息分页列表",
            protocols = "http,https", httpMethod = "GET")
    @GetMapping(value = SEND_ALI_SMS, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<Map> sendSmsFeign(AliSmsUtil.AliSmsRequestDTO aliSmsRequestDTO) throws Exception;
}