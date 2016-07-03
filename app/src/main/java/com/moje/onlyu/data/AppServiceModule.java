package com.moje.onlyu.data;

import com.moje.onlyu.model.User;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/26.
 */
@Module
public class AppServiceModule {

    @Provides
    User provideUser(){
        User user = new User();
        user.setId("9587");
        user.setName("Grant");
        return user;
    }
}
