package com.wusy.wusylibrary.util;

import android.app.Activity;

import com.wusy.wusylibrary.R;

import java.util.Random;

public class ActivityAnimUtil {
	private static ActivityAnimUtil util;

	private ActivityAnimUtil(){
		Random rd = new Random();
	}

	public synchronized static ActivityAnimUtil getInstance(){
		if(util==null){
			util=new ActivityAnimUtil();
		}
		return util;
	}
	public void starFinish(Activity activity){
		activity.overridePendingTransition(0, R.anim.activity_finish);
	}

	public void startActivityWithAnim(){

	}

}
