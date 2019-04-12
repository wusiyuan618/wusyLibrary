package com.wusy.wusylibrary.util.retrofit;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by XIAO RONG on 2019/3/27.
 */

public interface PersonAPI {
    @GET("td_web/td/login/")
    Call<LoginBean> login(@Query("name") String name,@Query("pwd") String pwd);
    @GET("td_web/td/login/")
    Observable<LoginBean> loginRX(@Query("name") String name, @Query("pwd") String pwd);
}