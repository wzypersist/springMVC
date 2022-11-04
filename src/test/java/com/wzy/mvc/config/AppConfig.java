package com.wzy.mvc.config;

import com.wzy.mvc.handler.interceptor.InterceptorRegistry;
import com.wzy.mvc.handler.mapping.RequestMappingHandlerMapping;
import com.wzy.mvc.intercepter.Test2HandlerInterceptor;
import com.wzy.mvc.intercepter.TestHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.wzy.mvc")
public class AppConfig {

    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
        interceptorRegistry.addMapperInterceptor(interceptor)
                .addExcludePatterns("/ex_test")
                .addIncludePatterns("/in_test");

        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
        interceptorRegistry.addMapperInterceptor(interceptor2)
                .addIncludePatterns("/in_test2", "/in_test3");

        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        mapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
        return mapping;
    }
}
