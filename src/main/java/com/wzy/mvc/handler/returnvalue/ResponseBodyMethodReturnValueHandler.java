package com.wzy.mvc.handler.returnvalue;

import com.alibaba.fastjson.JSON;
import com.wzy.mvc.annotation.ResponseBody;
import com.wzy.mvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(ResponseBody.class) ||
                (AnnotatedElementUtils.hasAnnotation(parameter.getContainingClass(),ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        mavContainer.setRequestHandled(true);
        System.out.println(returnValue);
        System.out.println(returnType);
        System.out.println(mavContainer);
        outPutMessage(response, JSON.toJSONString(returnValue));

    }

    private void outPutMessage(HttpServletResponse response, String message) {
        try (PrintWriter writer = response.getWriter()) {
            writer.write(message);
        }catch (IOException ex){

        }
    }
}
