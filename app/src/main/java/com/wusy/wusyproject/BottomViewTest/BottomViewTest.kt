package com.wusy.wusyproject.BottomViewTest

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import com.wusy.wusylibrary.base.BaseActivity
import com.wusy.wusylibrary.view.bottomSelect.BottomSelectView
import com.wusy.wusyproject.R
import kotlinx.android.synthetic.main.activity_bottomview.*

class BottomViewTest : BaseActivity() {

    override fun getContentViewId(): Int {
        return R.layout.activity_bottomview
    }

    override fun findView() {

    }

    override fun init() {
            bottomSelectView.createLayout(this,BottomViewDataUtil.getInstance().bottomSelectData,supportFragmentManager,R.id.fragmentBox)
    }


    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
    }
}
