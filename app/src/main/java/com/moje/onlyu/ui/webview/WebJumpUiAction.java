package com.moje.onlyu.ui.webview;

import java.util.HashMap;


public abstract class WebJumpUiAction {
    private String[] pages;

    public WebJumpUiAction(String... pages) {
        this.pages = pages;
    }

    public abstract void jumpUi(HashMap<String, String> params);

    public boolean match(String pageName) {
        for (String page : pages) {
            if (page.equals(pageName)) {
                return true;
            }
        }
        return false;
    }
}
