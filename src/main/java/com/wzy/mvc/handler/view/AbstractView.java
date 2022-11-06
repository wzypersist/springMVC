package com.wzy.mvc.handler.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * View接口的抽象实现类，具体逻辑交给子类实现
 * prepareResponse: 在实施渲染之前需要做的一些工作放入到这个方法中，比如：设置响应的头信息
 * renderMergedOutputModel: 执行渲染的逻辑都将放入到这个方法中
 */
public abstract class AbstractView implements View {

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.prepareResponse(request,response);
        this.renderMergedOutputModel(model, request, response);
    }

    protected void prepareResponse(HttpServletRequest request,
                                   HttpServletResponse response) {
    }

    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception{
    }



}
