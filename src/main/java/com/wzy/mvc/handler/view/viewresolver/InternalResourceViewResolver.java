package com.wzy.mvc.handler.view.viewresolver;

import com.wzy.mvc.handler.view.InternalResourceView;
import com.wzy.mvc.handler.view.View;

public class InternalResourceViewResolver extends UrlBasedViewResolver {
    @Override
    protected View buildView(String viewName) {
        String url = getPrefix() + viewName + getSuffix();
        return new InternalResourceView(url);
    }
}
