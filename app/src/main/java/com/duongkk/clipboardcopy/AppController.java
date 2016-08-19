package com.duongkk.clipboardcopy;

import android.app.Application;

import com.duongkk.clipboardcopy.utils.CommonUtils;

/**
 * Created by MyPC on 8/19/2016.
 */
public class AppController extends Application {
   static AppController instance;
     String imei;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        imei = CommonUtils.getImei(this);
    }

    public static synchronized AppController getInstance() {
        if(instance==null) instance = new AppController();
        return instance;
    }

    public  String getImei() {
        return imei;
    }


}
