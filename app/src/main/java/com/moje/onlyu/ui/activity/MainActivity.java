package com.moje.onlyu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.moje.onlyu.AppComponent;
import com.moje.onlyu.R;
import com.moje.onlyu.rx.MyBus;
import com.moje.onlyu.rx.TapEvent;
import com.moje.onlyu.tools.utils.MemoryUtils;
import com.moje.onlyu.ui.activity.component.DaggerMainActivityComponent;
import com.moje.onlyu.ui.activity.module.MainActivityModule;
import com.moje.onlyu.ui.activity.presenter.MainActivityPresenter;
import com.moje.onlyu.widget.PedometerView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    private CompositeSubscription _subscriptions;

    @InjectView(R.id.activity_main_tv)
    TextView activityMainTv;

    @InjectView(R.id.jump)
    Button jumpButton;

    @InjectView(R.id.pedometerview)
    PedometerView pedometerView;

    @Inject
    MainActivityPresenter presenter;


    @OnClick(R.id.jump)
    public void onTabButtonClicked(){
        startActivity(new Intent(this,FullscreenActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        User user = new User("Test", "User");
//        binding.setUser(user);
        ButterKnife.inject(this);
//        presenter.bindingData(binding);
//        presenter.showUserName();

        presenter.setPedometerView();


        _subscriptions = new CompositeSubscription();
        _subscriptions.add(MyBus.getInstance().toObservable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object event) {
                if(event instanceof TapEvent){
                    Timber.d("activityMainTv.setText(\"Good job!\");");
                    activityMainTv.setText("Good job!");
                }
            }
        }));

        observable.lift(new Observable.Operator() {
            @Override
            public Object call(Object o) {
                return null;
            }
        });

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder().appComponent(appComponent).mainActivityModule(new MainActivityModule(this)).build().inject(this);
    }

    public void initView(String userName) {
        activityMainTv.setText(userName);
    }

    public PedometerView getPedometerView(){
        return pedometerView;
    }

    public void doSomeTest(View v){
        String allocationMemory = MemoryUtils.getAllocationMemory(this);
        String availMemory = MemoryUtils.getAvailMemory(this);
        String totalMemory = MemoryUtils.getTotalMemory(this);
        Log.d("doSomeTest", allocationMemory);
        Log.d("doSomeTest", availMemory);
        Log.d("doSomeTest", totalMemory);

    }

    Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {

        }
    });

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

    Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {

        }
    };

}
