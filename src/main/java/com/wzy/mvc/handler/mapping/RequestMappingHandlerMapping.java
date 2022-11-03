package com.wzy.mvc.handler.mapping;

import com.wzy.mvc.annotation.RequestMapping;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * 1.在初始化的过程中，我们需要获取到容器中所有的Bean对象，所以继承于`ApplicationObjectSupport`
 * 2.在创建完对象后初始化`HandlerMethod`，所以实现了接口`InitializingBean`,在对象创建完成后，spring容器会调用这个方法
 */
public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements InitializingBean {

    private MappingRegistry mappingRegistry = new MappingRegistry();

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialHandlerMethods();
    }

    private void initialHandlerMethods() {
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(), Object.class);
        beanMap.entrySet().stream()
                .filter(entry -> this.isHandler(entry.getValue()))
                .forEach(entry -> this.detectHandlerMethods(entry.getKey(),entry.getValue()));

    }

    /**
     * 解析出handler中 所有被RequestMapping注解的方法
     * @param beanName
     * @param handler
     */
    private void detectHandlerMethods(String beanName, Object handler) {

        Class<?> type = handler.getClass();
        Map<Method, RequestMappingInfo> methodsOfMap = MethodIntrospector.selectMethods(type,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> getMappingForMethod(method, type));
        methodsOfMap.forEach(((method, requestMappingInfo) -> this.mappingRegistry.register(requestMappingInfo,handler,method)));
    }

    private RequestMappingInfo getMappingForMethod(Method method, Class<?> type) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if(requestMapping == null){
            return null;
        }
        String prefix = getPathPrefix(type);
        return new RequestMappingInfo(prefix,requestMapping);
    }

    private String getPathPrefix(Class<?> type) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(type, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return "";
        }
        return requestMapping.path();


    }

    /**
     * 判断类上是否有controller注解
     * @param handler
     * @return
     */
    private boolean isHandler(Object handler) {
        Class<?> type = handler.getClass();
        return AnnotatedElementUtils.hasAnnotation(type, Controller.class);
    }
}
