/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: SmsLog
 * Author:   xao
 * Date:     2020/9/15 14:17
 * Description: 短信日志
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.util;

import com.bim.marvel.message.sms.query.LogPageQuery;
import com.bim.marvel.message.sms.query.LogSaveQuery;

/**
 * 〈短信日志〉
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
public interface SmsLog {

    /**
     * 新增日志
     */
    LogSaveQuery log(LogSaveQuery logSaveQuery);

    /**
     * 查询分页日志
     * @param logPageQuery
     */
    void listPageLog(LogPageQuery logPageQuery);
}