package com.wzy.mvc.intercepter;


import com.wzy.mvc.handler.interceptor.InterceptorRegistry;
import com.wzy.mvc.handler.interceptor.MappedInterceptor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class HandlerInterceptorTest {

    private InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

    @Test
    public void test() throws Exception {
        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();

        interceptorRegistry.addMapperInterceptor(interceptor)
                .addExcludePatterns("/ex_test")
                .addIncludePatterns("/in_test");

        List<MappedInterceptor> interceptors = interceptorRegistry.getMappedInterceptors();

        Assert.assertEquals(interceptors.size(), 1);

        MappedInterceptor mappedInterceptor = interceptors.get(0);

        Assert.assertTrue(mappedInterceptor.matches("/in_test"));
        Assert.assertFalse(mappedInterceptor.matches("/ex_test"));

        mappedInterceptor.preHandle(null, null, null);
        mappedInterceptor.postHandle(null, null, null, null);
        mappedInterceptor.afterCompletion(null, null, null, null);
    }

}
