package com.moje.onlyu;

import android.app.Application;
import android.content.Context;

import com.moje.onlyu.data.api.ApiServiceModule;

import timber.log.Timber;

/**
 * Created by Administrator on 2016/4/26.
 */
public class GApplication extends Application {

    private AppComponent appComponent;

    public static GApplication get(Context context) {
        return (GApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiServiceModule(new ApiServiceModule()).build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
