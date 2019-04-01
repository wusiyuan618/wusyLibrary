package com.wusy.wusyproject.retrofit;

/**
 * Created by XIAO RONG on 2019/3/29.
 * 做数据请求时，我们更关系获取到的param，对于状态，只需要统一判断管理就好。
 * 所以封装网络请求基础数据类
 */

public class BaseResponse<T> {
    public String result;
    public String str;
    public T param;
    public boolean isSuccess(){
//        return str.equals("202");
        return true;
    }
}
