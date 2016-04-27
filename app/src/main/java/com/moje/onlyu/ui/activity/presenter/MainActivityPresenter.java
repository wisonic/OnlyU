package com.moje.onlyu.ui.activity.presenter;

import com.moje.onlyu.model.User;
import com.moje.onlyu.ui.activity.MainActivity;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MainActivityPresenter {
    private MainActivity mainActivity;
    private User user;

    public MainActivityPresenter(MainActivity mainActivity, User user){
        this.mainActivity = mainActivity;
        this.user = user;
    }

    public void showUserName(){
        mainActivity.initView(user.getName());
    }
}
