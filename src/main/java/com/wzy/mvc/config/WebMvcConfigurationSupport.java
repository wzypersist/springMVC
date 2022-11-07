package com.wzy.mvc.config;

import com.wzy.mvc.handler.adapter.HandlerAdapter;
import com.wzy.mvc.handler.adapter.RequestMappingHandlerAdapter;
import com.wzy.mvc.handler.argument.HandlerMethodArgumentResolver;
import com.wzy.mvc.handler.exception.ExceptionHandlerExceptionResolver;
import com.wzy.mvc.handler.exception.HandlerExceptionResolver;
import com.wzy.mvc.handler.interceptor.InterceptorRegistry;
import com.wzy.mvc.handler.interceptor.MappedInterceptor;
import com.wzy.mvc.handler.mapping.HandlerMapping;
import com.wzy.mvc.handler.mapping.RequestMappingHandlerMapping;
import com.wzy.mvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.handler.view.viewresolver.ContentNegotiatingViewResolver;
import com.wzy.mvc.handler.view.viewresolver.InternalResourceViewResolver;
import com.wzy.mvc.handler.view.viewresolver.ViewResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WebMvcConfigurationSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    private List<MappedInterceptor> interceptors;
    
    private List<HandlerMethodArgumentResolver> argumentResolvers;
    private List<HandlerMethodReturnValueHandler> returnValueHandlers;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 构建数据转换器`FormattingConversionService`,预留给用户可以自定义转换格式的接口供子类覆写
     * @return
     */
    @Bean
    public FormattingConversionService mvcConversionService(){
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        addFormatters(conversionService);
        return conversionService;

    }

    protected void addFormatters(DefaultFormattingConversionService conversionService) {
    }

    /**
     * 提供给用户添加自定义拦截器的扩展点，默认系统不添加任何拦截器
     * @param mvcConversionService
     * @return
     */
    protected List<MappedInterceptor> getInterceptors(FormattingConversionService mvcConversionService){
        if(this.interceptors == null){
            InterceptorRegistry registry = new InterceptorRegistry();
            addInterceptors(registry);
            this.interceptors = registry.getMappedInterceptors();
        }
        return this.interceptors;
    }

    protected void addInterceptors(InterceptorRegistry registry) {
    }
    
    @Bean
    public HandlerMapping handlerMapping(FormattingConversionService mvcConversionService){
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setInterceptors(getInterceptors(mvcConversionService));
        return handlerMapping;
    }
    
    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService){
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        handlerAdapter.setCustomArgumentResolvers(getArgumentResolvers());
        handlerAdapter.setCustomReturnValueHandlers(getReturnValueHandlers());
        return handlerAdapter;
        
    }

    public List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        if(this.argumentResolvers == null){
            this.argumentResolvers = new ArrayList<>();
            addArgumentResolvers(this.argumentResolvers);
        }
        
        return this.argumentResolvers;
    }

    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }

    public List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
        if(this.returnValueHandlers == null){
            this.returnValueHandlers = new ArrayList<>();
            addReturnValueHandlers(this.returnValueHandlers);
        }

        return this.returnValueHandlers;
    }

    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    }
    
    @Bean
    public HandlerExceptionResolver handlerExceptionResolver(FormattingConversionService mvcConversionService){
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setCustomArgumentResolvers(getArgumentResolvers());
        exceptionResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
        exceptionResolver.setConversionService(mvcConversionService);
        return exceptionResolver;
    }
    
    @Bean
    public ViewResolver viewResolver(){
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        List<ViewResolver> viewResolvers = new ArrayList<>();
        addViewResolvers(viewResolvers);
        if(CollectionUtils.isEmpty(viewResolvers)){
            resolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        }else{
            resolver.setViewResolvers(viewResolvers);
        }
        
        List<View> views = new ArrayList<>();
        addDefaultViews(views);
        if(!CollectionUtils.isEmpty(views)){
            resolver.setDefaultViews(views);
        }
        return resolver;
    }

    protected void addDefaultViews(List<View> views) {
    }

    protected void addViewResolvers(List<ViewResolver> viewResolvers) {
    }
    
    
}
