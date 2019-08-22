package com.wusy.wusyproject

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.wusy.wusylibrary.base.BaseActivity
import com.wusy.wusylibrary.base.BaseRecyclerAdapter
import com.wusy.wusylibrary.util.MaterialDialogsUtil
import com.wusy.wusylibrary.view.moduleComponents.ModuleViewAdapter
import com.wusy.wusylibrary.view.moduleComponents.ModuleViewBean
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity :BaseActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_home
    }

    override fun findView() {
    }

    override fun init() {
        titleView.setTitle("111")
                .showOKButton("提交",true, View.OnClickListener {
                    showToast("1111111")
                }).build()

    }

}
