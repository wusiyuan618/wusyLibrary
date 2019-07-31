package com.wusy.wusyproject;

import com.wusy.wusylibrary.base.BaseApplication;
import com.wusy.wusylibrary.util.LoadingViewUtil;

public class AndroidApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        LoadingViewUtil.getInstance().initLoadingDialog(this);
    }
}
