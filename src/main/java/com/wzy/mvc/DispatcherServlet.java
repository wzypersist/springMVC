package com.wzy.mvc;

import com.wzy.mvc.handler.HandlerExecutionChain;
import com.wzy.mvc.handler.adapter.HandlerAdapter;
import com.wzy.mvc.handler.exception.HandlerExceptionResolver;
import com.wzy.mvc.handler.mapping.HandlerMapping;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.handler.view.viewresolver.ViewResolver;
import com.wzy.mvc.utils.RequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;


public class DispatcherServlet extends HttpServlet implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;
    private Collection<HandlerExceptionResolver> handlerExceptionResolvers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 初始化部分，当Servlet在第一次初始化的时候会调用 init方法，
     * 在该方法里对诸如 handlerMapping，ViewResolver 等进行初始化，
     */
    @Override
    public void init() {
        this.handlerMapping = this.applicationContext.getBean(HandlerMapping.class);
        this.handlerAdapter = this.applicationContext.getBean(HandlerAdapter.class);
        this.viewResolver = this.applicationContext.getBean(ViewResolver.class);
        this.handlerExceptionResolvers = this.applicationContext.getBeansOfType(HandlerExceptionResolver.class).values();
    }

    /**
     * 对HTTP请求进行响应，作为一个Servlet，当请求到达时Web容器会调用其service方法;
     * 通过`RequestContextHolder`在线程变量中设置request，然后调用`doDispatch`完成请求
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("DispatcherServlet.service => uri:{}",req.getRequestURI());
        RequestContextHolder.setRequest(req);
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            logger.error("Handler the request fail",e);
        }finally {
            RequestContextHolder.resetRequest();
        }
    }

    /**
     * 1. 首先通过handlerMapping获取到处理本次请求的`HandlerExecutionChain`
     * 2. 执行拦截器的前置方法
     * 3. 通过`handlerAdapter`执行handler返回ModelAndView
     * 4. 执行拦截器的后置方法
     * 5. 处理返回的结果`processDispatchResult`
     * 6. 在处理完成请求后调用`executionChain.triggerAfterCompletion(request, response, dispatchException);`，
     *      完成拦截器的`afterCompletion`方法调用
     * @param req
     * @param resp
     * @throws Exception
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        Exception dispatchException = null;
        HandlerExecutionChain executionChain = null;
        try {
            ModelAndView mv = null;
            try {
                executionChain = this.handlerMapping.getHandler(req);
                if(!executionChain.applyPreHandle(req,resp)){
                    return;
                }
                mv = handlerAdapter.handle(req,resp,executionChain.getHandler());
                executionChain.applyPostHandle(req,resp,mv);
            }catch (Exception e){
                dispatchException = e;
            }
            processDispatchResult(req,resp,mv,dispatchException);
        }catch (Exception ex){
            dispatchException = ex;
            throw ex;
        }finally {
            if(Objects.nonNull(executionChain)){
                executionChain.triggerAfterCompletion(req,resp,dispatchException);
            }
        }

    }

    /**
     * 1.如果是正常的返回ModelAndView，那么就执行render方法，
     * 2.如果在执行的过程中抛出了任何异常，那么就会执行`processHandlerException`，方便做全局异常处理
     * @param req
     * @param resp
     * @param mv
     * @param ex
     */
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp,
                                       ModelAndView mv, Exception ex) throws Exception{
        if(Objects.nonNull(ex)){
            mv = processHandlerException(req,resp,ex);
        }
        if(Objects.nonNull(mv)){
            render(mv,req,resp);
        }
        logger.info("No view rendering, null ModelAndView returned.");
    }

    private ModelAndView processHandlerException(HttpServletRequest req, HttpServletResponse resp, Exception ex) throws Exception{
        if(CollectionUtils.isEmpty(this.handlerExceptionResolvers)){
            throw ex;
        }
        for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
            ModelAndView exModelAndView = resolver.resolveException(req,resp,ex);
            if(exModelAndView != null){
                return exModelAndView;
            }
        }
        throw ex;
    }

    /**
     * 首先通过ViewResolver解析出视图，然后在调用View的render方法实施渲染逻辑
     * @param mv
     * @param req
     * @param resp
     */
    private void render(ModelAndView mv, HttpServletRequest req,
                        HttpServletResponse resp) throws Exception{
        View view;
        String viewName = mv.getViewName();
        if(!StringUtils.isEmpty(viewName)){
            view = this.viewResolver.resolveViewName(viewName);
        }else{
            view = (View)mv.getView();
        }
        if(mv.getStatus() != null){
            resp.setStatus(mv.getStatus().getCode());
        }
        view.render(mv.getModel().asMap(),req,resp);

    }

}
