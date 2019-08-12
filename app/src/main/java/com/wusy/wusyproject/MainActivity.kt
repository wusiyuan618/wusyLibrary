package com.wusy.wusyproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        tempControlView!!.setTemp(16, 31, 18)
        tempControlView.setOnTempChangedListener {
            Log.i("wsy","当前温度：$it")
        }
    }
}
