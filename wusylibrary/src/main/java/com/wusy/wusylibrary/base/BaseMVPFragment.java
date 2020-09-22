package com.wusy.wusylibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by XIAO RONG on 2018/3/26.
 */

public abstract class BaseMVPFragment  <V extends IBaseMVPView, T extends BaseMVPPresenter<V>>
        extends BaseFragment {
    public T mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView((V)this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null){
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    /**
     * 创建Presenter对象
     * @return Presenter对象
     */
    protected abstract T createPresenter();

    public T getmPresenter() {
        if(mPresenter==null) createPresenter();
        return mPresenter;
    }
}
