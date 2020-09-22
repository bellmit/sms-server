/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: ProxyTemplate
 * Author:   Administrator
 * Date:     2020/9/22 9:39
 * Description: ProxyTemplate
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 〈ProxyTemplate〉
 *
 * @author xao
 * @date 2020/9/22
 * @since 1.0.0
 */
public class ProxyTemplate<T> implements InvocationHandler {
    private T proxyTarget;

    private ProxyEntry[] proxyEntry;

    public T getProxyTarget() {
        return proxyTarget;
    }

    public void setProxyTarget(T proxyTarget) {
        this.proxyTarget = proxyTarget;
    }

    public ProxyEntry[] getProxyEntry() {
        return proxyEntry;
    }

    public void setProxyEntry(ProxyEntry[] proxyEntry) {
        this.proxyEntry = proxyEntry;
    }

    public ProxyEntry getProxy(Method method) {
        return Arrays.asList(proxyEntry).stream().filter(v->v.getMethod().getName() == method.getName()).collect(Collectors.toList()).get(0);
    }

    public T genProxy() {
        return (T) Proxy.newProxyInstance(proxyTarget.getClass().getClassLoader(), proxyTarget.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke origin: " + System.currentTimeMillis());
        try{
            ProxyEntry proxyEntry = getProxy(method);
            proxyEntry.setResultData(method.invoke(proxyTarget, args));
            proxyEntry.setArgs(args);

            Object invoke_result = proxyEntry.getProAftAspect().apply(proxyEntry);
            System.out.println("invoke enhancer: " + System.currentTimeMillis());
            return invoke_result;
        }catch (Exception ex){
            return method.invoke(proxyTarget, args);
        }
    }
}