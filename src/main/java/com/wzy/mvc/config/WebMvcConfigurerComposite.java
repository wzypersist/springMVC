package com.wzy.mvc.config;

import com.wzy.mvc.handler.argument.HandlerMethodArgumentResolver;
import com.wzy.mvc.handler.interceptor.InterceptorRegistry;
import com.wzy.mvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.handler.view.viewresolver.ViewResolver;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WebMvcConfigurerComposite implements WebMvcConfigurer {
    
    private List<WebMvcConfigurer> delegates= new ArrayList<>();

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        delegates.forEach(configurer -> configurer.addArgumentResolvers(argumentResolvers));
    }

    @Override
    public void addReturnValueResolvers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        delegates.forEach(configurer -> configurer.addReturnValueResolvers(returnValueHandlers));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        delegates.forEach(configurer -> configurer.addInterceptors(registry));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        delegates.forEach(configurer -> configurer.addFormatters(registry));
    }

    @Override
    public void addViews(List<View> views) {
        delegates.forEach(configurer -> configurer.addViews(views));
    }

    @Override
    public void addViewResolvers(List<ViewResolver> viewResolvers) {
        delegates.forEach(configurer -> configurer.addViewResolvers(viewResolvers));
    }
    
    public WebMvcConfigurerComposite addWebMvcConfigurers(WebMvcConfigurer... webMvcConfigurers){
        Collections.addAll(this.delegates,webMvcConfigurers);
        return this;
    }
    
    public WebMvcConfigurerComposite addWebMvcConfigurers(List<WebMvcConfigurer> webMvcConfigurers){
        Collections.addAll(webMvcConfigurers);
        return this;
    }
    
    
}
