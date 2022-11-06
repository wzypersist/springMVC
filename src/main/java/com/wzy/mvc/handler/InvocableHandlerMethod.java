package com.wzy.mvc.handler;

import com.wzy.mvc.handler.argument.HandlerMethodArgumentResolver;
import com.wzy.mvc.handler.argument.HandlerMethodArgumentResolverComposite;
import com.wzy.mvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.wzy.mvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvocableHandlerMethod extends HandlerMethod {

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private HandlerMethodArgumentResolver argumentResolver;
    private HandlerMethodReturnValueHandler returnValueHandler;
    private ConversionService conversionService;

    public InvocableHandlerMethod(HandlerMethod handlerMethod,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(handlerMethod.getBean(), handlerMethod.getMethod());
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public InvocableHandlerMethod(Object bean, Method method,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(bean, method);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public void invokeAndHandle(HttpServletRequest request,
                              HttpServletResponse response,
                              ModelAndViewContainer mavContainer,
                              Object... proviedArgs) throws Exception{
        List<Object> args = this.getMethodArgumentValues(request,response,mavContainer,proviedArgs);

        //反射调用hanlerMethod中的某个方法
        Object result = doInvoke(args);
        System.out.println(result);
        if(Objects.isNull(result)){
            if(response.isCommitted()){
                mavContainer.setRequestHandled(true);
            }else {
                throw new IllegalStateException("Controller handler return value is null");
            }
        }
    }

    private Object doInvoke(List<Object> args) throws InvocationTargetException, IllegalAccessException {
        return this.getMethod().invoke(this.getBean(),args.toArray());
    }

    private List<Object> getMethodArgumentValues(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 ModelAndViewContainer mavContainer,
                                                 Object[] proviedArgs) throws Exception{
        Assert.notNull(argumentResolver, "HandlerMethodArgumentResolver can not null");
        // 获取到方法中的参数
        List<MethodParameter> parameters = this.getParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        for (MethodParameter parameter : parameters) {
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            Object arg = findProvidedArgument(parameter,proviedArgs);
            if(!Objects.isNull(arg)){
                args.add(arg);
                continue;
            }
            args.add(argumentResolver.resolveArgument(parameter,request,response,mavContainer,conversionService));
        }

        return args;
    }



    protected static Object findProvidedArgument(MethodParameter parameter, Object[] proviedArgs) {
        if(!ObjectUtils.isEmpty(proviedArgs)){
            for (Object proviedArg : proviedArgs) {
                if(parameter.getParameterType().isInstance(proviedArg)){
                    return proviedArg;
                }
            }
        }
        return null;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
