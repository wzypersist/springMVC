package com.wzy.mvc.config;

import com.wzy.mvc.DispatcherServlet;
import com.wzy.mvc.handler.adapter.HandlerAdapter;
import com.wzy.mvc.handler.adapter.RequestMappingHandlerAdapter;
import com.wzy.mvc.handler.interceptor.InterceptorRegistry;
import com.wzy.mvc.handler.mapping.HandlerMapping;
import com.wzy.mvc.handler.mapping.RequestMappingHandlerMapping;
import com.wzy.mvc.handler.view.viewresolver.ContentNegotiatingViewResolver;
import com.wzy.mvc.handler.view.viewresolver.InternalResourceViewResolver;
import com.wzy.mvc.handler.view.viewresolver.ViewResolver;
import com.wzy.mvc.interceptor.Test2HandlerInterceptor;
import com.wzy.mvc.interceptor.TestHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.Collections;

@Configuration
@ComponentScan(basePackages = "com.wzy.mvc")
public class AppConfig {

//    @Bean
//    public RequestMappingHandlerMapping handlerMapping() {
//        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
//
//        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
//        interceptorRegistry.addMapperInterceptor(interceptor)
//                .addExcludePatterns("/ex_test")
//                .addIncludePatterns("/in_test");
//
//        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
//        interceptorRegistry.addMapperInterceptor(interceptor2)
//                .addIncludePatterns("/in_test2", "/in_test3");
//
//        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
//        mapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
//        return mapping;
//    }

    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }
    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        return handlerAdapter;
    }
    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);
        return conversionService;
    }
    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        return negotiatingViewResolver;
    }
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
