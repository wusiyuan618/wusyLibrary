package com.wusy.wusylibrary.util.loggerExpand;

import android.os.Environment;

import java.io.File;

public class LoggerSetting {
    public static String saveDir= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "logger";
    public static String fileName="LogsByWusyLib";
    public static int fileSize=1024*1024;
    public static void changeSavePackage(String packageName){
        saveDir=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + packageName;
    }
}
