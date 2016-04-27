package com.moje.onlyu.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moje.onlyu.AppComponent;
import com.moje.onlyu.GApplication;

/**
 * Created by Administrator on 2016/4/26.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(GApplication.get(this).getAppComponent());
    }

    protected abstract void setupActivityComponent(AppComponent appComponent);
}
