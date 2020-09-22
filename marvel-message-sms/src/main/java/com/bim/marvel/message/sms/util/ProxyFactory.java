/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: ProxyFactory
 * Author:   xao
 * Date:     2020/9/16 12:07
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.util;

/**
 * @author xao
 * @date 2020/9/16
 * @since 1.0.0
 */
public class ProxyFactory {

    public static <T> ProxyTemplate<T> genProxy(T proxyTarget, ProxyEntry[] proxyEntry) {
        ProxyTemplate proxyTemplate = new ProxyTemplate(){{
            setProxyTarget(proxyTarget);
            setProxyEntry(proxyEntry);
        }};
        return proxyTemplate;
    }
}