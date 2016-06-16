//package com.moje.onlyu.ui.webview;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.webkit.WebSettings;
//
//public class WebViewActivity extends BaseWebViewActivity {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Bundle bundle = getIntent().getExtras();
//        if (null != bundle) {
//            int needRefresh = bundle.getInt(NEED_REFRESH);
//            WebSettings settings = getWebSetting();
//            if (needRefresh == 0) {
//                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//            } else if (needRefresh == 1) {
//                settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//            }
//            boolean isDarkTheme = bundle.getBoolean(TOPVIEWTHEME);
//            setTopTheme(isDarkTheme);
//            String s = bundle.getString(TITLE);
//            if (!TextUtils.isEmpty(s)) {
//                setTitle(s);
//            }
//            String url = bundle.getString(URL);
//            if (!TextUtils.isEmpty(url)) {
//                loadUrl(url);
//            }
//            boolean canBack = bundle.getBoolean(CAN_GOBACK);
//            if (canBack) {
//            }
//            setTitleCanBack();
//            boolean useWebTitle = bundle.getBoolean(USEWEBTITLE);
//            if (useWebTitle) {
//                setiWebListener(new IWebListener() {
//                    @Override
//                    public void onWebLoadStart() {
//
//                    }
//
//                    @Override
//                    public void onProgressChanged(int newProgress) {
//
//                    }
//
//                    @Override
//                    public void onReceivedTitle(String title) {
//                        if (!TextUtils.isEmpty(title)) {
//                            setTitle(title);
//                        }
//                    }
//                });
//            }
//        }
//    }
//}
