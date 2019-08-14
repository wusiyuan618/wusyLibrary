package com.wusy.wusylibrary.view.moduleComponents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.base.BaseRecyclerAdapter;
import com.wusy.wusylibrary.view.FullyGridLayoutManager;
import com.wusy.wusylibrary.view.FullyLinearLayoutManager;

import java.util.List;

/**
 * Created by DalaR on 2017/11/24.
 * 自定义模块组件，多用于门户页面的展示。
 * 提供专用的Adapter、Bean、Holder，可快速搭建门户页面
 */

public class ModuleView extends LinearLayout {
    TextView tv_title;
    ImageView img_title,img_right;
    RecyclerView recyclerView;
    LinearLayout ll_right,ll_title;
    ModuleViewAdapter adapter;
    public ModuleView(Context context) {
        this(context,null);
    }

    public ModuleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ModuleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_moduleview,this);
        tv_title= (TextView) findViewById(R.id.view_moduleview_tv_title);
        img_title= (ImageView) findViewById(R.id.view_moduleview_img_title);
        img_right= (ImageView) findViewById(R.id.view_moduleview_img_rightimg);
        recyclerView= (RecyclerView) findViewById(R.id.view_moduleview_recyclerView);
        ll_right= (LinearLayout) findViewById(R.id.view_moduleview_ll_rightimg);
        ll_title= (LinearLayout) findViewById(R.id.view_moduleview_ll_title);
    }
    public ModuleView isShowTitle(boolean isShow){
        if(isShow) ll_title.setVisibility(View.VISIBLE);
        else ll_title.setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置模块名
     * @param title 模块名
     * @param titleColor 字体颜色
     * @return ModuleView
     */
    public ModuleView setTitle(String title,int titleColor){
        tv_title.setText(title);
        if(titleColor!=0){
            tv_title.setTextColor(titleColor);
        }
        return this;
    }

    /**
     * 是否显示右侧拓展按钮
     * @param isShow 是否显示 默认不显示
     * @param listener 按钮的点击事件
     * @return ModuleView
     */
    public ModuleView showRightImg(boolean isShow,OnClickListener listener){
        if(isShow){
            ll_right.setVisibility(View.VISIBLE);
            if(listener!=null){
                ll_right.setOnClickListener(listener);
            }
        }
        return this;
    }

    /**
     *  是否显示右侧拓展按钮
     * @param isShow 是否显示 默认不显示
     * @param listener 按钮的点击事件
     * @param imgResource 自定义按钮的图标
     * @return ModuleView
     */
    public ModuleView showRightImg(boolean isShow,OnClickListener listener,int imgResource){
        showRightImg(isShow,listener);
        img_right.setImageResource(imgResource);
        return this;
    }

    /**
     * 更换左侧效果竖线的颜色。 默认是粉红色
     * @param color 颜色值
     * @return ModuleView
     */
    public ModuleView changeTitleImgColor(int color){
        img_title.setBackgroundColor(color);
        return this;
    }

    /**
     * 初始化模块的内容。使用的是recyclerView。通过ModuleViewBean中的LayoutNum参数控制显示线性布局或者网格布局
     *
     * @param context
     * @param list 内容数据 默认只会展示前面4条数据。其他数据在更多里展示。
     * @return ModuleView
     */
    public ModuleView showRecycelerView(Context context, List<ModuleViewBean> list){
        adapter=new ModuleViewAdapter(context);
        adapter.setList(list);
        if(list.size()==0)return this;
        recyclerView.setLayoutManager(new FullyGridLayoutManager(context,4));
        recyclerView.setAdapter(adapter);
        return this;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
