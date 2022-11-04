package com.wzy.mvc.handler.mapping;

import com.wzy.mvc.annotation.RequestMapping;
import com.wzy.mvc.exception.NoHandlerFoundException;
import com.wzy.mvc.handler.HandlerExecutionChain;
import com.wzy.mvc.handler.HandlerMethod;
import com.wzy.mvc.handler.interceptor.HandlerInterceptor;
import com.wzy.mvc.handler.interceptor.MappedInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 1.在初始化的过程中，我们需要获取到容器中所有的Bean对象，所以继承于`ApplicationObjectSupport`
 * 2.在创建完对象后初始化`HandlerMethod`，所以实现了接口`InitializingBean`,在对象创建完成后，spring容器会调用这个方法
 */
@Component
public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements InitializingBean {

    private MappingRegistry mappingRegistry = new MappingRegistry();
    private List<MappedInterceptor> interceptors = new ArrayList<>();

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialHandlerMethods();
    }

    private void initialHandlerMethods() {
        // 拿到spring容器的所有bean并解析成map
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(obtainApplicationContext(), Object.class);
        beanMap.entrySet().stream()
                // 根据bean上是否存在Controller注解过滤
                .filter(entry -> this.isHandler(entry.getValue()))
                // 解析带有Controller注解类的每个方法，
                // 若加了RequestMapping注解，则会将该方法上的RequestMapping注解的路径值和定义的方法保存为RequestMappingInfo
                // 最终注册到mappingRegistry
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
        String prefix = getMethodPathPrefix(type,method);
        return new RequestMappingInfo(prefix,requestMapping);
    }

    private String getPathPrefix(Class<?> type) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(type, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return "";
        }
        return requestMapping.path();

    }

    private String getMethodPathPrefix(Class<?>type, Method method) {
        RequestMapping requestMappingClass = AnnotatedElementUtils.findMergedAnnotation(type, RequestMapping.class);
        RequestMapping requestMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (Objects.isNull(requestMappingClass) || Objects.isNull(requestMappingMethod)) {
            return "";
        }
        return requestMappingClass.path()+requestMappingMethod.path();


    }

    /**
     * 判断类上是否有controller注解
     * @param handler
     * @return
     */
    private boolean isHandler(Object handler) {
        Class<?> type = handler.getClass();
        return AnnotatedElementUtils.hasAnnotation(type, Controller.class) ;
    }

    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception{
        String path = request.getRequestURI();
        HandlerMethod handler = mappingRegistry.getHandlerMethodByPath(path);
        if (Objects.isNull(handler)) {
            throw new NoHandlerFoundException(request);
        }
        return createHandlerExecutionChain(path, handler);
    }

    private HandlerExecutionChain createHandlerExecutionChain(String path, HandlerMethod handler) {
        List<HandlerInterceptor> interceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.matches(path))
                .collect(Collectors.toList());
        return new HandlerExecutionChain(handler,interceptors);
    }

    public void setInterceptors(List<MappedInterceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
