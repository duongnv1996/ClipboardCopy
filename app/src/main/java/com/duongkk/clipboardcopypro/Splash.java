package com.duongkk.clipboardcopypro;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopypro.application.AppController;
import com.duongkk.clipboardcopypro.utils.Constant;
import com.duongkk.clipboardcopypro.utils.RLog;
import com.duongkk.clipboardcopypro.utils.SharedPref;


public class Splash extends AppCompatActivity {
    MaterialDialog dialogConfirmSameApp;

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
//                int theme = SharedPref.getInstance(getApplicationContext()).getInt(Constant.KEY_THEME,  AppCompatDelegate.MODE_NIGHT_AUTO);
//                getDelegate().setLocalNightMode(theme);
                if( hasTheSameAppInstalled("com.duongkk.clipboardcopy")) {
                    if(dialogConfirmSameApp ==null){
                        dialogConfirmSameApp =   new MaterialDialog.Builder(Splash.this)
                                .title(R.string.error)
                                .content(R.string.msg_exist_same_app)
                                .positiveText(getString(R.string.dimiss))                             .positiveColor(Color.GRAY)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialogConfirmSameApp.dismiss();
                                        finish();
                                    }
                                })
                                .cancelable(false)
                                .build();
                    }
                    dialogConfirmSameApp.show();
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 100);
                    else
                        goAuth();
                } else {
                    goAuth();
                }


            }
        }.start();
    }
    private boolean hasTheSameAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
    private void goAuth() {
        AppController.getInstance().getImei();
        String urlID = SharedPref.getInstance(getApplicationContext()).getString(Constant.KEY_URL_ID, "");
        RLog.e(urlID);
        if(urlID.equals("")){
            startActivity(new Intent(Splash.this,LoginActivity.class));
        }else {
            startActivity(new Intent(Splash.this, MainActivity.class));
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            goAuth();

        }else{
           new MaterialDialog.Builder(this).positiveText("OK").positiveColor(Color.GRAY).title(getString(R.string.permission)).content(R.string.right).onPositive(new MaterialDialog.SingleButtonCallback() {
               @Override
               public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                       if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                           requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 100);
                       else
                           goAuth();
                   }
               }
           }).cancelable(false).show();

        }
    }
}
