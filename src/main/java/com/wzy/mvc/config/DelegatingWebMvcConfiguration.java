package com.wzy.mvc.config;

import com.wzy.mvc.handler.argument.HandlerMethodArgumentResolver;
import com.wzy.mvc.handler.interceptor.InterceptorRegistry;
import com.wzy.mvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.handler.view.viewresolver.ViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 为了把`WebMvcConfigurer`与`WebMvcConfigurationSupport`联系起来屏蔽掉实现的细节，
 * 只暴露扩展点给用户，我们需要实现`DelegatingWebMvcConfiguration`， 
 * 它从容器中拿出所有的WebMvcConfigurer,添加到WebMvcConfigurerComposite里面，
 * 在DelegatingWebMvcConfiguration中调用`WebMvcConfigurerComposite`完成扩展点的载入
 */
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport{
    private WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
    
    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers){
        if(!CollectionUtils.isEmpty(configurers)){
            this.configurers.addWebMvcConfigurers(configurers);
        }
    }

    @Override
    protected void addFormatters(DefaultFormattingConversionService conversionService) {
        configurers.addFormatters(conversionService);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        configurers.addInterceptors(registry);
    }

    @Override
    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        configurers.addReturnValueResolvers(returnValueHandlers);
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        configurers.addArgumentResolvers(argumentResolvers);
    }

    @Override
    protected void addDefaultViews(List<View> views) {
        configurers.addViews(views);
    }

    @Override
    protected void addViewResolvers(List<ViewResolver> viewResolvers) {
        configurers.addViewResolvers(viewResolvers);
    }
    
}
