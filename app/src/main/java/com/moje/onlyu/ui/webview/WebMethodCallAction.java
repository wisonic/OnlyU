package com.moje.onlyu.ui.webview;

import java.util.HashMap;


public abstract class WebMethodCallAction<T> {

    private String method;

    public WebMethodCallAction(String method) {
        this.method = method;
    }

    public abstract T call(HashMap params);

    public boolean match(String url){
        return url.equals(method);
    }
}
