package com.wzy.mvc.adapter;


import com.alibaba.fastjson.JSON;
import com.wzy.mvc.ModelAndView;
import com.wzy.mvc.controller.TestInvocableHandlerMethodController;
import com.wzy.mvc.handler.HandlerMethod;
import com.wzy.mvc.handler.adapter.RequestMappingHandlerAdapter;
import org.junit.Test;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.lang.reflect.Method;

public class RequestMappingHandlerAdapterTest {

    @Test
    public void handle() throws Exception {
        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testViewName", Model.class);
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        handlerAdapter.afterPropertiesSet();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerMethod);

        System.out.println("modelAndView:");
        System.out.println(JSON.toJSONString(modelAndView));
    }


}
