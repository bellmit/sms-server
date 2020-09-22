/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: ProxyEntry
 * Author:   Administrator
 * Date:     2020/9/22 9:36
 * Description: ProxyEntry
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.util;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 〈ProxyEntry〉
 *
 * @author xao
 * @date 2020/9/22
 * @since 1.0.0
 */
public class ProxyEntry {
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