/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: MessageControllerImpl
 * Author:   xao
 * Date:     2020/9/27 11:26
 * Description: 消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bim.marvel.common.core.model.dto.SimplePageVO;
import com.bim.marvel.common.core.model.dto.SimpleVO;
import com.bim.marvel.common.core.model.query.PageQuery;
import com.bim.marvel.feign.MessageController;
import com.bim.marvel.message.cent.query.MessageQuery;
import com.bim.marvel.message.cent.query.MessageSaveQuery;
import com.bim.marvel.message.cent.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈消息〉
 *
 * @author xao
 * @date 2020/9/27
 * @since 1.0.0
 */
@RestController
public class MessageControllerImpl implements MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发布消息
     *
     * @param messageSaveQuery
     * @throws Exception
     */
    @Override
    public SimpleVO addMessage(MessageSaveQuery messageSaveQuery) throws Exception {
        return new SimpleVO(messageService.addMessage(messageSaveQuery));
    }

    /**
     * 查看消息列表
     *
     * @param pageQuery
     */
    @Override
    @GetMapping("/api/message/list")
    public SimplePageVO listMessage(PageQuery pageQuery) {
        IPage<MessageQuery> messageSaveQueryIPage = messageService.listMessage(pageQuery);
        return new SimplePageVO(messageSaveQueryIPage.getRecords(), messageSaveQueryIPage);
    }
}