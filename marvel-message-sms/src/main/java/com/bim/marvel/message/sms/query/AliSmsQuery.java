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
package com.bim.marvel.message.sms.query;

import com.bim.marvel.message.sms.config.AliSmsConfig;
import com.bim.marvel.message.sms.enums.SmsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 〈阿里云短信〉
 *
 * @author xao
 * @date 2020/9/14
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliSmsQuery extends SmsQuery implements Serializable {

    /**
     * 短信参数 signatureNonce
     */
    private String signatureNonce;

    /**
     * 短信参数 timestamp
     */
    private String timestamp;

    /**
     * 短信参数 regionId
     */
    private String regionId;

    /**
     * 短信参数 version
     */
    private String version;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板
     */
    private String templateCode;

    /**
     * 参数
     */
    private String templateParam;

    /**
     * aliSmsConfig
     */
    private AliSmsConfig aliSmsConfig;
}