package com.wzy.mvc.configurer.context;

import com.wzy.mvc.support.GenericWebApplicationContext;
import com.wzy.mvc.support.WebApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContextException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Map;


public class ServletWebServerApplicationContext extends GenericWebApplicationContext implements WebServerApplicationContext {

    //定义WebServer，这是SpringBoot中的类，有多个实现：Tomcat，jetty等等
    private WebServer webServer;

    public ServletWebServerApplicationContext() {
    }

    @Override
    public WebServer getWebServer() {
        return this.webServer;
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        try {
            super.refresh();
        }catch (RuntimeException ex){
            WebServer webServer = this.webServer;
            if(webServer != null){
                webServer.stop();
            }
            throw ex;
        }
    }

    @Override
    protected void onRefresh() throws BeansException {
        super.onRefresh();
        try {
            this.webServer = createrWebServer();
            this.webServer.start();
        }catch (Throwable e){
            throw new ApplicationContextException("Unable to start web server",e);
        }
    }

    private WebServer createrWebServer() {
        ServletWebServerFactory factory = getBeanFactory().getBean(ServletWebServerFactory.class);
        return factory.getWebServer(this::selfInitialize);
    }

    //ServletContextInitializer 在Web容器启动完成后会回调此方法，比如：向ServletConext中添加DispatchServlet
    private void selfInitialize(ServletContext servletContext) throws ServletException {
        prepareWebApplicationContext(servletContext);
        Map<String, ServletContextInitializer> beanMaps = getBeanFactory().getBeansOfType(ServletContextInitializer.class);
        for (ServletContextInitializer bean : beanMaps.values()) {
            bean.onStartup(servletContext);

        }
    }

    private void prepareWebApplicationContext(ServletContext servletContext) {
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,this);
        setServletContext(servletContext);
    }
}
