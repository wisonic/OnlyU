package com.moje.onlyu.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.moje.onlyu.AppComponent;
import com.moje.onlyu.R;
import com.moje.onlyu.ui.activity.component.DaggerMainActivityComponent;
import com.moje.onlyu.ui.activity.module.MainActivityModule;
import com.moje.onlyu.ui.activity.presenter.MainActivityPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.activity_main_tv)
    TextView activityMainTv;

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        presenter.showUserName();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder().appComponent(appComponent).mainActivityModule(new MainActivityModule(this)).build().inject(this);
    }

    public void initView(String userName) {
        activityMainTv.setText(userName);
    }
}
