package com.wzy.mvc.handler.view.viewresolver;

import com.wzy.mvc.handler.view.View;

public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;

}
