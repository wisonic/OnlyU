package com.moje.onlyu;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.moje.onlyu.data.api.ApiServiceModule;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2016/4/26.
 */
public class GApplication extends Application {
    private static Context sContext;
    private AppComponent appComponent;
    private static GApplication application = null;

    public static int statusBarHeight = 0;
    public static boolean isMIUIv6 = true;

    private String LOG_TAG = "OnlyU";

    public static GApplication get(Context context) {
        return (GApplication) context.getApplicationContext();
    }

    public static GApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        application = this;
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiServiceModule(new ApiServiceModule()).build();
        //Facebook Stetho 配合OkHttp方便在Chrome中进行网络数据调试
        Stetho.initializeWithDefaults(this);
        //监控UI卡顿
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        //监控内存泄漏
        LeakCanary.install(this);

        Logger.init(LOG_TAG).setMethodCount(2).hideThreadInfo().setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE).setMethodOffset(2);              // default 0
//                .logTool(new AndroidLogTool()); // custom log tool, optional

    }

    private Typeface iconfont;
    private final static String fontPath = "iconfont/iconfont.ttf";

    public Typeface getIconfont() {
        if (iconfont == null) {
            iconfont = Typeface.createFromAsset(sContext.getAssets(), fontPath);
        }
        return iconfont;
    }

    public static Context getAppContext() {
        return sContext;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
