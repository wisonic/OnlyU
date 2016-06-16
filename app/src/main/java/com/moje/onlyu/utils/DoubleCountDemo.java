package com.moje.onlyu.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/6/7.
 */
public class DoubleCountDemo {

    int element = 0;

    public DoubleCountDemo() {
        element = 0;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =0; i < 500; i++) {
                    Log.d("WTF",Thread.currentThread().getId() + " i: " + i);
                    element++;
                    Log.d("WTF",Thread.currentThread().getId()+ " i: " + i + "element: " + element);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j=0; j < 500; j++) {
//                    Log.d("WTF",Thread.currentThread().getId() + " j: " + j);
                    element++;
//                    Log.d("WTF",Thread.currentThread().getId()  + " j: " + j+  "element: " + element);
                }
            }
        }).start();
    }

}
