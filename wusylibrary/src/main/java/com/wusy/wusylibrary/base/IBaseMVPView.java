package com.wusy.wusylibrary.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

/**
 * Created by XIAO RONG on 2018/3/23.
 */

public interface IBaseMVPView {
    /***
     * 获取Context
     * @return Context
     */
    Context getmContext();

    /***
     * 显示Progress
     */
    void showProgress();

    /***
     * 关闭Progress
     */
    void closeProgress();
    /**
     * 更新List，展现数据
     */
    void showList(BaseAdapter adapter);
    void showList(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter);
}
