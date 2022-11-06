package com.wzy.mvc.handler.view.viewresolver;

import com.sun.xml.internal.ws.client.RequestContext;
import com.wzy.mvc.handler.view.RedirectView;
import com.wzy.mvc.handler.view.View;
import com.wzy.mvc.utils.RequestContextHolder;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 1. getCandidateViews: 通过视图名字使用`ViewResolver`解析出所有不为null的视图，
 * 如果默认视图不为空，把所有视图返回作为候选视图
 * 2. getBestView: 从request中拿出头信息`Accept`，
 * 根据视图的ContentType从候选视图中匹配出最优的视图返回
 */
@Setter
public class ContentNegotiatingViewResolver implements ViewResolver, InitializingBean {

    private List<ViewResolver> viewResolvers = new ArrayList<>();
    private List<View> defaultViews = new ArrayList<>();

    @Override
    public View resolveViewName(String viewName) throws Exception {
        List<View> candidateViews = getCandidateView(viewName);
        View bestView = getBestView(candidateViews);
        if(Objects.nonNull(bestView)){
            return bestView;
        }
        return null;
    }

    private View getBestView(List<View> candidateViews) {
        Optional<View> viewOptional = candidateViews.stream()
                .filter(view -> view instanceof RedirectView)
                .findAny();
        if(viewOptional.isPresent()){
            return viewOptional.get();
        }
        HttpServletRequest request = RequestContextHolder.getRequest();
        Enumeration<String> acceptHeaders = request.getHeaders("Accept");
        while (acceptHeaders.hasMoreElements()) {
            for (View view : candidateViews) {
                if (acceptHeaders.nextElement().contains(view.getContentType())) {
                    return view;
                }
            }
        }
        return null;
    }

    private List<View> getCandidateView(String viewName) throws Exception{
        List<View> candidateViews = new ArrayList<>();
        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);
            if(Objects.nonNull(view)){
                candidateViews.add(view);
            }
        }
        if(!CollectionUtils.isEmpty(defaultViews)){
            candidateViews.addAll(defaultViews);
        }
        return candidateViews;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(viewResolvers, "viewResolvers can not null");
    }
}
