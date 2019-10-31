package com.wusy.wusyproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.wusy.wusylibrary.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class SecondActivity : BaseActivity(){
    override fun findView() {
    }

    override fun init() {
//       titleView.setTitle("111").showBackButton(true,this).build()
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_home
    }


    override fun onResume() {
        super.onResume()

    }
}