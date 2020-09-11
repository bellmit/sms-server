/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsFeignClient
 * Author:   Administrator
 * Date:     2020/9/11 17:31
 * Description: 阿里短信接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.feign;

import feign.Param;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 〈阿里短信接口〉
 *
 * @author xao
 * @date 2020/9/11
 * @since 1.0.0
 */
public interface AliSmsFeignClient {

    /**
     * 发送短信Feign
     *
     * @param signature
     * @param param1
     * @param param2
     */
    @GetMapping("?Signature={signature}&param1={param1}&param2={param2}")
    void sendAliSms(@Param("Signature") String signature, @Param("param1") String param1, @Param("param2") String param2);
}