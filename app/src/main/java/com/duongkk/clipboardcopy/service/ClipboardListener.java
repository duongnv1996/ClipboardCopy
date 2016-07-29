package com.duongkk.clipboardcopy.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by MyPC on 7/29/2016.
 */
public class ClipboardListener extends Service {
    ClipboardManager clipboard;
    @Override
    public void onCreate() {
        super.onCreate();
        clipboard  = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String content = clipboard.getText().toString();
                if(content!=null && !content.isEmpty())
                Toast.makeText(getBaseContext(),content,Toast.LENGTH_LONG).show();

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
}
