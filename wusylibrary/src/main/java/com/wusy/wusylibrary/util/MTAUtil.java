package com.wusy.wusylibrary.util;

import android.content.Context;

import com.tencent.stat.StatService;

import java.util.Properties;

/**
 * Created by XIAO RONG on 2018/5/16.
 */

public class MTAUtil {
    private static MTAUtil matUtil;
    private MTAUtil(){

    }
    public static synchronized MTAUtil getInstance(){
        if(matUtil==null) matUtil=new MTAUtil();
        return matUtil;
    }
    /**
     * 埋点方法
     */
    public void MATClickStatistics(Context context, String id, Properties properties){
        StatService.trackCustomKVEvent(context, id, properties);
    }
    /**
     * 统计某个页面的访问量
     */
    public void MATPageStatistics(Context context,boolean begin){
        if(begin) StatService.onResume(context);
        else StatService.onPause(context);
    }
    public void MATPageStatistics(Context context,boolean begin,String activityName){
        if(begin) StatService.trackBeginPage(context,activityName);
        else StatService.trackEndPage(context,activityName);
    }
}
