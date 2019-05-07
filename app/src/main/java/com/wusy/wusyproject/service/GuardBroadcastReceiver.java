package com.wusy.wusyproject.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class GuardBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case Intent.ACTION_TIME_TICK:
                Log.i("wsy","收到系统广播Intent.ACTION_TIME_TICK");
                Log.i("wsy","服务是否存在---"+isServiceExisted(context,GuardService.class.getName()));
                if(!isServiceExisted(context,GuardService.class.getName())){
                    context.startService(new Intent(context, GuardService.class));
                }
                break;
            case "android.intent.action.BOOT_COMPLETED":
                    Log.i("wsy","收到系统广播BOOT_COMPLETED");
                Toast.makeText(context,"收到重启广播",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context,ServiceActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                break;
        }
    }
    public boolean isServiceExisted(Context context,String className) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(Integer.MAX_VALUE);
        int myUid = android.os.Process.myUid();
        for (ActivityManager.RunningServiceInfo runningServiceInfo : serviceList) {
            if (runningServiceInfo.uid == myUid && runningServiceInfo.service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
