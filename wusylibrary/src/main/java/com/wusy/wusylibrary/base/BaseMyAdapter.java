package com.wusy.wusylibrary.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by XIAO RONG on 2018/3/26.
 */

public abstract class BaseMyAdapter<T> extends BaseAdapter {
    private Context mC;
    private List<T> list;
    public BaseMyAdapter(Context mC) {
        this.mC = mC;
    }

    public BaseMyAdapter(Context mC, List<T> list) {
        this.mC = mC;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position,convertView,parent);
    }
    public abstract View initView(int position, View convertView, ViewGroup parent);

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Context getmC() {
        return mC;
    }
}
