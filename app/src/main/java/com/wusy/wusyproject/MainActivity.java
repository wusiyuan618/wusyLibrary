package com.wusy.wusyproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wusy.wusylibrary.util.retrofit.RxRtTestUtil;


public class MainActivity extends AppCompatActivity{
    String TAG = "wsy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new RxRtTestUtil().simpleRX_Retrofit();
    }
}
