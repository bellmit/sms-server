/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: MessageController
 * Author:   xao
 * Date:     2020/9/27 8:35
 * Description: 消息controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.feign;

import com.bim.marvel.common.core.model.dto.SimplePageVO;
import com.bim.marvel.common.core.model.dto.SimpleVO;
import com.bim.marvel.common.core.model.query.PageQuery;
import com.bim.marvel.message.cent.query.MessageSaveQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 〈消息controller〉
 *
 * @author xao
 * @date 2020/9/25
 * @since 1.0.0
 */
public interface MessageController {

    /**
     * 发布消息
     */
    String ADD_MESSAGE = "/api/message/add";

    /**
     * 查看消息列表
     */
    String LIST_MESSAGE = "/api/message/list";

    /**
     * 发布消息
     *
     * @throws Exception
     */
    @ApiOperation(
            value = "发布消息",
            notes = "发布消息",
            protocols = "http,https", httpMethod = "POST")
    @PostMapping(value = ADD_MESSAGE, produces = {MediaType.APPLICATION_JSON_VALUE})
    SimpleVO addMessage(MessageSaveQuery messageSaveQuery) throws Exception;

    /**
     * 查看消息列表
     */
    @ApiOperation(
            value = "查看消息列表",
            notes = "查看消息列表",
            protocols = "http,https", httpMethod = "GET")
    @PostMapping(value = LIST_MESSAGE, produces = {MediaType.APPLICATION_JSON_VALUE})
    SimplePageVO listMessage(PageQuery pageQuery);
}