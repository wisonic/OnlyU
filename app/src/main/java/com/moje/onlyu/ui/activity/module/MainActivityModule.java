package com.moje.onlyu.ui.activity.module;

import com.moje.onlyu.model.User;
import com.moje.onlyu.ui.activity.ActivityScope;
import com.moje.onlyu.ui.activity.MainActivity;
import com.moje.onlyu.ui.activity.presenter.MainActivityPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/26.
 */
@Module
public class MainActivityModule {

    private MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActiviy(){
        return mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivityPresenter provideMainActivityPresenter(MainActivity mainActivity, User user){
        return new MainActivityPresenter(mainActivity, user);
    }
}
