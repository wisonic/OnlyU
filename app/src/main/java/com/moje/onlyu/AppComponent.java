package com.moje.onlyu;

import android.app.Application;

import com.moje.onlyu.data.AppServiceModule;
import com.moje.onlyu.data.api.ApiService;
import com.moje.onlyu.data.api.ApiServiceModule;
import com.moje.onlyu.model.User;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/26.
 */
@Singleton
@Component(modules = {AppModule.class, ApiServiceModule.class, AppServiceModule.class})
public interface AppComponent {

    Application getApplication();

    ApiService getService();

    User getUser();
}
