package com.wzy.mvc.intercepter;


import com.wzy.mvc.ModelAndView;
import com.wzy.mvc.handler.interceptor.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test2HandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        System.out.println("Test2HandlerInterceptor => preHandle");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)  {
        System.out.println("Test2HandlerInterceptor => postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  {
        System.out.println("Test2HandlerInterceptor => afterCompletion");
    }
}
