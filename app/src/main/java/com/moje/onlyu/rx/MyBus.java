package com.moje.onlyu.rx;

/**
 * Created by gongqibing on 16/5/7.
 */
public class MyBus {
    private static RxBus rxBus;

    public static RxBus getInstance(){
        if(null == rxBus){
            rxBus = new RxBus();
        }
        return rxBus;
    }
}
