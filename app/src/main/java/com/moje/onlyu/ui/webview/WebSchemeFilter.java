package com.moje.onlyu.ui.webview;

public abstract class WebSchemeFilter {

    private String[] actions;

    public WebSchemeFilter(String... actions) {
        this.actions = actions;
    }

    public abstract boolean action(String url);

    protected boolean filter(String url){
        for (String action : actions) {
            if (url.startsWith(action)) {
                return true;
            }
        }
        return false;
    }

    protected boolean contains(String url) {
        for (String action : actions) {
            if (url.contains(action)) {
                return true;
            }
        }
        return false;
    }
}
