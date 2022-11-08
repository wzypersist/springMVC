package com.wzy.mvc.support;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javax.servlet.ServletContext;

public class GenericWebApplicationContext extends GenericApplicationContext implements ConfigurableApplicationContext {

    private ServletContext servletContext;

    public GenericWebApplicationContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public GenericWebApplicationContext(){

    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
