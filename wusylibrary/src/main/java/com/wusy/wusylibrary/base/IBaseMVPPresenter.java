package com.wusy.wusylibrary.base;

/**
 * Created by XIAO RONG on 2018/3/26.
 */

public interface IBaseMVPPresenter {
    /**
     * 提供View
     */
    IBaseMVPView getMVPView();
    /**
     * 提供Module
     */
    IBaseMVPModel getModel();
}
