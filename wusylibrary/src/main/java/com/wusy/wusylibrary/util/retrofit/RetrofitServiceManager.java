package com.wusy.wusylibrary.util.retrofit;

import com.jkyeo.basicparamsinterceptor.BasicParamsInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XIAO RONG on 2019/3/29.
 * retrofitServiceManager管理的是ServiceAPI的创建，对外公布create方法，返回ServiceApi实例
 * 同时对OkHttpClient进行了封装和引入，可以对请求的公共参数，请求时间等进行添加和限制
 */

public class RetrofitServiceManager {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;//读写超时
    private static final String BASE_URL="http://222.211.90.120:6071/";//根地址
    private static  RetrofitServiceManager retrofitServiceManager;
    //日志显示级别
    private Retrofit mRetrofit;
    /**
     * 获取RetrofitServiceManager
     * @return
     */
    public synchronized static RetrofitServiceManager getInstance(){
        if(retrofitServiceManager==null) return new RetrofitServiceManager();
        return retrofitServiceManager;
    }
    private RetrofitServiceManager(){
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);//读操作超时时间
        builder.retryOnConnectionFailure(true);

        // 添加公共参数拦截器
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                .addHeaderParam("paltform","android")
                .addHeaderParam("userToken","1234343434dfdfd3434")
                .addHeaderParam("userId","123445")
                .build();
        //添加日志打印拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);//设置日志显示级别
        builder.addInterceptor(basicParamsInterceptor);
        builder.addInterceptor(loggingInterceptor);
        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }



    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }
}