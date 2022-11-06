package com.wzy.mvc.handler.argument;

import com.wzy.mvc.annotation.RequestParam;
import com.wzy.mvc.exception.MissingServletRequestParameterException;
import com.wzy.mvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
                                  HttpServletResponse response, ModelAndViewContainer container,
                                  ConversionService conversionService) throws Exception {
        RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
        if(Objects.isNull(requestParam)){
            return null;
        }
        String value = request.getParameter(requestParam.name());
        if(StringUtils.isEmpty(value)){
            return null;
        }
        if(!StringUtils.isEmpty(value)){
            return conversionService.convert(value,parameter.getParameterType());
        }
        if(requestParam.required()){
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }

        return null;
    }
}
