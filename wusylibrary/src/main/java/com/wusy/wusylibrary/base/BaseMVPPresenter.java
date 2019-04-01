package com.wusy.wusylibrary.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by XIAO RONG on 2018/3/23.
 */

public class BaseMVPPresenter <T extends IBaseMVPView>{
    /**
     * View接口类型的弱引用
     */
    private Reference<T> mViewRef;

    /**
     * 建立关联
     */
    void attachView(T view){
        mViewRef = new WeakReference<>(view);
        if(isViewAttached()) {
            T mMvpView = getView();
        }
    }

    /**
     * 获取View
     * @return View
     */
    public T getView(){
        if (mViewRef != null) {
            return mViewRef.get();
        }
        return null;
    }

    /**
     * UI展示相关的操作需要判断一下 Activity 是否已经 finish.
     * <p>
     * todo : 只有当 isActivityAlive 返回true时才可以执行与Activity相关的操作,
     * 比如 弹出Dialog、Window、跳转Activity等操作.
     *
     * @return boolean
     */
    public boolean isViewAttached(){
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 解除关联
     */
    public void detachView(){
        if( mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
