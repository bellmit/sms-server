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

import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Map;

/**
 * MongodbLog
 *
 * @author xao
 * @date 2020/9/15
 * @since 1.0.0
 */
public class MongodbLog implements SmsLog {

    private String url;
    private MongoTemplate mongoTemplate;

    public MongodbLog(String url, MongoTemplate mongoTemplate) {
        this.url = url;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void log(Map log) {
    }
}