package com.wusy.wusyproject.service;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.wusy.wusylibrary.base.BaseActivity;
import com.wusy.wusyproject.R;

import java.util.ArrayList;

public class ServiceActivity extends BaseActivity {
    private GuardBroadcastReceiver myBrodcast;
    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void findView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void init() {
        myBrodcast= new GuardBroadcastReceiver();
        ArrayList<String> list=new ArrayList<>();
        list.add(Intent.ACTION_TIME_TICK);
//        list.add("android.intent.action.BOOT_COMPLETED");
        addBroadcastAction(list,myBrodcast);
        startService(new Intent(this, GuardService.class));
//        startService(new Intent(this, TestService.class));
    }
}
