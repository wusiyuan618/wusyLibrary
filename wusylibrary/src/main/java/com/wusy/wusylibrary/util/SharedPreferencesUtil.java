package com.wusy.wusylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DalaR on 2017/11/2.
 */

public class SharedPreferencesUtil {
    private static final String FILE_NAME = "SharePreferencesFile";
    private static SharedPreferencesUtil util;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SharedPreferencesUtil(Context context){
        sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public synchronized static SharedPreferencesUtil getInstance(Context context){
        if(util==null){
            util=new SharedPreferencesUtil(context);
        }
        return util;
    }

    /**
     * 保存数据到文件
     * @param key
     * @param data
     */
    public void saveData(String key, Object data) {
        if(data==null) return;
        String type = data.getClass().getSimpleName();
        //可写为switch-case比较好if-else分支过多影响性能
        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    /**
     * 从文件中读取数据
     ** @param key
     * @param defValue  不能为空  取出值的类型
     * @return
     */
    public Object getData( String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sp.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defValue);
        }

        return null;
    }
}
