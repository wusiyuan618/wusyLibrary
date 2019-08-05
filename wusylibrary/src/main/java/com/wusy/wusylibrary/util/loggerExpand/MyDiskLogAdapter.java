package com.wusy.wusylibrary.util.loggerExpand;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.LogAdapter;

public class MyDiskLogAdapter implements LogAdapter {

    @NonNull
    private final MyFormatStrateg formatStrategy;


    public MyDiskLogAdapter(){
        formatStrategy = MyFormatStrateg.newBuilder().build();
    }

    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        return true;
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        formatStrategy.log(priority, tag, message);
    }
}
