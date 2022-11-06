package com.wzy.mvc.handler.adapter;

import com.wzy.mvc.ModelAndView;
import com.wzy.mvc.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                        HandlerMethod handler) throws Exception;

}
