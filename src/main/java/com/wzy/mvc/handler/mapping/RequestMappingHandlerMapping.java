package com.wzy.mvc.handler.mapping;


import com.wzy.mvc.annotation.RequestMapping;
import com.wzy.mvc.exception.NoHandlerFoundException;
import com.wzy.mvc.handler.HandlerExecutionChain;
import com.wzy.mvc.handler.HandlerMethod;
import com.wzy.mvc.handler.interceptor.HandlerInterceptor;
import com.wzy.mvc.handler.interceptor.MappedInterceptor;
import com.wzy.mvc.handler.mapping.HandlerMapping;
import com.wzy.mvc.handler.mapping.MappingRegistry;
import com.wzy.mvc.handler.mapping.RequestMappingInfo;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, InitializingBean {

    private MappingRegistry mappingRegistry = new MappingRegistry();
    private List<MappedInterceptor> interceptors = new ArrayList<>();

    public void setInterceptors(List<MappedInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    public void afterPropertiesSet() throws Exception {
        initialHandlerMethods();
    }

    private void initialHandlerMethods() {
        Map<String, Object> beansOfMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(), Object.class);
        beansOfMap.entrySet().stream()
                .filter(entry -> this.isHandler(entry.getValue()))
                .forEach(entry -> this.detectHandlerMethods(entry.getKey(), entry.getValue()));
    }

    /**
     * 类上有标记Controller的注解就是我们需要找的handler
     *
     * @param handler
     * @return
     */
    private boolean isHandler(Object handler) {
        Class<?> beanType = handler.getClass();
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class));
    }

    /**
     * 解析出handler中 所有被RequestMapping注解的方法
     *
     * @param beanName
     * @param handler
     */
    private void detectHandlerMethods(String beanName, Object handler) {
        Class<?> beanType = handler.getClass();
        Map<Method, RequestMappingInfo> methodsOfMap = MethodIntrospector.selectMethods(beanType,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> getMappingForMethod(method, beanType));

        methodsOfMap.forEach((method, requestMappingInfo) -> this.mappingRegistry.register(requestMappingInfo, handler, method));
    }

    /**
     * 查找method上面是否有RequestMapping，有 => 构建RequestMappingInfo
     *
     * @param method
     * @param beanType
     * @return
     */
    private RequestMappingInfo getMappingForMethod(Method method, Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return null;
        }
        String prefix = getPathPrefix(beanType);
        return new RequestMappingInfo(prefix, requestMapping);
    }

    private String getPathPrefix(Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(beanType, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return "";
        }
        return requestMapping.path();
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String lookupPath = request.getRequestURI();
        HandlerMethod handler = mappingRegistry.getHandlerMethodByPath(lookupPath);
        if (Objects.isNull(handler)) {
            throw new NoHandlerFoundException(request);
        }
        return createHandlerExecutionChain(lookupPath, handler);
    }

    private HandlerExecutionChain createHandlerExecutionChain(String lookupPath, HandlerMethod handler) {
        List<HandlerInterceptor> interceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.matches(lookupPath))
                .collect(toList());
        return new HandlerExecutionChain(handler, interceptors);
    }

}
