package com.wzy.mvc.handler.mapping;

import com.wzy.mvc.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有映射的注册中心
 * `MappingRegistry`是`RequestMappingInfo`、`HandlerMethod`的注册中心，
 * 当解析完一个控制器的method后就会向`MappingRegistry`中注册一个；
 * 最后当接收到用户请求后，根据请求的url在`MappingRegistry`找到对应的`HandlerMethod`；
 */
public class MappingRegistry {

    private Map<String,RequestMappingInfo> pathMappingInfo = new ConcurrentHashMap<>();
    private Map<String, HandlerMethod> pathHandlerMethod = new ConcurrentHashMap<>();

    /**
     * 注册url和Mapping/HandlerMethod的对应关系
     * @param requestMappingInfo
     * @param handler
     * @param method
     */
    public void register(RequestMappingInfo requestMappingInfo, Object handler, Method method){
        pathMappingInfo.put(requestMappingInfo.getPath(),requestMappingInfo);
        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        pathHandlerMethod.put(requestMappingInfo.getPath(),handlerMethod);
    }

    public Map<String, HandlerMethod> getPathHandlerMethod() {
        return pathHandlerMethod;
    }

    public Map<String, RequestMappingInfo> getPathMappingInfo() {
        return pathMappingInfo;
    }

    public RequestMappingInfo getRequestMappingInfo(String path){
        return pathMappingInfo.get(path);
    }

    public HandlerMethod getHandlerMethodByPath(String path){
        return pathHandlerMethod.get(path);
    }
}
