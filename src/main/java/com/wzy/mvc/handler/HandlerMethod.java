package com.wzy.mvc.handler;

import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * HandlerMethod对应控制器中的方法（Controller中的每个方法），也就是实际处理业务的handler
 */
public class HandlerMethod {

    private Object bean;
    private Class<?> beanType;
    private Method method;
    private List<MethodParameter> parameters;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.beanType = bean.getClass();
        this.method = method;
        this.parameters = new ArrayList<>();
        for (int i = 0; i < method.getParameterCount(); i++) {
            parameters.add(new MethodParameter(method,i));
        }

    }
}
