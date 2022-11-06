package com.wzy.mvc.handler.view.viewresolver;

import com.wzy.mvc.handler.view.InternalResourceView;
import com.wzy.mvc.handler.view.RedirectView;
import com.wzy.mvc.handler.view.View;

/**
 * 1. 当viewName以`redirect:`开头，那么返回`RedirectView`视图
 * 2. 当viewName以`forward:`开头，那么返回`InternalResourceView`视图
 * 3. 如果都不是，那么就执行模板方法`buildView`
 */
public abstract class UrlBasedViewResolver extends AbstractCachingViewResolver {

    public static final String REDIRECT_URL_PREFIX = "redirect:";
    public static final String FORWARD_URL_PREFIX = "forward:";

    private String prefix = "";
    private String suffix = "";

    @Override
    protected View createView(String viewName) {
        if(viewName.startsWith(REDIRECT_URL_PREFIX)){
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl);
        }
        if(viewName.startsWith(FORWARD_URL_PREFIX)){
            String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
            return new InternalResourceView(forwardUrl);
        }
        return buildView(viewName);
    }

    protected abstract View buildView(String viewName);

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
