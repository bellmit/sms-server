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
import java.util.Date;

/**
 * 〈分页日志〉
 *
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogPageQuery {

    /**
     * pageIndex
     */
    private Integer pageIndex;

    /**
     * pageSize
     */
    private Integer pageSize;

    /**
     * fromDate
     */
    private Date fromDate;

    /**
     * toDate
     */
    private Date toDate;

    /**
     * logTypeEnum
     */
    private SmsLogTypeEnum smsLogTypeEnum;
}