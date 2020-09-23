/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsQuery
 * Author:   xao
 * Date:     2020/9/15 15:00
 * Description: 短信餐宿
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 〈短信参数〉
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsQuery {
    /**
     * 号码
     */
    private String phone;

    /**
     * 号码
     */
    private String phoneNumbers;
}