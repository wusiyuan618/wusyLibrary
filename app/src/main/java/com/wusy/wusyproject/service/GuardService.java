package com.wusy.wusyproject.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;



public class GuardService extends Service {
    private GuardBroadcastReceiver guardBroadcastReceiver;
    private IntentFilter intentFilter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("wsy","GuardService运行中");
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("wsy","GuardService onCreate");
        guardBroadcastReceiver=new GuardBroadcastReceiver();
        if(intentFilter==null) intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(guardBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("wsy", "GuardService onStartCommand");
        handler.sendEmptyMessageDelayed(0, 1000);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("wsy","GuardService onDestroy");
//        stopForeground(true);
        if(guardBroadcastReceiver!=null)unregisterReceiver(guardBroadcastReceiver);
    }
}
