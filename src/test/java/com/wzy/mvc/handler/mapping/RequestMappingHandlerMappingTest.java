package com.wzy.mvc.handler.mapping;

import com.wzy.mvc.BaseJunit4Test;
import com.wzy.mvc.controller.TestHandlerController;
import com.wzy.mvc.exception.NoHandlerFoundException;
import com.wzy.mvc.handler.HandlerExecutionChain;
import com.wzy.mvc.handler.HandlerMethod;
import com.wzy.mvc.handler.interceptor.MappedInterceptor;
import com.wzy.mvc.handler.mapping.MappingRegistry;
import com.wzy.mvc.handler.mapping.RequestMappingHandlerMapping;
import com.wzy.mvc.handler.mapping.RequestMappingInfo;
import com.wzy.mvc.interceptor.Test2HandlerInterceptor;

import com.wzy.mvc.interceptor.TestHandlerInterceptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestMappingHandlerMappingTest extends BaseJunit4Test {

    @Autowired
    private RequestMappingHandlerMapping  requestMappingHandlerMapping;

    @Test
    public void test(){
        MappingRegistry mappingRegistry = requestMappingHandlerMapping.getMappingRegistry();

        String path = "/index/test";
        String path1 = "/index/test2";
        String path4 = "/test4";


        System.out.println(mappingRegistry.getPathHandlerMethod().size());

        HandlerMethod handlerMethod = mappingRegistry.getHandlerMethodByPath(path);
        HandlerMethod handlerMethod2 = mappingRegistry.getHandlerMethodByPath(path1);
        HandlerMethod handlerMethod4 = mappingRegistry.getHandlerMethodByPath(path4);

        System.out.println(handlerMethod4);
        System.out.println(handlerMethod);
        System.out.println(handlerMethod2);


        RequestMappingInfo mapping = mappingRegistry.getRequestMappingInfo(path);
        RequestMappingInfo mapping2 = mappingRegistry.getRequestMappingInfo(path1);

        System.out.println(mapping);
        System.out.println(mapping2);
        System.out.println(mapping.getRequestMethod());
        System.out.println(mapping2.getRequestMethod());
    }



    @Test
    public void testGetHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        //测试TestHandlerInterceptor拦截器生效
        request.setRequestURI("/in_test");
        HandlerExecutionChain executionChain = requestMappingHandlerMapping.getHandler(request);

        HandlerMethod handlerMethod = executionChain.getHandler();
        Assert.assertTrue(handlerMethod.getBean() instanceof TestHandlerController);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof TestHandlerInterceptor);

        //测试TestHandlerInterceptor拦截器不生效
        request.setRequestURI("/ex_test");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 0);

        //测试找不到Handler,抛出异常
        request.setRequestURI("/in_test454545");
        try {
            requestMappingHandlerMapping.getHandler(request);
        } catch (NoHandlerFoundException e) {
            System.out.println("异常URL:" + e.getRequestURL());
        }

        //测试Test2HandlerInterceptor拦截器对in_test2、in_test3都生效
        request.setRequestURI("/in_test2");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof Test2HandlerInterceptor);

        request.setRequestURI("/in_test3");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof Test2HandlerInterceptor);
    }
}
