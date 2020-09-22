package com.wusy.wusylibrary.base;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by XIAO RONG on 2018/3/23.
 */

public abstract class BaseMVPActivity <V extends IBaseMVPView, T extends BaseMVPPresenter<V>>
        extends BaseActivity {
    public T mPresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView((V)this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null){
            mPresenter.detachView();
        }
        super.onDestroy();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter == null){
            mPresenter = createPresenter();
        }
    }

    /**
     * 创建Presenter对象
     * @return Presenter对象
     */
    protected abstract T createPresenter();

    public T getmPresenter() {
        return mPresenter;
    }
}
