package com.duongkk.clipboardcopy.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.firebase.client.Firebase;

/**
 * Created by MyPC on 8/19/2016.
 */
public class AppController extends MultiDexApplication {
   static AppController instance;
     String imei;
    String coppiedText="";
    String url_root="https://clipboard-copy.firebaseio.com/";
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        imei = CommonUtils.getImei(this);
        Firebase.setAndroidContext(this);
        if(!SharedPref.getInstance(this).getString(Constant.KEY_URL_ID,"").equals("")){
            url_root+=SharedPref.getInstance(this).getString(Constant.KEY_URL_ID,"");
        }
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

    public String getUrl_root() {
        return url_root;
    }

    public void setUrl_root(String url_root) {
        this.url_root = url_root;
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
