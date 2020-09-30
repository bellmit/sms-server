/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: MessageSaveQuery
 * Author:   xao
 * Date:     2020/9/25 17:06
 * Description: 阿里云短信配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.cent.query;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 〈阿里云短信〉
 *
 * @author xao
 * @date 2020/9/25
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageQuery implements Serializable {

    /**
     * 消息编码
     */
    private Long id;

    /**
     * 消息标题
     */
    @NotNull
    private String title;

    /**
     * 消息内容
     */
    @NotNull
    private String content;

    /**
     * 消息类型
     */
    @NotNull
    private Integer type;

    /**
     * 跳转链接
     */
    private String url;

    /**
     * 发布日期
     */
    private Date publishDate;

    /**
     * 发布用户
     */
    @NotNull
    private Long MessageSaveQuery;
}