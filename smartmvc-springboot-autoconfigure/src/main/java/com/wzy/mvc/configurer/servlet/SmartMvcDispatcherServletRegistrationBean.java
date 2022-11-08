package com.wzy.mvc.configurer.servlet;

import com.wzy.mvc.DispatcherServlet;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.util.Assert;

/**
 * SpringBoot已经提供了很方便的方式来注册Servlet，只需要继承`ServletRegistrationBean`，
 * 查看源码我们会发现这个类的父类是`ServletContextInitializer`，
 * 在上面我们已经提到了在WebServer创建完成之后会调用`ServletContextInitializer`的`onStartup`
 * 方法。
 * @author wenzhaoyu
 * @date 2022/11/8
 */

public class SmartMvcDispatcherServletRegistrationBean extends ServletRegistrationBean<DispatcherServlet>
            implements DispatcherServletPath {

    private String path;


    public SmartMvcDispatcherServletRegistrationBean(DispatcherServlet servlet,String path) {
        super(servlet);
        Assert.notNull(path,"Path must be null");
        this.path = path;
        super.addUrlMappings(getServletUrlMapping());
    }

    @Override
    public String getPath() {
        return this.path;
    }

}
