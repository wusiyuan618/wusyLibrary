package com.wusy.wusylibrary.util.retrofit;


import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public class NetObserver<T> extends DisposableObserver<T> {
    private String message="";
    private final String TAG="NetObserver";
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            message = "网络错误";
        } else if (e instanceof SocketTimeoutException) {
            message = "网络连接超时";
        } else if (e instanceof ConnectException) {
            message = "连接失败";
        } else if (e instanceof IOException) {
            message = "网络错误";
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            message = "证书验证失败";
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //均视为解析错误
            message="数据解析出现错误";
        } else {
            message = "未知错误";
        }
        Log.e(TAG,message);
    }

    @Override
    public void onComplete() {

    }
}
