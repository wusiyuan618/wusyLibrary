package com.wusy.wusylibrary.util;

import android.app.Activity;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DalaR on 2017/10/30.
 */

public class ActivityManager {
    public ArrayList<Activity> list;
    private static ActivityManager activityManager;
    private ActivityManager(){
        list=new ArrayList<>();
    }
    public synchronized static ActivityManager getInstance(){
        if(activityManager==null){
            activityManager=new ActivityManager();
        }
        return activityManager;
    }
    public void addActivity(Activity activity){
        list.add(activity);
    }
    public void removeActivity(Activity activity){
        list.remove(activity);
    }
    public void finishAllActivity(){
        Logger.i("当前Activity数----"+list.size());
        try {
            for (Activity ac:list) {
                if (ac != null) ac.finish();
            }
        }catch (Exception e){
            Logger.e(e,"error");
        }
    }
}
