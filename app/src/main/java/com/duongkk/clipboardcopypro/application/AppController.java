package com.duongkk.clipboardcopypro.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.crittercism.app.Crittercism;
import com.duongkk.clipboardcopypro.utils.CommonUtils;
import com.duongkk.clipboardcopypro.utils.Constant;
import com.duongkk.clipboardcopypro.utils.SharedPref;
import com.firebase.client.Firebase;

/**
 * Created by MyPC on 8/19/2016.
 */
public class AppController extends MultiDexApplication {
    static AppController instance;
     String imei;
    String coppiedText = "";
    String url_root = "https://clipboard-copy.firebaseio.com/";
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Crittercism.initialize(getApplicationContext(), "4396502ea66c44f59904dc8f4bcab37400555300");
        Firebase.setAndroidContext(this);
        if (!SharedPref.getInstance(this).getString(Constant.KEY_URL_ID, "").equals("")) {
            url_root += SharedPref.getInstance(this).getString(Constant.KEY_URL_ID, "");
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    public static synchronized AppController getInstance() {
        if (instance == null) instance = new AppController();
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

    public  void setCoppiedText(String coppiedText) {
        this.coppiedText = coppiedText;
    }

    public String getImei() {
        imei = CommonUtils.getImei(this);
        return imei;
    }


}
