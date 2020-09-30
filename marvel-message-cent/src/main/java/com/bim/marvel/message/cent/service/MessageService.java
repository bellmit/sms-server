/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: MessageService
 * Author:   Administrator
 * Date:     2020/9/27 11:28
 * Description: 消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.cent.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bim.marvel.common.core.model.query.PageQuery;
import com.bim.marvel.common.util.SimpleConverter;
import com.bim.marvel.message.cent.dao.MessageDAO;
import com.bim.marvel.message.cent.entity.MessageDO;
import com.bim.marvel.message.cent.query.MessageQuery;
import com.bim.marvel.message.cent.query.MessageSaveQuery;
import org.springframework.stereotype.Service;

/**
 * 〈消息 MessageService〉
 *
 * @author xao
 * @date 2020/9/27
 * @since 1.0.0
 */
@Service
public class MessageService extends ServiceImpl<MessageDAO, MessageDO> {

    /**
     * 发布消息
     *
     * @param messageSaveQuery 消息参数
     * @throws Exception
     */
    public boolean addMessage(MessageSaveQuery messageSaveQuery) throws Exception {
        MessageDO messageDO = SimpleConverter.convert(messageSaveQuery, MessageDO.class);
        return save(messageDO);
    }

    /**
     * 查看消息列表
     *
     * @param pageQuery 分页参数
     */
    public IPage<MessageQuery> listMessage(PageQuery pageQuery) {
        IPage<MessageDO> messageDOIPage = page(new Page(pageQuery.getPageIndex(), pageQuery.getPageSize()));
        IPage<MessageQuery> messageSaveQueryIPage = new Page();
        messageSaveQueryIPage.setCurrent(messageDOIPage.getCurrent());
        messageSaveQueryIPage.setSize(messageDOIPage.getSize());
        messageSaveQueryIPage.setTotal(messageDOIPage.getTotal());
        messageSaveQueryIPage.setRecords(SimpleConverter.convert(messageDOIPage.getRecords(), MessageQuery.class));
        return messageSaveQueryIPage;
    }
}