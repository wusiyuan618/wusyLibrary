package com.wusy.wusylibrary.view.moduleComponents;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by DalaR on 2017/11/27.
 */

public class ModuleViewBean implements Serializable {
    /**
     * 模块Item的图片
     */
    private int ImageResource;
    /**
     * 模块Item的图片地址
     */
    private String imageUrl;
    /**
     * 模块Item的内容 多为标题
     */
    private String title;
    /**
     * Item关联的详情展示Activity
     */
    private Class c;
    /**
     * 为每一个Item添加独特的点击事件
     */
    private ModuleViewAdapter.OnModuleViewItemClickListener onModuleViewItemClickListener;
    /**
     * 数据标识
     */
    private Object data;
    /**
     * 数据传参
     */
    private Bundle bundle;
    public ModuleViewBean() {
    }

    public ModuleViewBean(int imageResource, String title,Class c ) {
        this.ImageResource = imageResource;
        this.title = title;
        this.c=c;
    }
    public ModuleViewBean(int imageResource, String title,Class c,Bundle bundle ) {
        this.ImageResource = imageResource;
        this.title = title;
        this.c=c;
        this.bundle=bundle;
    }
    public ModuleViewBean(int imageResource, String title, Class c,Bundle bundle,ModuleViewAdapter.OnModuleViewItemClickListener onModuleViewItemClickListener) {
        this.ImageResource = imageResource;
        this.title = title;
        this.c=c;
        this.bundle=bundle;
        this.onModuleViewItemClickListener=onModuleViewItemClickListener;
    }
    public ModuleViewBean(String imageUrl, String title, Class c) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.c=c;
    }
    public ModuleViewBean(String imageUrl, String title,  Class c,Bundle bundle,ModuleViewAdapter.OnModuleViewItemClickListener onModuleViewItemClickListener) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.c=c;
        this.bundle=bundle;
        this.onModuleViewItemClickListener=onModuleViewItemClickListener;
    }
    public ModuleViewBean(String imageUrl, String title, Class c,Bundle bundle) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.c=c;
        this.bundle=bundle;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public void setImageResource(int imageResource) {
        ImageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getC() {
        return c;
    }

    public void setC(Class c) {
        this.c = c;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public ModuleViewAdapter.OnModuleViewItemClickListener getOnModuleViewItemClickListener() {
        return onModuleViewItemClickListener;
    }

    public void setOnModuleViewItemClickListener(ModuleViewAdapter.OnModuleViewItemClickListener onModuleViewItemClickListener) {
        this.onModuleViewItemClickListener = onModuleViewItemClickListener;
    }

    public Object getIndex() {
        return data;
    }

    public void setIndex(Object data) {
        this.data = data;
    }
}
