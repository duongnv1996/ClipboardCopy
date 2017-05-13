package com.duongkk.clipboardcopypro.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.duongkk.clipboardcopypro.utils.CommonUtils;
import com.duongkk.clipboardcopypro.utils.Constant;
import com.duongkk.clipboardcopypro.utils.SharedPref;

/**
 * Created by MyPC on 8/28/2016.
 */

public  class ReceiverOff extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "test");
        String action = intent.getAction();
        if(CommonUtils.YES_ACTION.equals(action)) {
            Log.v("shuffTest","Pressed YES");
            SharedPref.getInstance(context).putBoolean(Constant.KEY_ON_SERVICE,false);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        }
    }

}
