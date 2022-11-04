package com.wzy.mvc.handler.interceptor;

import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {

    private List<MappedInterceptor> mappedInterceptors = new ArrayList<>();

    public MappedInterceptor addMapperInterceptor(HandlerInterceptor interceptor){
        MappedInterceptor mappedInterceptor = new MappedInterceptor(interceptor);
        mappedInterceptors.add(mappedInterceptor);
        return mappedInterceptor;
    }

    public List<MappedInterceptor> getMappedInterceptors() {
        return mappedInterceptors;
    }
}
