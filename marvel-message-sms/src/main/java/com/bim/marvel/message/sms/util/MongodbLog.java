/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: MongodbLog
 * Author:   xao
 * Date:     2020/9/15 15:24
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.util;

import com.bim.marvel.message.sms.query.LogPageQuery;
import com.bim.marvel.message.sms.query.LogSaveQuery;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * MongodbLog
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class MongodbLog implements SmsLog {

    /**
     * url
     */
    private String url;

    /**
     * mongoTemplate
     */
    private MongoTemplate mongoTemplate;

    public MongodbLog(String url, MongoTemplate mongoTemplate) {
        this.url = url;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 新增日志
     *
     * @param logSaveQuery
     */
    @Override
    public LogSaveQuery log(LogSaveQuery logSaveQuery) {
        return mongoTemplate.insert(logSaveQuery);
    }

    /**
     * 查询分页日志
     *
     * @param logPageQuery
     */
    @Override
    public void listPageLog(LogPageQuery logPageQuery) {
    }
}