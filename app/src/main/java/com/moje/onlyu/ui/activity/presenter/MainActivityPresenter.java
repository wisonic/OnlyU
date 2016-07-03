package com.moje.onlyu.ui.activity.presenter;

import com.moje.onlyu.model.User;
import com.moje.onlyu.ui.activity.MainActivity;
import com.moje.onlyu.widget.PedometerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

//    public void bindingData(ActivityMainBinding binding){
//        binding.setUser(user);
//    }

    public void setPedometerView(){
        PedometerView mPedometerView = mainActivity.getPedometerView();
        mPedometerView.setData(2532,3125,20,getNowTime(),avgStep(getDayStepNum()),getDayStepNum(),getDate());
        mPedometerView.startAnimator();
    }

    public String getNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        return df.format(new Date());
    }

    public int[] getDayStepNum(){
        int[] stepNum = {1000,1500,400,3000,5000,3654,125};
        return stepNum;
    }

    public int avgStep(int[] steps){
        int total = 0;
        for(int step:steps){
            total +=step;
        }
        int avg = total/steps.length;
        return avg;
    }

    public String[] getDate(){
        String[] dates = new String[7];
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        for (int i=0;i<7;i++){
            dates[i] = (day-(7-i))+"日";
        }
        return dates;
    }

    public void showUserName(){
        mainActivity.initView(user.getName());
    }
}
