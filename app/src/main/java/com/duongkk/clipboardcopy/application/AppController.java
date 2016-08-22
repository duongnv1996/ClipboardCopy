package com.duongkk.clipboardcopy.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.firebase.client.Firebase;

/**
 * Created by MyPC on 8/19/2016.
 */
public class AppController extends MultiDexApplication {
   static AppController instance;
     String imei;
    String coppiedText="";
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        imei = CommonUtils.getImei(this);
        Firebase.setAndroidContext(this);

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized AppController getInstance() {
        if(instance==null) instance = new AppController();
        return instance;
    }

    public String getCoppiedText() {
        return coppiedText;
    }

    public void setCoppiedText(String coppiedText) {
        this.coppiedText = coppiedText;
    }

    public  String getImei() {
        return imei;
    }


}
