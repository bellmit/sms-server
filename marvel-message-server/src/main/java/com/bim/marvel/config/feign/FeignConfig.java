/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: FeignConfig
 * Author:   Allen
 * Date:   2020年8月12日08:27:57
 * Description: seeker-Feign服务配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.config.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 〈marvel-Feign服务配置-因此本项目已引入Auth-client，
 * 其中FeignClientConfig已配置加载拦截器，故而此处只需指定扫描包即可，
 * 如果在不需要实现登陆的项目中只需单独引入Auth-feign与feign-config,
 * 无须单独配置，或直接引入feign-config取代本单独配置也可〉
 *
 * @author Allen
 * @date 2020年8月12日16:56:54
 * @since 1.0.0
 */
@Configuration
@EnableFeignClients(basePackages = "com.bim.marvel.feign")
public class FeignConfig {
}
