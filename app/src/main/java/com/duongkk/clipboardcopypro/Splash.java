package com.duongkk.clipboardcopypro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.view.MotionEvent;
import android.view.View;

import com.duongkk.clipboardcopypro.utils.Constant;
import com.duongkk.clipboardcopypro.utils.SharedPref;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                int theme = SharedPref.getInstance(getApplicationContext()).getInt(Constant.KEY_THEME,  AppCompatDelegate.MODE_NIGHT_AUTO);
                getDelegate().setLocalNightMode(theme);
                if(SharedPref.getInstance(getApplicationContext()).getString(Constant.KEY_URL_ID, "").equals("")){
                    startActivity(new Intent(Splash.this,LoginActivity.class));
                }else {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                }
                finish();

            }
        }.start();
    }

}
