package com.wzy.mvc.handler.returnvalue;

import com.wzy.mvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {

    private List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();

    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (HandlerMethodReturnValueHandler returnValueHandler : handlerMethodReturnValueHandlers) {
            if(returnValueHandler.supportsReturnType(returnType)){
                returnValueHandler.handleReturnValue(returnValue,returnType,mavContainer,request,response);
                return;
            }

        }
        throw new IllegalArgumentException("Unsupported parameter type ["+
                returnType.getParameterType().getName() +"]. supportsParameter should be called first.");
    }


    public void addReturnValueHandler(HandlerMethodReturnValueHandler... handlers){
        this.handlerMethodReturnValueHandlers.addAll(Arrays.asList(handlers));
    }

    public void addReturnValueHandler(Collection<HandlerMethodReturnValueHandler> handlers){
        this.handlerMethodReturnValueHandlers.addAll(handlers);
    }


    public void clear(){
        this.handlerMethodReturnValueHandlers.clear();
    }

}
