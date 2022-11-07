package com.wzy.mvc.handler.exception;

import com.wzy.mvc.annotation.ExceptionHandler;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 1. 首先定义了`EXCEPTION_HANDLER_METHODS`静态变量，判断方法是否有注解`ExceptionHandler`
 * 2. detectExceptionMappings: 解析出方法上`ExceptionHandler`配置的所有异常
 * 3. 构造方法中传入了Bean的类型，
 * 使用`MethodIntrospector.selectMethods`过滤出所有被`ExceptionHandler`标注的类
 *（在HanderMapping的初始化也使用过同样的方法），保存异常类型和方法的对应关系
 * 4. resolveMethod: 通过异常类型找出对应的方法
 *
 * @author wenzhaoyu
 * @date 2022/11/7
 */

public class ExceptionHandlerMethodResolver {

    public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = method ->
            AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class);

    private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentReferenceHashMap<>();

    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
            for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
                this.mappedMethods.put(exceptionType, method);
            }
        }
    }

    private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {
        ExceptionHandler ann = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
        Assert.state(ann != null, "No ExceptionHandler annotation");
        return Arrays.asList(ann.value());
    }

    public Map<Class<? extends Throwable>, Method> getMappedMethods() {
        return mappedMethods;
    }

    public boolean hasExceptionMappings(){
        return !this.mappedMethods.isEmpty();
    }

    public Method resolveMethod(Exception exception){
        Method method = resolveMethodByExceptionType(exception.getClass());
        if(method == null){
            Throwable cause = exception.getCause();
            if(cause != null){
                method = resolveMethodByExceptionType(cause.getClass());
            }
        }
        return method;
    }

    private Method resolveMethodByExceptionType(Class<? extends Throwable> aClass) {
        return mappedMethods.get(aClass);
    }

}
