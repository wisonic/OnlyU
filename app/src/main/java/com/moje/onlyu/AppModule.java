package com.moje.onlyu;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/26.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return application;
    }
}
