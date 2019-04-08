package com.wusy.wusyproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jkyeo.basicparamsinterceptor.BasicParamsInterceptor;
import com.wusy.wusylibrary.view.NumberKeyBoxView;
import com.wusy.wusylibrary.view.PwdIndicator;
import com.wusy.wusyproject.retrofit.LoginBean;
import com.wusy.wusyproject.retrofit.PersonAPI;
import com.wusy.wusyproject.retrofit.PersonAPILoader;
import com.wusy.wusyproject.retrofit.RetrofitServiceManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{
    String TAG = "wsy";
//    TextView textView;
    PwdIndicator pwdIndicator;
    NumberKeyBoxView numberKeyBoxView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        pwdIndicator=findViewById(R.id.pwdindicator);
//        pwdIndicator.initIndicator(6);
//        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pwdIndicator.add();
//            }
//        });
//        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pwdIndicator.remove();
//            }
//        });
        numberKeyBoxView=findViewById(R.id.numberkeyboxview);
        numberKeyBoxView.setNumberKeyBoxViewClick(new NumberKeyBoxView.NumberKeyBoxViewClick() {
            @Override
            public void click(String value) {
                Log.i(TAG,value);
            }
        });
//        textView = findViewById(R.id.textView);
//        simpleRX_Retrofit();
    }

    /**
     * 基础的Retrofit使用
     */
    private void retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://222.211.90.120:6071/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        PersonAPI personAPI = retrofit.create(PersonAPI.class);
        Call<LoginBean> call = personAPI.login("luoy", "123456");
        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean loginBean = response.body();
                Log.i("wsy", loginBean.getParam().toString());
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                Log.i("wsy", "error", t);
            }
        });
    }

    /**
     * RX+Retrofit结合使用
     */
    private void RX_Retrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS);//连接 超时时间
        builder.writeTimeout(20, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(20, TimeUnit.SECONDS);//读操作 超时时间
        builder.retryOnConnectionFailure(true);//错误重连

// 添加公共参数拦截器
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                .addHeaderParam("token", "1")//添加公共参数
                .addParam("uid", "2")
                .addQueryParam("api_version", "3")
                .build();

        builder.addInterceptor(basicParamsInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://222.211.90.120:6071/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();
        PersonAPI personAPI = retrofit.create(PersonAPI.class);
        personAPI.loginRX("luoy", "123456")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new DisposableObserver<LoginBean>() {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        Log.i(TAG, loginBean.getParam().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "loginRX报错", e);

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "loginRX传递完成");
                    }
                });
    }
    /**
     * 引入封装类，简化retrofit+rx的使用
     */
    private void simpleRX_Retrofit(){
        PersonAPI personAPI=RetrofitServiceManager
                .getInstance()
                .create(PersonAPI.class);
        PersonAPILoader personAPILoader=new PersonAPILoader();
        personAPILoader.login("luoy","123456")
                .doOnNext(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean loginBean) throws Exception {

                    }
                })
                .subscribe(new DisposableObserver<LoginBean>() {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        Log.i(TAG, loginBean.getParam().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "loginRX报错", e);

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "loginRX传递完成");
                    }
                });
    }
}
