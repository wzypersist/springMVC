package com.wzy.mvc.handler.adapter;

import com.wzy.mvc.ModelAndView;
import com.wzy.mvc.handler.HandlerMethod;
import com.wzy.mvc.handler.InvocableHandlerMethod;
import com.wzy.mvc.handler.ModelAndViewContainer;
import com.wzy.mvc.handler.argument.*;
import com.wzy.mvc.handler.returnvalue.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RequestMappingHandlerAdapter implements HandlerAdapter, InitializingBean {

    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;

    /**
     * 处理方法
     * @param request
     * @param response
     * @param handlerMethod
     * @return
     * @throws Exception
     */
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                               HandlerMethod handlerMethod) throws Exception {
        InvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        invocableMethod.invokeAndHandle(request,response,mavContainer);
        return getModelAndView(mavContainer);
    }

    /**
     * 得到InvocableHandlerMethod对象来调用其中方法
     * @param handlerMethod
     * @return
     */
    private InvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new InvocableHandlerMethod(handlerMethod,
                this.argumentResolverComposite,
                this.returnValueHandlerComposite,
                this.conversionService);
    }

    /**
     * 返回ModelAndView对象
     * @param mavContainer
     * @return
     */
    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer) {
        if(mavContainer.isRequestHandled()){
            return null;
        }
        ModelAndView mav = new ModelAndView();
        mav.setModel(mavContainer.getModel());
        mav.setStatus(mavContainer.getStatus());
        mav.setView(mavContainer.getView());
        return mav;
    }

    /**
     * 把系统默认支持的参数解析器和返回值处理器以及用户自定义的一起添加到系统中。
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService, "conversionService can not null");
        if(Objects.isNull(argumentResolverComposite)){
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            argumentResolverComposite.addResolver(resolvers);
        }
        if(Objects.isNull(returnValueHandlerComposite)){
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlerComposite.addReturnValueHandler(handlers);
        }
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
        handlers.add(new ResponseBodyMethodReturnValueHandler());
        handlers.add(new MapMethodReturnValueHandler());
        handlers.add(new ModelMethodReturnValueHandler());
        handlers.add(new ViewMethodReturnValueHandler());
        handlers.add(new ViewNameMethodReturnValueHandler());
        if (!CollectionUtils.isEmpty(getCustomReturnValueHandlers())) {
            handlers.addAll(getCustomReturnValueHandlers());
        }
        return handlers;

    }

    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        resolvers.add(new ModelMethodArgumentResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new RequestBodyMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());

        if(!CollectionUtils.isEmpty(getCustomArgumentResolvers())){
            resolvers.addAll(getCustomArgumentResolvers());
        }
        return resolvers;
    }

    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return customArgumentResolvers;
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
    }

    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return customReturnValueHandlers;
    }

    public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> customReturnValueHandlers) {
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
