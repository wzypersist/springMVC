package com.wzy.mvc.handler.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * getContentType: 控制视图支持的ContentType是什么，默认是返回空
 * render: 通过response把model中的数据渲染成视图返回给用户
 */
public interface View {

    default String getContentType(){
        return null;
    }

    void render(Map<String,Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
