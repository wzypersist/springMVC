package com.wzy.mvc.handler.returnvalue;

import com.wzy.mvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter parameter) {

        return Model.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(returnValue == null){
            return;
        }else if(returnValue instanceof Model){
            mavContainer.getModel().addAllAttributes(((Model) returnValue).asMap());
        }else{
// should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }

    }
}
