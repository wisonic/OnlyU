package com.moje.onlyu.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.okhttp.Interceptor;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.moje.onlyu.AppComponent;
import com.moje.onlyu.R;
import com.moje.onlyu.ui.activity.component.DaggerMainActivityComponent;
import com.moje.onlyu.ui.activity.module.MainActivityModule;
import com.moje.onlyu.ui.activity.presenter.MainActivityPresenter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.activity_main_tv)
    Button activityMainTv;

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        activityMainTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SystemClock.sleep(20000);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        testOKhttp("http://www.baidu.com");
//                    }
//                }).start();
//                startAsyncTask();
            }
        });
        presenter.showUserName();
    }

    void startAsyncTask() {
        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
        // the activity instance will leak.
        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... params) {
                // Do some slow work in background
                SystemClock.sleep(20000);
                return null;
            }
        }.execute();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder().appComponent(appComponent).mainActivityModule(new MainActivityModule(this)).build().inject(this);
    }

    public void initView(String userName) {
        activityMainTv.setText(userName);
    }

    private void testOKhttp(String url) {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d("Test", response.body().string());
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
