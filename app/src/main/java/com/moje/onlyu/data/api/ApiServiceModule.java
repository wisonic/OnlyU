package com.moje.onlyu.data.api;

import android.app.Application;

import com.facebook.stetho.okhttp3.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/4/26.
 */
@Module
public class ApiServiceModule {
    private static final String ENDPOINT = "";
    private static final int CONN_TIME_OUT = 60 * 1000;
    private static final int READ_TIME_OUT = 60 * 1000;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            okHttpClient.networkInterceptors().add( new StethoInterceptor());
        }
//        okHttpClient.setConnectTimeout(CONN_TIME_OUT, TimeUnit.MILLISECONDS);
//        okHttpClient.setReadTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit  provideRestAdapter(Application application, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(AppConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit  restAdapter) {
        return restAdapter.create(ApiService.class);
    }
}
