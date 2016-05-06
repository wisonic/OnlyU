package com.moje.onlyu.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
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

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.AsyncOnSubscribe;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

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
                SystemClock.sleep(20000);
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

    private void rxTest(){
        Integer[] intArray = new Integer[]{1,2,3,4,5};
        File[] folders = new File[3];
        Observable.from(folders)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Observable.from(file.listFiles());
                    }
                })
        .filter(new Func1<File, Boolean>() {
            @Override
            public Boolean call(File file) {
                return file.getName().endsWith("png");
            }
        })
        .map(new Func1<File, Bitmap>() {
            @Override
            public Bitmap call(File file) {
                return BitmapFactory.decodeFile(file.getName(),null);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                //Refresh UI
            }
        });
    }

    Observer<String> observer = new Observer<String>(){
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

    Observable justObservable = Observable.just("1","2","3");

    private void testSub(){
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
