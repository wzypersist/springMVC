package com.wzy.mvc.handler;

import com.wzy.mvc.BaseJunit4Test;
import com.wzy.mvc.annotation.RequestMapping;
import com.wzy.mvc.handler.mapping.MappingRegistry;
import com.wzy.mvc.handler.mapping.RequestMappingHandlerMapping;
import com.wzy.mvc.handler.mapping.RequestMappingInfo;
import com.wzy.mvc.http.RequestMethod;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestMappingHandlerMappingTest extends BaseJunit4Test {

    @Autowired
    private RequestMappingHandlerMapping  requestMappingHandlerMapping;

    @Test
    public void test(){
        MappingRegistry mappingRegistry = requestMappingHandlerMapping.getMappingRegistry();

        String path = "/index/test";
        String path1 = "/index/test2";
        String path4 = "/test4";

        Assert.assertEquals(mappingRegistry.getPathHandlerMethod().size(), 2);

        HandlerMethod handlerMethod = mappingRegistry.getHandlerMethodByPath(path);
        HandlerMethod handlerMethod2 = mappingRegistry.getHandlerMethodByPath(path1);
        HandlerMethod handlerMethod4 = mappingRegistry.getHandlerMethodByPath(path4);

        Assert.assertNull(handlerMethod4);
        Assert.assertNotNull(handlerMethod);
        Assert.assertNotNull(handlerMethod2);


        RequestMappingInfo mapping = mappingRegistry.getRequestMappingInfo(path);
        RequestMappingInfo mapping2 = mappingRegistry.getRequestMappingInfo(path1);

        Assert.assertNotNull(mapping);
        Assert.assertNotNull(mapping2);
        Assert.assertEquals(mapping.getRequestMethod(), RequestMethod.GET);
        Assert.assertEquals(mapping2.getRequestMethod(), RequestMethod.POST);
    }

}
