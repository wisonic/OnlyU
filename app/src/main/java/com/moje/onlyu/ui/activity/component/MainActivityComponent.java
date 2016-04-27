package com.moje.onlyu.ui.activity.component;

import com.moje.onlyu.AppComponent;
import com.moje.onlyu.ui.activity.ActivityScope;
import com.moje.onlyu.ui.activity.MainActivity;
import com.moje.onlyu.ui.activity.module.MainActivityModule;
import com.moje.onlyu.ui.activity.presenter.MainActivityPresenter;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/27.
 */
@ActivityScope
@Component(modules = MainActivityModule.class, dependencies = AppComponent.class)
public interface MainActivityComponent {
    MainActivity inject(MainActivity mainActivity);

    MainActivityPresenter presenter();
}
