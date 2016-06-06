package com.moje.onlyu.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.moje.onlyu.R;
import com.orhanobut.logger.Logger;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    public void onBackPressed() {
        Logger.d("onBackPressed");
        super.onBackPressed();
    }

    @Override
    public void finish() {
        Logger.d("finish");
        super.finish();
    }

    @Override
    protected void onStop() {
        Logger.d("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Logger.d("onDestroy");
        super.onDestroy();
    }
}
