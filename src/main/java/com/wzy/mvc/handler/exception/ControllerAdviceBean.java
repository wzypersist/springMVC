package com.wzy.mvc.handler.exception;

import com.wzy.mvc.annotation.ControllerAdvice;
import lombok.Getter;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 1. 该类用于表示被`ControllerAdvice`标注的类，比如`TestController`被标注了`ControllerAdvice`，
 * 那么就需要构建一个ControllerAdviceBean对象，beanType为`TestController`;bean就是`TestController`的实例对象
 * 2. hasControllerAdvice: 判断类上是否有注解`ControllerAdvice`，
 * 在开发handlerMapping的初始化是也有类似的操作
 * 3. findAnnotatedBeans: 从容器中找出被`ControllerAdvice`标注的所有类，构建一个`ControllerAdviceBean`集合返回
 * @author wenzhaoyu
 * @date 2022/11/7
 */

@Getter
public class ControllerAdviceBean {
    private String beanName;
    private Class<?> beanType;
    private Object bean;

    public ControllerAdviceBean(String beanName, Object bean) {
        Assert.notNull(bean, "Bean must not be null");
        this.beanName = beanName;
        this.beanType = bean.getClass();
        this.bean = bean;
    }

    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context){
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Object.class);
        return beanMap.entrySet().stream()
                .filter(entry -> hasControllerAdvice(entry.getValue()))
                .map(entry -> new ControllerAdviceBean(entry.getKey(),entry.getValue()))
                .collect(Collectors.toList());
    }

    private static boolean hasControllerAdvice(Object bean) {
        Class<?> beanType = bean.getClass();
        return AnnotatedElementUtils.hasAnnotation(beanType, ControllerAdvice.class);
    }

}
