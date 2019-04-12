package com.wusy.wusylibrary.util.retrofit;
import io.reactivex.Observable;

/**
 * Created by XIAO RONG on 2019/3/29.
 * 管理了ServiceAPI的接口，避免每次请求都需要去create一次
 * 继承统一的ObjectLoader，统一对RX的Observale进行配置。
 * 在这里，只需要对observable做个性化的数据操作，统一配置放在ObjectLoader中
 */

public class APILoader extends ObjectLoader {
    private PersonAPI mPersonAPI;
    public APILoader(){
        mPersonAPI=RetrofitServiceManager.getInstance().create(PersonAPI.class);
    }
    public Observable<LoginBean> login(String name,String pwd){
        return observe(
                mPersonAPI.loginRX(name,pwd)
        );
    }
}