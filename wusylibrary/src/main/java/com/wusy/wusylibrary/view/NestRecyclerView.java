package com.wusy.wusylibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by XIAO RONG on 2018/7/18.
 * 直接测量RecyclerView的最大长度，用于嵌套ScrollView
 */

public class NestRecyclerView extends RecyclerView {
    public NestRecyclerView(Context context) {
        super(context);
        setLayoutManager(new LinearLayoutManager(context));
        setNestedScrollingEnabled(false);
    }

    public NestRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        setNestedScrollingEnabled(false);
    }

    public NestRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context));
        setNestedScrollingEnabled(false);
    }
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int newHeightSpec=MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthSpec, newHeightSpec);
    }
}
