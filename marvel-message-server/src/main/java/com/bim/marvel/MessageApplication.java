/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: MessageApplication
 * Author:   Allen
 * Date:     2020/9/11
 * Description: 主程序入口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 〈主程序入口〉<br>
 * MessageApplication
 *
 * @author Allen
 * @date 2020/9/11
 * @since 1.0.0
 */
@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class MessageApplication {

    /**
     * java主程序入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        new SpringApplication(MessageApplication.class).run(args);
    }
}
