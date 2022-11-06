package com.wzy.mvc.handler.returnvalue;

import com.wzy.mvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodReturnValueHandler {

    boolean supportsReturnType(MethodParameter parameter);

    void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                           ModelAndViewContainer mavContainer,
                           HttpServletRequest request, HttpServletResponse response) throws Exception;



}
