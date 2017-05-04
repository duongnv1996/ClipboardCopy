package com.duongkk.clipboardcopy.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.duongkk.clipboardcopy.R;
import com.duongkk.clipboardcopy.application.AppController;
import com.duongkk.clipboardcopy.models.Message;
import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.RLog;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by MyPC on 7/29/2016.
 */
public class ClipboardListener extends Service implements ChildEventListener, ClipboardManager.OnPrimaryClipChangedListener, ValueEventListener {
    ClipboardManager clipboard;
    private Firebase mRoot;
    private String contentFromServer;
    String mPreviousText = "";
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        RLog.e(SharedPref.getInstance(this).getString(Constant.KEY_URL_ID, ""));
        mRoot = new Firebase(Constant.URL_ROOT_FINAL + SharedPref.getInstance(this).getString(Constant.KEY_URL_ID, ""));
        mRoot.addChildEventListener(this);
        mRoot.addListenerForSingleValueEvent(this);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clipboard.addPrimaryClipChangedListener(this);
        Boolean isShow = SharedPref.getInstance(this).getBoolean(Constant.KEY_NOTIFY, true);
        if (isShow) {
            CommonUtils.showNotification(this);
        } else {

            notificationManager.cancel(1);

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clipboard.removePrimaryClipChangedListener(this);
        notificationManager.cancelAll();
        stopSelf();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    boolean timeToNotify= false;
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        try {

            if (clipboard != null) {
                Message message = dataSnapshot.getValue(Message.class);
                contentFromServer = message.getContent();
                clipboard.setPrimaryClip(ClipData.newPlainText("msg",message.getContent()));  // TODO: 5/5/2017 change from settext to clipdata because deprecated
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.KEY_MSG, message);
                intent.putExtra(Constant.KEY_BUNDLE, bundle);
                sendBroadcast(intent);
               if(timeToNotify) notificationWhenCopying(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notificationWhenCopying(Message message) {
        int option = SharedPref.getInstance(getApplicationContext()).getInt(Constant.KEY_NOTIFY_WHEN_COPIED,0);
        switch (option){
            case 1:{
                Toast.makeText(getApplicationContext(),String.format(getString(R.string.notification_copied),message.getContent()),Toast.LENGTH_SHORT).show();
                break;
            }
            case 2:{
                CommonUtils.createNotificationWithMsg(this,String.format(getString(R.string.notification_copied),message.getContent()));
                break;
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }
   // addListenerForSingleValueEvent
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        timeToNotify = true;
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    @Override
    public void onPrimaryClipChanged() {
        if (SharedPref.getInstance(this).getBoolean(Constant.KEY_ON_SERVICE, true)) {
            final String content = clipboard.getText().toString();
            if (mPreviousText.equals(content)) return;
            else {
                if (content != null && !content.isEmpty() && !content.equals(contentFromServer) && !content.equals(AppController.getInstance().getCoppiedText())) {
                    Message msg = new Message();
                    msg.setContent(content);
                    msg.setDate(CommonUtils.getCurrentTime());
                    msg.setId(CommonUtils.getImei(getBaseContext()));
                    mRoot.push().setValue(msg, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            //Toast.makeText(getBaseContext(), content, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                mPreviousText = content;
            }
        }
    }
}
