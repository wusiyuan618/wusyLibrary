package com.wusy.wusylibrary.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.util.ActivityAnimUtil;
import com.wusy.wusylibrary.util.ActivityManager;
import com.wusy.wusylibrary.util.CommonUtil;
import com.wusy.wusylibrary.util.ImageLoaderUtil;
import com.wusy.wusylibrary.util.LoadingViewUtil;
import com.wusy.wusylibrary.util.MTAUtil;
import com.wusy.wusylibrary.util.StatusBarUtil;

import java.util.ArrayList;


/**
 * Created by DalaR on 2017/10/30.
 * 这是一个Activity的基类，适合所有Activity继承。封装了一些通用方法，同时对Activity做了一些通用配置
 * 配置：
 * 1、去掉了标题栏
 * 2、利用ActivityManager统一管理所有Activity
 * 3、提供Activity的TAG，常用语打印Log信息的筛选。TAG=Activity的类名
 * 4、重写了finish（）方法。为其添加动画
 * 方法：
 * 1、打印Log的方法showLogInfo（）和showLogError（）
 * 2、打印Toast的方法showToast（）
 * 3、添加广播的方法addBroadcastAction（）
 * 4、isChangeStatusBar(boolean isChange)
 * 是否开启状态栏管理的方法。6.0以上手机对状态栏进行管理。兼容6.0以下手机正常样式。
 * 需要在子Activity的首层Viewgroup添加id---layout_total. 2018/5/23
 */

public abstract class BaseActivity extends AppCompatActivity{
    public String TAG="";
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    private LoadingViewUtil loadingViewUtil;
    private Dialog loadDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseInit();
        findView();
        init();
    }
    private void baseInit(){
        //         去掉标题栏
        getSupportActionBar().hide();
        setContentView(getContentViewId());
        //固定竖屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CommonUtil commonUtil = CommonUtil.getInstance();
        loadingViewUtil=LoadingViewUtil.getInstance();
        loadDialog=loadingViewUtil.createLoadingDialog(this,"");
        //为TAG赋值。值为类名
        TAG= commonUtil.getClassName(getComponentName().getClassName());
        //将Activity添加进管理器中
        ActivityManager.getInstance().addActivity(this);
        //是否开启状态栏
        isChangeStatusBar(true);
        //注册ButterKinef

    }

    private void isChangeStatusBar(boolean isChange){
        if (isChange){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StatusBarUtil.transparencyBar(this);
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                try {
                    ViewGroup layout = findViewById(R.id.layout_total);
                    layout.setClipToPadding(true);
                    layout.setFitsSystemWindows(true);
                    layout.setBackgroundColor(getResources().getColor(R.color.titleViewBackgroundColor));
                }catch (Exception e){
                    Logger.e("该Activity没有为首层Layout添加id--layout_total，无法管理状态栏。");
                }
            }
        }
    }

    public abstract int getContentViewId();
    public abstract void findView();
    public abstract void init();

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d(getComponentName().getClassName()+"执行onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(getComponentName().getClassName()+"执行onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(getComponentName().getClassName()+"执行onResume");
        MTAUtil.getInstance().MATPageStatistics(this,true,TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(getComponentName().getClassName()+"执行onPause");
        MTAUtil.getInstance().MATPageStatistics(this,false,TAG);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(getComponentName().getClassName()+"执行onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(getComponentName().getClassName()+"执行onDestroy");
        ActivityManager.getInstance().removeActivity(this);
        if(broadcastReceiver!=null) unregisterReceiver(broadcastReceiver);
    }

    /**
     * Log打印info信息
     * @param info
     */
    public void showLogInfo(String info){
        Logger.i(info);
    }

    /**
     * Log打印Eoor信息
     * @param error
     */
    public void showLogError(String error){
        try {
            Logger.e(error);
        }catch (Exception ignored){

        }
    }
    /**
     * 打印Toast信息
     */
    public void showToast(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }
    /**
     * 重写的finish方法。为它添加了动画
     */
    @Override
    public void finish() {
        super.finish();
        ActivityAnimUtil.getInstance().starFinish(this);
    }

    /**
     * 处理广播时，添加Action动作
     * 注册广播的时候一定记得在onDestory中销毁
     * @param actions
     */
    public void addBroadcastAction(ArrayList<String> actions,BroadcastReceiver broadcastReceiver){
        //实例化广播需要的InntentFilter和MyBroadcastReceiver
        this.broadcastReceiver=broadcastReceiver;
        if(intentFilter==null) intentFilter=new IntentFilter();
        for(int i=0;i<actions.size();i++){
            intentFilter.addAction(actions.get(i));
        }
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void showLoadImage(){
        loadingViewUtil.showDialog(loadDialog);
    }
    public void hideLoadImage(){
        loadingViewUtil.dismissDialog(loadDialog);
    }
    public void setLoadDialogMsg(String msg){
        loadingViewUtil.setLoadMsg(loadDialog,msg);
    }
}
