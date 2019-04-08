package com.wusy.wusylibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wusy.wusylibrary.R;

import java.util.ArrayList;
import java.util.List;

public class PwdIndicator extends LinearLayout {
    private LinearLayout ll_view;
    private Context mC;
    private int currentIndex=0;//当前被点亮的个数。
    private int count=0;//总计数
    private List<ImageView> imageViews;
    public PwdIndicator(Context context) {
        this(context,null);
    }

    public PwdIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PwdIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    @SuppressLint("NewApi")
    public PwdIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mC=context;
        LayoutInflater.from(context).inflate(R.layout.view_pwdindicator, this);
        findView();
        init();
    }

    private void findView() {
        ll_view=findViewById(R.id.view_pwdindicator);
    }
    private void init(){
        imageViews=new ArrayList<>();
    }
    public void initIndicator(int index){
        if(imageViews.size()>0){
//            clear();
            return;
        }
        this.count=index;
        for(int i=0;i<index;i++){
            ImageView imageView = new ImageView(mC);
            LayoutParams params_imageView = new LayoutParams(20, 20);
            params_imageView.leftMargin = 15;
            params_imageView.rightMargin=15;
            imageView.setImageResource(R.mipmap.indicate_normal);
            ll_view.addView(imageView, params_imageView);
            imageViews.add(imageView);
        }
    }
    public boolean add(){
        if(currentIndex!=count){
            imageViews.get(currentIndex).setImageResource(R.mipmap.indicator_select);
            currentIndex++;
            return true;
        }else return false;
    }
    public boolean remove(){
        if(currentIndex!=0){
            currentIndex--;
            imageViews.get(currentIndex).setImageResource(R.mipmap.indicate_normal);
            return true;
        }else return false;
    }
    public boolean isEnd(){
        Log.i("wsy",currentIndex+"---"+count);
        return true;
    }
    public void clear(){
        currentIndex=0;
        for (ImageView imageView:imageViews){
            imageView.setImageResource(R.mipmap.indicate_normal);
        }
    }
}
