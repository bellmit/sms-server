/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: MessageDO
 * Author:   xao
 * Date:     2020/9/25 11:38
 * Description: 消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.cent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 〈消息〉
 *
 * @author xao
 * @date 2020/9/25
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TAB_SYSTEM_MESSAGE")
public class MessageDO {

    /**
     * 消息编码
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 消息状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 消息标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 消息类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 跳转链接
     */
    @TableField(value = "url")
    private String url;

    /**
     * 发布日期
     */
    @TableField(value = "publishDate")
    private Date publishDate;

    /**
     * 发布用户
     */
    @TableField(value = "publishLoginId")
    private Long publishLoginId;

    /**
     * 编辑日期
     */
    @TableField(value = "editDate")
    private Date editDate;

    /**
     * 编辑用户
     */
    @TableField(value = "editLoginId")
    private Long editLoginId;
}