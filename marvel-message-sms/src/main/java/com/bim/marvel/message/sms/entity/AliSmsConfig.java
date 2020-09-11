/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsConfig
 * Author:   xao
 * Date:     2020/9/11 17:06
 * Description: 阿里云短信配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 〈阿里云短信配置〉
 *
 * @author xao
 * @date 2020/9/11
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class AliSmsConfig {
    /**
     * accessKey
     */
    private String accessKey;

    /**
     * accessSecret
     */
    private String accessSecret;
}
