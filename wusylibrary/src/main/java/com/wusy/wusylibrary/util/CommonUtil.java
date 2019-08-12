package com.wusy.wusylibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by DalaR on 2017/11/20.
 */

public class CommonUtil {
    private static CommonUtil util;
    private CommonUtil(){

    }

    public synchronized static CommonUtil getInstance(){
        if(util==null){
            util=new CommonUtil();
        }
        return util;
    }

    /**
     * 创建apk文件 createFile(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
     *
     * @param name
     *            void
     * @exception
     * @since 1.0.0
     */
    public  void createFile(String name) {

        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState())) {
            File updateDir = new File(Environment.getExternalStorageDirectory()
                    + "/" + "workdb/");
            File updateFile = new File(updateDir + "/" + name);
            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 判断传入Object是否为空
     * @param obj
     * @return
     */
    public boolean isNull(Object obj){
        if(obj==null||obj.equals("")){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 通过包名获取类名
     * @param packageName
     * @return
     */
    public String getClassName(String packageName){
        String [] names = packageName.split("\\.");
        String name=names[names.length-1];
        return name;
    }
    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
