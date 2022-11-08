package com.wzy.mvc.configurer;

import com.wzy.mvc.annotation.EnableWebMvc;
import com.wzy.mvc.config.WebMvcConfigurationSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@Import({SmartMvcDispatcherServletAutoConfiguration.class, ServletWebServerFactoryAutoConfiguration.class})
public class WebMvcAutoConfiguration {
    @EnableWebMvc //使用SmartMVC的默认配置，不在starter中自定义任何组件
    @EnableConfigurationProperties({WebMvcProperties.class})
    public static class EnableWebMvcAutoConfiguration {
    }
}
