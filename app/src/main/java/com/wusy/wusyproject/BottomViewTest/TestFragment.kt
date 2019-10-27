package com.wusy.wusyproject.BottomViewTest

import android.view.View
import android.widget.TextView
import com.wusy.wusylibrary.base.BaseFragment
import com.wusy.wusyproject.R

class TestFragment :BaseFragment(){
    override fun getContentViewId(): Int {
        return R.layout.fragment_test
    }

    override fun findView(view: View?) {
        view!!.findViewById<TextView>(R.id.tv).text=arguments!!.getString("text")
    }

    override fun init() {
    }

}
