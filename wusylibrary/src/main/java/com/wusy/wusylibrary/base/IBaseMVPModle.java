package com.wusy.wusylibrary.base;

import java.util.List;

/**
 * Created by XIAO RONG on 2018/3/26.
 */

public interface IBaseMVPModle<T> {
    /**
     * 获取数据集合
     * @return
     */
    List<T> getList();
    /**
     * 请求数据列表
     */
    void requestData(int source);
}
