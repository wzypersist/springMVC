package com.wzy.mvc.view.resolver;


import com.wzy.mvc.handler.view.InternalResourceView;
import com.wzy.mvc.handler.view.RedirectView;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.handler.view.viewresolver.ContentNegotiatingViewResolver;
import com.wzy.mvc.handler.view.viewresolver.InternalResourceViewResolver;
import com.wzy.mvc.utils.RequestContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;

public class ContentNegotiatingViewResolverTest {

    @Test
    public void resolveViewName() throws Exception {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept", "text/html");
        RequestContextHolder.setRequest(request);

        View redirectView = negotiatingViewResolver.resolveViewName("redirect:/silently9527.cn");
        Assert.assertTrue(redirectView instanceof RedirectView);

        View forwardView = negotiatingViewResolver.resolveViewName("forward:/silently9527.cn");
        Assert.assertTrue(forwardView instanceof InternalResourceView);

        View view = negotiatingViewResolver.resolveViewName("/silently9527.cn");
        Assert.assertTrue(view instanceof InternalResourceView);

    }
}
