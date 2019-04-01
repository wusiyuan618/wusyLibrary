package com.wusy.wusylibrary.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.util.CommonUtil;

/**
 * Created by DalaR on 2017/11/20.
 * 自定义标题栏组件。
 */

public class TitleView extends LinearLayout {
    private LinearLayout ll_view,ll_backimg,ll_moreimg,ll_ok;
    private TextView tv_title,tv_ok;
    private ImageView img_back,img_more;
    private CommonUtil commonUtil;
    private Animation outAnimation,enterAnimation;
    private Context mC;
    public TitleView(Context context) {
        this(context,null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mC=context;
        LayoutInflater.from(context).inflate(R.layout.view_titleview, this);
        findView();
        init();
    }

    private void init() {
        commonUtil=CommonUtil.getInstance();
    }

    private void findView(){
        ll_view= (LinearLayout) findViewById(R.id.view_titleview);
        ll_backimg= (LinearLayout) findViewById(R.id.titleview_ll_back);
        ll_moreimg= (LinearLayout) findViewById(R.id.titleview_ll_more);
        tv_title= (TextView) findViewById(R.id.titleview_tv_title);
        img_back= (ImageView) findViewById(R.id.titleview_img_back);
        img_more= (ImageView) findViewById(R.id.titleview_img_more);
        tv_ok=findViewById(R.id.titleview_tv_ok);
        ll_ok=findViewById(R.id.titleview_ll_ok);

    }
    /**
     * 设置标题栏的标题内容
     * @param title 标题内容
     * @return TitleView
     */
    public TitleView setTitle(String title){
        tv_title.setText(title);
        return this;
    }
    /**
     * 是否显示回退按钮。通过传入的Activity自行调用finish方法
     * @param isShow 是否显示按钮
     * @param activity 按钮关联的Activity
     * @return TitleView
     */
    public TitleView showBackButton(boolean isShow, final Activity activity){
        if(isShow) {
            ll_backimg.setVisibility(VISIBLE);
            ll_backimg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }else{
            ll_backimg.setVisibility(INVISIBLE);
        }
        return this;
    }

    public TitleView showBackButtonWithListener(boolean isShow,OnClickListener listener){
        if(isShow) {
            ll_backimg.setVisibility(VISIBLE);
            ll_backimg.setOnClickListener(listener);
        }else{
            ll_backimg.setVisibility(INVISIBLE);
        }
        return this;
    }
    /**
     * 是否显示更多按钮。并且为按钮绑定事件
     * @param isShow 是否显示按钮
     * @param listener 构建该按钮的事件
     * @return TitleView
     */
    public TitleView showMoreButton(boolean isShow,OnClickListener listener){
        if(isShow) {
            ll_moreimg.setVisibility(VISIBLE);
            if (!commonUtil.isNull(listener)) ll_moreimg.setOnClickListener(listener);
        }else{
            ll_moreimg.setVisibility(INVISIBLE);
        }
        return this;
    }
    /**
     * 改变回退按钮的图片样式
     * @param imgResouce 新的图片样式
     * @return TitleView
     */
    public TitleView changeBackImgResource(int imgResouce){
        img_back.setImageResource(imgResouce);
        return this;
    }
    /**
     * 改变更多按钮的图片样式
     * @param imgResouce 新的图片样式
     * @return TitleView
     */
    public TitleView changeMoreImgResource(int imgResouce){
        img_more.setImageResource(imgResouce);
        return this;
    }

    /**
     * 改变标题栏的背景颜色
     * @param color 背景颜色
     * @return TitleView
     */
    public TitleView changeBackground(int color){
        ll_view.setBackgroundColor(color);
        return this;
    }

    /**
     * 添加确定按钮
     * @param text
     * @param isShow
     * @param listener
     * @return
     */
    public TitleView showOKButton(String text,boolean isShow,OnClickListener listener){
        if(isShow) {
            tv_ok.setText(text);
            ll_ok.setVisibility(VISIBLE);
            ll_moreimg.setVisibility(GONE);
            if (!commonUtil.isNull(listener)) ll_ok.setOnClickListener(listener);
        }else{
            ll_ok.setVisibility(GONE);
        }
        return this;
    }
    public TitleView changeOkButtonText(String text){
        tv_ok.setText(text);
        return this;
    }
    /**
     *  显示TitleView的方法，带有向上收缩的动画。目标用于上拉下拉时的自主收缩
     */
    public void showTitleView(){
        if(enterAnimation==null) enterAnimation= AnimationUtils.loadAnimation(mC,R.anim.push_scale_in);
        enterAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                TitleView.this.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(enterAnimation);
    }
    /**
     *  隐藏TitleView的方法，带有向上展开的动画。目标用于上拉下拉时的自主收缩
     */
    public void hideTitleView(){
        if(outAnimation==null) outAnimation=AnimationUtils.loadAnimation(mC,R.anim.push_scale_out);
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TitleView.this.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(outAnimation);
    }
    /**
     * 确认构建标题栏
     */
    public void build(){
        ll_view.setVisibility(View.VISIBLE);
    }
}

