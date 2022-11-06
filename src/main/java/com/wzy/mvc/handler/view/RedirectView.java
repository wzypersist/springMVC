package com.wzy.mvc.handler.view;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 1. 定义url，表示重定向的地址，实际也就是控制器中返回的视图名截取`redirect:`之后的字符串
 * 2. createTargetUrl: 根据url拼接出重定向的地址，
 * 如果有设置`contentPath`，需要把`contentPath`拼接到链接的前面；
 * 如果Model中有属性值，需要把model中的属性值拼接到链接后面
 */
public class RedirectView extends AbstractView {

    private String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception
    {
        String targetUrl = createTargetUrl(model, request);
        response.sendRedirect(targetUrl);
    }

    /**
     * model中的数据添加到URL后面作为参数
     * @param model
     * @param request
     * @return
     */
    private String createTargetUrl(Map<String, Object> model, HttpServletRequest request) {
        Assert.notNull(this.url, "url can not null");
        StringBuilder queryParams = new StringBuilder();
        model.forEach((k,v)->{
            queryParams.append(k).append("=").append(v).append("&");
        });
        if(queryParams.length()>0){
            queryParams.deleteCharAt(queryParams.length()-1);
        }
        StringBuilder targetUrl = new StringBuilder();
        if(this.url.startsWith("/")){
            targetUrl.append(getContextPath(request));
        }
        targetUrl.append(url);
        if(queryParams.length() > 0){
            targetUrl.append("?").append(queryParams.toString());
        }
        return targetUrl.toString();
    }

    private String  getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        while(contextPath.startsWith("//")){
            contextPath = contextPath.substring(1);
        }
        return contextPath;
    }

    public String getUrl() {
        return url;
    }
}
