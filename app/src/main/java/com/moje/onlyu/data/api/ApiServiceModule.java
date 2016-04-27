package com.moje.onlyu.data.api;

import android.app.Application;

import com.avos.avoscloud.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

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
        okHttpClient.setConnectTimeout(CONN_TIME_OUT, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Application application, OkHttpClient okHttpClient) {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setClient(new OkClient()).setEndpoint(ENDPOINT);
        return builder.build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(RestAdapter restAdapter){
        return restAdapter.create(ApiService.class);
    }
}
