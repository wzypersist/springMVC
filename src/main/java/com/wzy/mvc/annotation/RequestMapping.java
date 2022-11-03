package com.wzy.mvc.annotation;

import com.wzy.mvc.http.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String path();

    RequestMethod method() default RequestMethod.GET;


}
