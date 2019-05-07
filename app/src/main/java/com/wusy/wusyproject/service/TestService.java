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
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.wusy.wusyproject.R;



public class TestService extends Service {
    private GuardBroadcastReceiver guardBroadcastReceiver;
    private IntentFilter intentFilter;
    private final String MESSAGE_CHANNELID="message";
    private final String MESSAGE_CHANNELNAME="督办消息";
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("rxr","TestService运行中");
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
        createNotificationChannel(MESSAGE_CHANNELID,MESSAGE_CHANNELNAME,NotificationManager.IMPORTANCE_MAX,this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("wsy","GuardService onStartCommand");
        handler.sendEmptyMessageDelayed(0,1000);
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext(),MESSAGE_CHANNELID); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, ServiceActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("要显示的内容") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);
        return super.onStartCommand(intent, flags, startId);
    }
    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel(String channelId, String channelName, int importance, Context context) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }else{
            Log.e("wsy","Android版本低于26，无需创建通知渠道");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("wsy","GuardService onDestroy");
        stopForeground(true);
        if(guardBroadcastReceiver!=null)unregisterReceiver(guardBroadcastReceiver);
    }
}
