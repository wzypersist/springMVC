package com.wzy.mvc.handler.view.viewresolver;

import com.wzy.mvc.handler.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 1. 定义一个默认的空视图`UNRESOLVED_VIEW`，当通过`viewName`解析不到视图返回null时，把默认的视图放入到缓存中
 * 2. 由于可能存在同一时刻多个用户请求到同一个视图，所以需要使用`synchronized`加锁
 * 3. 如果缓存中获取到的视图是`UNRESOLVED_VIEW`，那么就返回null
 */
public abstract class AbstractCachingViewResolver implements ViewResolver{

    private final Object lock = new Object();
    private static final View UNRESOLVED_VIEW = (model, request, response) -> {
    };
    private Map<String ,View> cacheViews = new HashMap<>();

    @Override
    public View resolveViewName(String viewName) throws Exception {
        View view = cacheViews.get(viewName);
        if(Objects.nonNull(view)){
            return (view != UNRESOLVED_VIEW ? view : null);
        }
        synchronized (lock){
            view = cacheViews.get(viewName);
            if(Objects.nonNull(view)){
                return (view != UNRESOLVED_VIEW ? view : null);
            }
            view = createView(viewName);
            if(Objects.isNull(view)){
                view = UNRESOLVED_VIEW;
            }
            cacheViews.put(viewName,view);
        }
        return (view != UNRESOLVED_VIEW ? view : null);
    }

    protected abstract View createView(String viewName);
}
