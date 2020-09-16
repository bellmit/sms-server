/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsFeignClient
 * Author:   xao
 * Date:     2020/9/16
 * Description: 阿里短信接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈阿里短信接口〉
 *
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
@FeignClient(name = "aliSms", url = "http://dysmsapi.aliyuncs.com")
public interface AliSmsFeignClient {

    /**
     * 发送短信Feign
     *
     * @param signature 短信签名
     * @param param 短信参数
     */
    @GetMapping
    void sendAliSms(@RequestParam("Signature") String signature,
                    @RequestParam("param") String param);
}