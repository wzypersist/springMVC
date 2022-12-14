package com.wzy.mvc.handler.interceptor;

import com.wzy.mvc.ModelAndView;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    default boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler){
        return true;
    }

    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @Nullable ModelAndView modelAndView){

    }

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response,Object handler,
                            @Nullable Exception ex){

    }

}
