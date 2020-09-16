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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static class ProxyEntry {

        public Class clazz;

        public Method method;

        public Function proAftAspect;

        public Object[] args;

        public Object resultData;

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public Function getProAftAspect() {
            return proAftAspect;
        }

        public void setProAftAspect(Function proAftAspect) {
            this.proAftAspect = proAftAspect;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

        public Object getResultData() {
            return resultData;
        }

        public void setResultData(Object resultData) {
            this.resultData = resultData;
        }
    }

    public static class ProxyTemplate<T> implements InvocationHandler {

        private T proxyTarget;

        private ProxyFactory.ProxyEntry[] proxyEntry;

        public T getProxyTarget() {
            return proxyTarget;
        }

        public void setProxyTarget(T proxyTarget) {
            this.proxyTarget = proxyTarget;
        }

        public ProxyFactory.ProxyEntry[] getProxyEntry() {
            return proxyEntry;
        }

        public void setProxyEntry(ProxyFactory.ProxyEntry[] proxyEntry) {
            this.proxyEntry = proxyEntry;
        }

        public ProxyFactory.ProxyEntry getProxy(Method method) {
            return Arrays.asList(proxyEntry).stream().filter(v->v.getMethod() == method).collect(Collectors.toList()).get(0);
        }

        public T genProxy() {
            return (T) Proxy.newProxyInstance(proxyTarget.getClass().getClassLoader(), proxyTarget.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("invoke origin: " + System.currentTimeMillis());

            ProxyEntry proxyEntry = getProxy(method);
            proxyEntry.setResultData(method.invoke(proxyTarget, args));
            proxyEntry.setArgs(args);

            Object invoke_result = proxyEntry.getProAftAspect().apply(proxyEntry);

            System.out.println("invoke enhancer: " + System.currentTimeMillis());
            return invoke_result;
        }
    }
}