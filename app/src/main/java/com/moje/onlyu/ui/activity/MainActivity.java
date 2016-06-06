package com.moje.onlyu.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.moje.onlyu.AppComponent;
import com.moje.onlyu.R;
import com.moje.onlyu.ui.activity.component.DaggerMainActivityComponent;
import com.moje.onlyu.ui.activity.module.MainActivityModule;
import com.moje.onlyu.ui.activity.presenter.MainActivityPresenter;
import com.moje.onlyu.utils.ImmersiveFullScreenUtils;
import com.moje.onlyu.widget.TextViewTF;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.AsyncOnSubscribe;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity extends Activity {

    public int titleViewHeight = -1;
    boolean blackForMIUI = false;
    boolean showSystemBarTint = true;

    @InjectView(R.id.activity_main_tv)
    Button activityMainTv;

    @Inject
    MainActivityPresenter presenter;
    @InjectView(R.id.center_txt)
    TextView centerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImmersiveFullScreenUtils.initSystemBar(this);
        ButterKnife.inject(this);

        TextViewTF noDisturbIcon = new TextViewTF(this);
        noDisturbIcon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        noDisturbIcon.setText(R.string.chat_nodisturb);

        noDisturbIcon.layout(0, 0, 40, 40);
        noDisturbIcon.buildDrawingCache();
        Drawable drawable = new BitmapDrawable(noDisturbIcon.getDrawingCache());

        drawable.setBounds(0, 0, 40, 40);
        centerTxt.setCompoundDrawables(null, null, drawable, null);


        activityMainTv.setOnClickListener(v -> {
            Logger.d("Logger Test");
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
//            Toast.makeText(getApplicationContext(), "Lambda", Toast.LENGTH_LONG).show();
//            rxTest();
        });
//        activityMainTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.d("Logger Test");
//                Toast.makeText(getApplicationContext(), "Lambda", Toast.LENGTH_LONG).show();
////                SystemClock.sleep(20000);
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        testOKhttp("http://www.baidu.com");
////                    }
////                }).start();
////                startAsyncTask();
//            }
//        });
//        presenter.showUserName();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (showSystemBarTint) {
            titleViewHeight = ImmersiveFullScreenUtils.uiSystemBarTint(this, getWindow().getDecorView());
        }
        if (!blackForMIUI) {
            //CommonUtil.miDarkSystemBar(this);
        }
    }

    public void setBlackForMIUI(boolean blackForMIUI) {
        this.blackForMIUI = blackForMIUI;
    }

    public void setShowSystemBarTint(boolean showSystemBarTint) {
        this.showSystemBarTint = showSystemBarTint;
    }

    @Override
    protected void onResume() {
        Logger.d("onResume");
        super.onResume();
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    void startAsyncTask() {
        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
        // the activity instance will leak.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Do some slow work in background
                SystemClock.sleep(20000);
                return null;
            }
        }.execute();
    }

//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerMainActivityComponent.builder().appComponent(appComponent).mainActivityModule(new MainActivityModule(this)).build().inject(this);
//    }

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

    private void rxTest() {
        Observable.interval(1, TimeUnit.SECONDS)
                .debounce(300, TimeUnit.MILLISECONDS)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("error");
                    }

                    @Override
                    public void onNext(Long number) {
                        Logger.d("hello world");
                    }
                });

    }

    private void rxTest2() {
        Integer[] intArray = new Integer[]{1, 2, 3, 4, 5};
        File[] folders = new File[3];
        Observable.from(folders)
                .debounce(300, MILLISECONDS)
                .throttleFirst(1, TimeUnit.SECONDS)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        if (null != file) {
                            return Observable.from(file.listFiles());
                        } else {
                            return null;
                        }
                    }
                })
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        if (null != file) {
                            return file.getName().endsWith("png");
                        } else {
                            return false;
                        }
                    }
                })
                .map(new Func1<File, Bitmap>() {
                    @Override
                    public Bitmap call(File file) {
                        if (null != file) {
                            return BitmapFactory.decodeFile(file.getName(), null);
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        //Refresh UI
                        Logger.d("debounce test");
                    }
                });
    }

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {

        }
    };

    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {

        }
    };

    Observable syncObservable = Observable.create(new SyncOnSubscribe() {
        @Override
        protected Object generateState() {
            return null;
        }

        @Override
        protected Object next(Object state, Observer observer) {
            return null;
        }

        @Override
        public void call(Object o) {

        }
    });

    Observable asyncObservable = Observable.create(new AsyncOnSubscribe() {
        @Override
        protected Object generateState() {
            return null;
        }

        @Override
        protected Object next(Object state, long requested, Observer observer) {
            return null;
        }

        @Override
        public void call(Object o) {

        }
    });

    Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("");
        }
    });

    String[] words = {"Hello", "Hi", "Aloha"};

    Observable justObservable = Observable.just("1", "2", "3");

    private void testSub() {
        observable.subscribe(observer);
        observable.subscribe(subscriber);

        observable.flatMap(new Func1<String, Observable<Message>>() {
            @Override
            public Observable<Message> call(String o) {
                return null;
            }
        });
    }

    Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
        }
    };

}
