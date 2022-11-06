package com.wzy.mvc.handler;

import com.sun.deploy.net.HttpResponse;
import com.wzy.mvc.ModelAndView;
import com.wzy.mvc.handler.interceptor.HandlerInterceptor;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecutionChain {

    private HandlerMethod handler;
    private List<HandlerInterceptor> interceptors = new ArrayList<>();
    private int interceptorIndex = -1;

    public HandlerExecutionChain(HandlerMethod handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        if(!CollectionUtils.isEmpty(interceptors)){
            this.interceptors = interceptors;
        }
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception{
        if (CollectionUtils.isEmpty(interceptors)) {
            return true;
        }
        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);
            if(!interceptor.preHandle(request,response,this.handler)){
                triggerAfterCompletion(request,response,null);
            }
            this.interceptorIndex = i;

        }
        return true;
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)) {
            return;
        }
        for(int i = interceptors.size()-1;i >= 0; i--){
            this.interceptors.get(i).postHandle(request,response,this.handler,mv);
        }
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if(CollectionUtils.isEmpty(interceptors)){
            return;
        }
        for (int i = this.interceptorIndex; i >= 0 ; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.afterCompletion(request,response,this.handler,ex);

        }
    }


    public HandlerMethod getHandler() {
        return handler;
    }

    public List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }
}
