package com.wzy.mvc.config;

import com.wzy.mvc.handler.argument.HandlerMethodArgumentResolver;
import com.wzy.mvc.handler.interceptor.InterceptorRegistry;
import com.wzy.mvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.handler.view.viewresolver.ViewResolver;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

import java.util.List;

/**
 * 提供给用户扩展的接口，方便用户添加所有的扩展点方法
 */
public interface WebMvcConfigurer {

    //参数解析器的扩展点
    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){}
    
    //返回值解析器
    default void addReturnValueResolvers(List<HandlerMethodReturnValueHandler> returnValueHandlers){}
    
    //拦截器暴露的扩展点
    default void addInterceptors(InterceptorRegistry registry){}
    
    //数据格式转换扩展点
    default void addFormatters(FormatterRegistry registry){}
    
    // 视图扩展点
    default void addViews(List<View> views){}
    
    //视图解析器扩展点
    default void addViewResolvers(List<ViewResolver> viewResolvers){}
    
}
