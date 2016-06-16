//package com.moje.onlyu.ui.webview;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ProgressBar;
//
//import com.alibaba.fastjson.JSON;
//import com.moje.onlyu.BuildConfig;
//import com.moje.onlyu.R;
//import com.moje.onlyu.ui.activity.BaseActivity;
//import com.moje.onlyu.utils.SystemUtil;
//import com.moje.onlyu.widget.IWTopTitleView;
//import com.orhanobut.logger.Logger;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
//@SuppressWarnings("unchecked")
//public abstract class BaseWebViewActivity extends Activity {
//
//    public static final String TAG = "BaseWebViewActivity";
//    public static final String TITLE = "title";
//    public static final String URL = "url";
//    public static final String TOPVIEWTHEME = "isDarkTop";
//    public static final String NEED_REFRESH = "needRefresh";
//    public static final String CAN_GOBACK = "canBack";
//    public static final String USEWEBTITLE = "usewebtitle";
//    private static final String UA = " Iwjw_Android_";
//
//    private static final String ROUTER_SERVER = "_router=server";
//
//    private final static int TIMER_BEGIN = 0;
//    private final static int TIMER_END = 1;
//
//    @InjectView(R.id.webview_title)
//    IWTopTitleView topTitleView;
//    @InjectView(R.id.web_view_main_layout)
//    View webViewMainLayout;
//    @InjectView(R.id.web_view)
//    WebView webView;
//    @InjectView(R.id.webview_progress_line)
//    ProgressBar loadingProgressBar;
//    @InjectView(R.id.webview_progress_wheel)
//    View loadingWheel;
//    @InjectView(R.id.webview_networkerror)
//    View networkErrorView;
//    @InjectView(R.id.webview_serviceerror)
//    View serviceErrorView;
//    @InjectView(R.id.webview_retry)
//    View webviewRetry;
//    @InjectView(R.id.web_view_swipelayout)
//    SwipeRefreshLayout swipeRefreshLayout;
//
//    private LoadingStyle style = LoadingStyle.LINE;
//
//    private boolean loadFinish;
//    private CountDownTimer timer;
//
//    private String firstUrl;
//    private boolean titleCanBack;
//
//    private WebViewShareBean webViewShareBean;
//
//    private List<WebSchemeFilter> schemeFilters = new ArrayList<>();
//    private List<WebJumpUiAction> jumpUiActions = new ArrayList<>();
//    private List<WebMethodCallAction> webMethodCallActions = new ArrayList<>();
//    private IWebListener iWebListener;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ButterKnife.inject(this);
//        initWebViews();
//    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    private void initWebViews() {
//
//        WebSettings settings = getWebSetting();
//        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(true);
//        settings.setDefaultTextEncodingName("UTF-8");
//        settings.setBuiltInZoomControls(false);
//        settings.setUseWideViewPort(true);
//
//        String userAgent = settings.getUserAgentString();
//        userAgent += UA + BuildConfig.VERSION_NAME;
//        settings.setUserAgentString(userAgent);
//        Logger.i(TAG, "userAgent " + userAgent);
//
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setLoadWithOverviewMode(true);
//        settings.setAppCacheEnabled(true);
//        settings.setDomStorageEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                showLoadView();
//                showContentView();
//                handler.sendEmptyMessage(0);
//                Logger.i(TAG, "onPageStarted url " + url);
//                if (null != iWebListener) {
//                    iWebListener.onWebLoadStart();
//                }
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                setFirstBaseUrl(url);
//                loadFinish = true;
//                if (null != iWebListener) {
//                    iWebListener.onReceivedTitle(view.getTitle());
//                }
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String
//                    failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//
//                view.setVisibility(View.GONE);
//                if (!SystemUtil.isNetWorkAvaliable(BaseWebViewActivity.this)) {
//                    showNetWorkError();
//                } else {
//                    showServiceError();
//                }
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                if (!url.contains(ROUTER_SERVER)) {
//
//                    try {
//                        String realPath = findRealPath(url);
//                        HashMap<String, String> map = HybridEngine.getHybridEngine().getRouter()
//                                .getRules();
//
//                        if (map.containsKey(realPath)) {
//                            String filePath = map.get(realPath);
//                            File file = new File(VersionUpdate.getHybridFileDir
//                                    (BaseWebViewActivity.this)
//                                    + File.separator + filePath);
//                            if (file.exists()) {
//                                loadFile(file.getAbsolutePath());
//                                return true;
//                            }
//                        }
//                    } catch (Exception ignored) {
//
//                    }
//                }
//
//                boolean should = super.shouldOverrideUrlLoading(view, url);
//
//                for (WebSchemeFilter webSchemeFilter : schemeFilters) {
//                    if (webSchemeFilter.action(url)) {
//                        should = true;
//                    }
//                }
//
//                return should;
//            }
//
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
//            }
//
//        });
//        webView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                if (null != iWebListener) {
//                    iWebListener.onProgressChanged(newProgress);
//                }
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                if (null != iWebListener) {
//                    iWebListener.onReceivedTitle(title);
//                }
//            }
//
//        });
//
//
//        WebSchemeFilter hybridSchemeFilter = new WebSchemeFilter() {
//            @Override
//            public boolean action(String url) {
//                return false;
//            }
//        };
//
//        WebSchemeFilter jsWebSchemeFilter = new WebSchemeFilter("iwjw://callnatvie") {
//            @Override
//            public boolean action(String url) {
//                if (filter(url)) {
//                    Uri uri = Uri.parse(url);
//                    String path = uri.getPath();
//                    String callbackName = "callback";
//                    final String callBack = uri.getQueryParameter(callbackName);
//                    HashMap<String, String> params = new HashMap<>();
//                    final StringBuilder js = new StringBuilder
//                            ("javascript:");
//                    switch (path) {
//                        case "/callapiservice":
//
//                            String apiName = "apiname";
//                            String fullApiName = "fullapiname";
//                            String api = uri.getQueryParameter(apiName) + ".rest";
//
//                            if (!TextUtils.isEmpty(api)) {
//                                if (!api.startsWith("/")) {
//                                    api = "/" + api;
//                                }
//                            }
//
//                            String fakeApi = uri.getQueryParameter(fullApiName);
//                            boolean isFakeApi = !TextUtils.isEmpty(fakeApi);
//                            String callApi = isFakeApi ? fakeApi : api;
//
//                            for (String key : uri.getQueryParameterNames()) {
//                                if (!key.contains(callbackName) && !key.contains(apiName) && !key
//                                        .contains(fullApiName)) {
//                                    params.put(key, uri.getQueryParameter(key));
//                                }
//                            }
//                            ServiceJsCallSender.exec(BaseWebViewActivity.this, isFakeApi,
//                                    callApi, params, new IwjwRespListener() {
//                                        @Override
//                                        public void onJsonSuccess(Object jsonObject) {
//                                            String parameter = JSON.toJSONString
//                                                    (jsonObject);
//                                            Logger.i(TAG, parameter);
//                                            loadJs(js.append(callBack).append("" +
//                                                    "(").append
//                                                    (parameter).append(")").toString());
//                                        }
//
//                                        @Override
//                                        public void onFailInfo(Response response, String
//                                                errorInfo) {
//                                            super.onFailInfo(response, errorInfo);
//                                            String parameter = JSON.toJSONString
//                                                    (response);
//                                            Logger.i(TAG, parameter);
//                                            loadJs(js.append(callBack).append("" +
//                                                    "(").append
//                                                    (parameter).append(")").toString());
//                                        }
//                                    });
//                            break;
//                        case "/jumpui":
//                            String pageKey = "page";
//                            String pageName = uri.getQueryParameter(pageKey);
//
//                            for (String key : uri.getQueryParameterNames()) {
//                                if (!key.contains(callbackName) && !key.contains(pageKey)) {
//                                    params.put(key, uri.getQueryParameter(key));
//                                }
//                            }
//
//                            if (!TextUtils.isEmpty(pageName)) {
//                                for (WebJumpUiAction action : jumpUiActions) {
//                                    if (action.match(pageName)) {
//                                        action.jumpUi(params);
//                                    }
//                                }
//                            }
//
//                            loadJs(js.append(callBack).append("()").toString());
//
//                            break;
//                        case "/datacollection":
//                            try {
//                                String data = uri.getQueryParameter("data");
//                                HashMap dataMap = JSON.parseObject(data, HashMap.class);
//                                EventLog.getInstance().updateJsBridgeEventInfo(dataMap);
//
//                                loadJs(js.append(callBack).append("" +
//                                        "(").append(")").toString());
//
//                            } catch (Exception e) {
//                                Logger.i(e.toString());
//                            }
//
//                            break;
//                        case "/callmethod":
//                            String methodKey = "method";
//                            String method = uri.getQueryParameter(methodKey);
//
//                            for (String key : uri.getQueryParameterNames()) {
//                                if (!key.contains(callbackName) && !key.contains(methodKey)) {
//                                    params.put(key, uri.getQueryParameter(key));
//                                }
//                            }
//
//                            Object call = new Object();
//
//                            if (!TextUtils.isEmpty(method)) {
//                                for (WebMethodCallAction action : webMethodCallActions) {
//                                    if (action.match(method)) {
//                                        call = action.call(params);
//                                    }
//                                }
//                            }
//                            loadJs(js.append(callBack).append("" +
//                                    "(").append(call).append(")").toString());
//                            break;
//                        default:
//                            break;
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        };
//
//        WebSchemeFilter phoneCallWebSchemeFilter = new WebSchemeFilter("mailto:", "geo:", "tel:") {
//
//            @Override
//            public boolean action(String url) {
//                if (filter(url)) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        };
//
//        schemeFilters.add(hybridSchemeFilter);
//        schemeFilters.add(phoneCallWebSchemeFilter);
//        schemeFilters.add(jsWebSchemeFilter);
//
//        addBaseJumpUiActions();
//        addBaseCallMethodAction();
//
//        webViewShareBean = new WebViewShareBean(this);
//
//        addTitleRightText(R.string.detail_forward, R.style.text_18_00000000, new View
//                .OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (CheckDoubleClick.isFastDoubleClick()) return;
//                showShareDialog();
//            }
//        });
//
//        shouldShowShare(false);
//
//        setLoadingStyle(LoadingStyle.LINE);
//
//        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
//        swipeRefreshLayout.setRefreshing(false);
//        swipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
//        setCanRefresh(false);
//
//    }
//
//    private void addBaseJumpUiActions() {
//
//        addJumpUiActions(new WebJumpUiAction("0") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//
//                if (params.containsKey("url")) {
//                    String url = String.valueOf(params.get("url"));
//                    Map<String, String> dataMap = ObjectUtil.newHashMap();
//                    dataMap.put(WebViewActivity.URL, url);
//                    dataMap.put(WebViewActivity.USEWEBTITLE, "true");
//                    dataMap.put(WebViewActivity.CAN_GOBACK, "true");
//                    MyIntent.startActivity(BaseWebViewActivity.this, WebViewActivity.class,
//                            dataMap);
//                }
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("1") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toSecdHouseActivity(BaseWebViewActivity.this, new Intent());
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("2") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toNewHouseHome(BaseWebViewActivity.this, new Intent());
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("3") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toRentalActivity(BaseWebViewActivity.this, new Intent());
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("4", "finance") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toFinanceHome(BaseWebViewActivity.this, new Intent());
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("5") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("6") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                if (params.containsKey("url")) {
//                    String url = params.get("url");
//                    IndexChoiceBusinessView.toEncyclopedias(BaseWebViewActivity.this, url);
//                }
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("7") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                if (params.containsKey("url")) {
//                    String url = params.get("url");
//                    IndexChoiceBusinessView.toPpTalk(BaseWebViewActivity.this, url);
//                }
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("8") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toCalcultorActivity(BaseWebViewActivity.this);
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("9") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("10") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//            }
//        });
//
//        addJumpUiActions(new WebJumpUiAction("11") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                Intent intent = new Intent(BaseWebViewActivity.this, DemandTreasureActivity.class);
//                startActivity(intent);
//            }
//        });
//        addJumpUiActions(new WebJumpUiAction("12") {
//            @Override
//            public void jumpUi(HashMap<String, String> params) {
//                Intent intent = new Intent(BaseWebViewActivity.this, CapitalActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    private void addBaseCallMethodAction() {
//
//        addMethodCallAction(new WebMethodCallAction("getcurrenttimeexact") {
//            @Override
//            public Object call(HashMap params) {
//                return TimeUtil.getCurrentTimeExact().getTimeInMillis();
//            }
//        });
//
//    }
//
//    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout
//            .OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//            callJsRefresh();
//            swipeRefreshLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (null != swipeRefreshLayout) {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//            }, 2000);
//        }
//    };
//
//    protected void callJsRefresh() {
//        loadJs("javascript:callJs('refresh',{})");
//    }
//
//    @OnClick(R.id.webview_retry)
//    void clickRetry() {
//        reloadData();
//    }
//
//    protected void setCanRefresh(boolean canRefresh) {
//        swipeRefreshLayout.setEnabled(canRefresh);
//    }
//
//    protected void addTitleRightText(int resId, int style, View.OnClickListener event) {
//        topTitleView.addRightText(resId, style, event);
//    }
//
//    public void addTitleRightText(String text, int style, View.OnClickListener event) {
//        topTitleView.addRightText(text, style, event);
//    }
//
//    protected void addTitleCustomView(View view, View.OnClickListener onClickListener) {
//        topTitleView.addMiddleView(view, onClickListener);
//    }
//
//    protected void addLeftView(View view, View.OnClickListener onClickListener) {
//        topTitleView.addLeftView(view, onClickListener);
//    }
//
//    public void showMiddleView(boolean show) {
//        topTitleView.showMiddleView(show);
//    }
//
//    protected void setiWebListener(IWebListener iWebListener) {
//        this.iWebListener = iWebListener;
//    }
//
//    WebSettings getWebSetting() {
//        return webView.getSettings();
//    }
//
//    public void setTitle(String title) {
//        topTitleView.setTitleText(title);
//    }
//
//    public void setEmptyTitle() {
//        topTitleView.setTitleText("");
//    }
//
//    public void showBack(boolean show) {
//        topTitleView.showBackBtn(show);
//    }
//
//    public void showRightTextIcon(boolean show) {
//        topTitleView.showRightTextIcon(show ? View.VISIBLE : View.GONE);
//    }
//
//    public void setNoTitle() {
//        topTitleView.setVisibility(View.GONE);
//        CommonUtil.uiSystemBarTintNoTitle(this, webViewMainLayout);
//    }
//
//    protected void setTopTheme(boolean isDark) {
//        topTitleView.setDark(isDark);
//    }
//
//    /**
//     * 设置webview的title可以使用webview返回 一些有重定向的页面使用时会有问题，因为重定向页面无法后退
//     * 默认back键直接finish
//     */
//    protected void setTitleCanBack(final ITitleClickListener listener) {
//
//        titleCanBack = true;
//
//        topTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
//            @Override
//            public boolean onBackClick() {
//                if (null != listener) {
//                    listener.onClick();
//                }
//                return false;
//            }
//        });
//    }
//
//    protected void setTitleCanBack() {
//        setTitleCanBack(null);
//    }
//
//    public void loadUrl(String url) {
//
//        if (null != webView) {
//            firstUrl = null;
//
//            try {
//                if (url.contains(ROUTER_SERVER)) {
//                    webView.loadUrl(url);
//                } else {
//
//                    String realPath = findRealPath(url);
//                    HashMap<String, String> map = HybridEngine.getHybridEngine().getRouter().getRules();
//
//
//                    if (map.containsKey(realPath)) {
//                        String filePath = map.get(realPath);
//                        File file = new File(VersionUpdate.getHybridFileDir(BaseWebViewActivity
//                                .this)
//                                + File.separator + filePath);
//                        if (file.exists()) {
//                            loadFile(file.getAbsolutePath());
//                        } else {
//                            webView.loadUrl(url);
//                        }
//                    } else {
//                        webView.loadUrl(url);
//                    }
//                }
//            } catch (Exception e) {
//                webView.loadUrl(url);
//            }
//        }
//    }
//
//    private void loadFile(String filePath) {
//        if (null != webView) {
//            webView.loadUrl("file:///" + filePath);
//        }
//    }
//
//    public void loadJs(String js) {
//        if (null != webView) {
//            webView.loadUrl(js);
//        }
//    }
//
//    @Override
//    public void reloadData() {
//        showLoadView();
//        webView.reload();
//    }
//
//    protected void goBack() {
//        webView.goBack();
//    }
//
//    @Override
//    public void showLoadView() {
//        loadFinish = false;
//        shouldShowLoading(true);
//        webView.setVisibility(View.GONE);
//        webviewRetry.setVisibility(View.GONE);
//        serviceErrorView.setVisibility(View.GONE);
//        networkErrorView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void showContentView() {
//        webView.setVisibility(View.VISIBLE);
//        serviceErrorView.setVisibility(View.GONE);
//        networkErrorView.setVisibility(View.GONE);
//        webviewRetry.setVisibility(View.GONE);
//    }
//
//    private void showNetWorkError() {
//        networkErrorView.setVisibility(View.VISIBLE);
//        webviewRetry.setVisibility(View.VISIBLE);
//        webView.setVisibility(View.GONE);
//        serviceErrorView.setVisibility(View.GONE);
//        shouldShowLoading(false);
//    }
//
//    private void showServiceError() {
//        webviewRetry.setVisibility(View.VISIBLE);
//        serviceErrorView.setVisibility(View.VISIBLE);
//        webView.setVisibility(View.GONE);
//        networkErrorView.setVisibility(View.GONE);
//        shouldShowLoading(false);
//    }
//
//    public void setLoadingStyle(LoadingStyle loadingStyle) {
//        this.style = loadingStyle;
//        switch (loadingStyle) {
//            case LINE:
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loadingWheel.setVisibility(View.GONE);
//                break;
//            case WHEEL:
//                loadingProgressBar.setVisibility(View.GONE);
//                loadingWheel.setVisibility(View.VISIBLE);
//                break;
//        }
//    }
//
//    public void shouldShowLoading(boolean should) {
//        getLoadingView().setVisibility(should ? View.VISIBLE : View.GONE);
//    }
//
//    private View getLoadingView() {
//        return style.equals(LoadingStyle.LINE) ? loadingProgressBar : loadingWheel;
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case TIMER_BEGIN:
//                    if (null != timer) {
//                        timer.cancel();
//                    }
//                    initProgress();
//                    initTimeCounter();
//                    break;
//                case TIMER_END:
//                    timer.cancel();
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        if (null != timer) {
//            timer.cancel();
//        }
//        webView.destroy();
//        swipeRefreshLayout.setOnRefreshListener(null);
//        super.onDestroy();
//    }
//
//    private void initTimeCounter() {
//        timer = new CountDownTimer(1000 * 10, 50) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (loadingProgressBar.getProgress() >= 70) {
//                    if (loadingProgressBar.getProgress() <= 95) {
//                        loadingProgressBar.setProgress(loadingProgressBar.getProgress() + 1);
//                    }
//                } else {
//                    loadingProgressBar.setProgress(loadingProgressBar.getProgress() + 4);
//                }
//                if (loadingProgressBar.getProgress() >= 95 && loadFinish) {
//                    loadingProgressBar.setVisibility(View.GONE);
//                    handler.sendEmptyMessage(1);
//                }
//            }
//
//            //设置10秒的计时器。基本保证onFinish时 loadFinish = true;
//            @Override
//            public void onFinish() {
//                //if (loadFinish)
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        };
//        timer.start();
//    }
//
//    private void initProgress() {
//        loadingProgressBar.setProgress(0);
//    }
//
//    //设置一级页面地址
//    private void setFirstBaseUrl(String baseUrl) {
//        if (TextUtils.isEmpty(firstUrl)) {
//            this.firstUrl = baseUrl;
//        }
//    }
//
//    //当前url是否为一级页面
//    public boolean isFirstBaseUrl() {
//        if(null != webView) {
//            String loadingUrl = webView.getUrl();
//            if (!TextUtils.isEmpty(loadingUrl) && !TextUtils.isEmpty(firstUrl)) {
//                String compareUrlloading = loadingUrl;
//                String compareUrlfirst = firstUrl;
//                if (compareUrlloading.endsWith("/")) {
//                    compareUrlloading = compareUrlloading.substring(0, compareUrlloading.length() - 1);
//
//                }
//                if (firstUrl.endsWith("/")) {
//                    compareUrlfirst = compareUrlfirst.substring(0, compareUrlfirst.length() - 1);
//                }
//                Logger.i(TAG, "url " + compareUrlloading + " firstUrl " + compareUrlfirst);
//                return compareUrlloading.equals(compareUrlfirst);
//            } else {
//                return true;
//            }
//        }
//        return true;
//    }
//
//    protected void addJumpUiActions(WebJumpUiAction webJumpUiAction) {
//        jumpUiActions.add(webJumpUiAction);
//    }
//
//    protected void addMethodCallAction(WebMethodCallAction webMethodCallAction) {
//        webMethodCallActions.add(webMethodCallAction);
//    }
//
//    private boolean cangoBack() {
//        return webView.canGoBack();
//    }
//
//    @Override
//    public int getLayout() {
//        return R.layout.webview_layout;
//    }
//
//    public WebViewShareBean getWebViewShareBean() {
//        return webViewShareBean;
//    }
//
//    private void showShareDialog() {
//        if (null != webViewShareBean) {
//            webViewShareBean.showShareDialog(getSupportFragmentManager());
//        }
//    }
//
//    public void showLeftCustomView(boolean should) {
//        topTitleView.showLeftCustomView(should ? View.VISIBLE : View.GONE);
//    }
//
//    public void shouldShowShare(boolean should) {
//        topTitleView.showRightTextIcon(should ? View.VISIBLE : View.GONE);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//        Logger.i(TAG, "webView canback " + cangoBack());
//
//        //重定向问题
//        //由pc.iwjwtest.com 重定向到m.iwjwtest.com上 不能goback
//
//        if (titleCanBack) {
//            if (!isFirstBaseUrl()) {
//
//                if (cangoBack()) {
//                    goBack();
//                    showLeftCustomView(true);
//                } else {
//                    super.onBackPressed();
//                }
//            } else {
//                super.onBackPressed();
//            }
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    private String findRealPath(String url) {
//
//        String keyPath = null;
//        Uri uri = Uri.parse(url);
//        Log.i(TAG, "findRealPath url path == " + uri.getPath());
//
//        String path = uri.getPath();
//        if (!TextUtils.isEmpty(path)) {
//            String[] ps = path.split("/");
//            keyPath = ps[ps.length - 1];
//            if (keyPath.contains(".")) {
//                keyPath = keyPath.split("\\.")[0];
//            }
//        }
//        return keyPath;
//    }
//
//
//    public interface IWebListener {
//        void onWebLoadStart();
//
//        void onProgressChanged(int newProgress);
//
//        void onReceivedTitle(String title);
//    }
//
//    public interface ITitleClickListener {
//        void onClick();
//    }
//
//    protected enum LoadingStyle {
//        LINE,
//        WHEEL
//    }
//
//}
