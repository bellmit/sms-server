 /*
  * Copyright (C), 2020, 安徽贝慕信息科技有限公司
  * FileName: MessageDAO
  * Author:   Administrator
  * Date:     2020/9/25 11:51
  * Description: 消息
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.bim.marvel.message.cent.dao;

 import com.baomidou.mybatisplus.core.mapper.BaseMapper;
 import com.bim.marvel.message.cent.entity.MessageDO;
 import org.apache.ibatis.annotations.Mapper;

 /**
  * 〈消息〉
  *
  * @author xao
  * @date 2020/9/25
  * @since 1.0.0
  */
 @Mapper
 public interface MessageDAO extends BaseMapper<MessageDO> {
 }