package com.duongkk.clipboardcopy.application;

import android.app.Application;

import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.firebase.client.Firebase;

/**
 * Created by MyPC on 8/19/2016.
 */
public class AppController extends Application {
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
