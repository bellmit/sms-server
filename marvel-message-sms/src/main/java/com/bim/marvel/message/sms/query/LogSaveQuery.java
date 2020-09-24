/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: LogPageQuery
 * Author:   xao
 * Date:     2020/9/16 9:27
 * Description: 分页日志
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.query;

import com.bim.marvel.message.sms.enums.SmsLogTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 〈新增日志〉
 *
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "LogSaveQuery")
public class LogSaveQuery {

    /**
     * date
     */
    private Date date;

    /**
     * logTypeEnum
     */
    private SmsLogTypeEnum smsLogTypeEnum;

    /**
     * smsQuery
     */
    private String smsQuery;
}