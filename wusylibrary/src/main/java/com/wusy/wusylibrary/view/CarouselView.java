package com.wusy.wusylibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.util.ImageLoaderUtil;
import com.wusy.wusylibrary.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by XIAO RONG on 2018/8/8.
 */

public class CarouselView extends LinearLayout implements ViewPager.OnPageChangeListener {
    public static final int ANIM_NORMAL=0;
    public static final int ANIM_ALPHA_PAGETRANS=1;
    public static final int ANIM_SCALEMAGIC=2;
    public static final int ANIM_ROTATEMAGIC=3;
    public static final int ANIM_LEFTLEAVE=4;
    public static final int ANIM_SCALERIGHTLEAVE=5;
    private FrameLayout frameLayout;
    private LinearLayout ll_bottomview;
    private String TAG="CarouselView";
    private Context mC;
    private ViewPager viewPager;
    private LinearLayout ll_point;
    private TextView tv_desc;
    private ArrayList<ImageView> imageViews; //存放图片的集合
    private ArrayList<CarouselBean> beans;
    private int lastPosition;
    private boolean isRunning = true;
    private int intervalsTime=5000;

    private OnImageClickListener m_onImageClickListener;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    break;
            }
        }
    };
    public CarouselView(Context context) {
        this(context,null);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_carousel, this);
        this.mC=context;
        findView();
    }
    private void findView(){
        viewPager=findViewById(R.id.view_carousel_viewpager);
        tv_desc=findViewById(R.id.view_carousel_tv_desc);
        ll_point=findViewById(R.id.view_carousel_ll_point);
        frameLayout=findViewById(R.id.view_carousel_framelayout);
        ll_bottomview=findViewById(R.id.view_carousel_ll_bottomview);
    }

    public void init(ArrayList<CarouselBean> beans,int anim){
        this.beans=beans;
        initView();
        initAnim(anim);
        initAdapter();
        startCarousel();
    }
    public void init(ArrayList<CarouselBean> beans){
        init(beans,ANIM_NORMAL);
    }
    /**
     * 是否开启轮播，默认不开启
     * @param isRunning
     */
    public void setIsRunningCarousel(boolean isRunning){
        this.isRunning=isRunning;
    }

    /**
     * 轮播间隔时间，默认为5000毫秒
     * @param intervalsTime
     */
    public void setIntervalsTime(int intervalsTime){
        this.intervalsTime=intervalsTime;
    }

    /**
     * 设置底部描述布局的背景颜色。推荐#6000
     * @param colorResource
     */
    public void setBottomViewBackGroundColor(int colorResource){
        ll_bottomview.setBackgroundColor(colorResource);
    }

    /**
     * 设置是否显示指示器，默认显示
     * @param visible
     */
    public void setPointVisible(boolean visible){
        if (visible) ll_point.setVisibility(VISIBLE);
        else ll_point.setVisibility(GONE);
    }

    /**
     * 设置是否显示文字描述。默认不显示
     * @param visible
     */
    public void setDescVisible(boolean visible){
        if (visible) tv_desc.setVisibility(VISIBLE);
        else tv_desc.setVisibility(GONE);
    }
    private void initAnim(int anim) {
        switch (anim){
            case ANIM_ALPHA_PAGETRANS:
                excisionPage();
                viewPager.setPageTransformer(true,new AlphaPageTransformer());
                break;
            case ANIM_SCALEMAGIC:
                excisionPage();
                viewPager.setPageTransformer(true,new ScaleMagic());
                break;
            case ANIM_ROTATEMAGIC:
                excisionPage();
                viewPager.setPageTransformer(true,new RotateMagic());
                break;
            case ANIM_LEFTLEAVE:
                viewPager.setPageTransformer(true,new LeftLeave());
                break;
            case ANIM_SCALERIGHTLEAVE:
                viewPager.setPageTransformer(true,new ScaleRightLeave());
                break;
            case ANIM_NORMAL:
            default:
                viewPager.setClipChildren(true);
                frameLayout.setClipChildren(true);
                break;
        }
    }

    /**
     * 当需要ViewPager一个界面显示多个Item的时候，调用改方法。
     */
    private void excisionPage(){
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(120,40,120,40);
        viewPager.setLayoutParams(lp);
        viewPager.setClipChildren(false);
        frameLayout.setClipChildren(false);
        //设置Page间间距
        viewPager.setPageMargin(20);
        //设置缓存的页面数量
        if(beans.size()>5)viewPager.setOffscreenPageLimit(2);
    }
    /**
     * 初始化适配器
     */
    private void initAdapter() {
        ll_point.getChildAt(0).setEnabled(true);//初始化控件时，设置第一个小圆点为亮色
        tv_desc.setText(beans.get(0).getImgDescs()); //设置第一个图片对应的文字
        lastPosition=0;
        viewPager.setAdapter(new CarouseAdapter());
    }

    /**
     * 初始化控件
     */
    private void initView(){
        viewPager.setOnPageChangeListener(this);
        imageViews=new ArrayList<>();
        ImageView imageView;
        View pointView;
        for (int i = 0; i < beans.size(); i++){
            //添加图片到集合中
            imageView = new ImageView(mC);
            if(beans.get(i).getImgResource()!=0){
                imageView.setBackgroundResource(beans.get(i).getImgResource());
            }
            if(beans.get(i).getImgUrl()!=null&&!beans.get(i).getImgUrl().equals("")){
                ImageLoaderUtil instance = ImageLoaderUtil.getInstance(mC);
                instance.setDefaultImg(R.mipmap.default_publicimg);
                instance.loadingImage(beans.get(i).getImgUrl(),imageView);
            }
            imageViews.add(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片铺满xy轴，解决图片宽高小了导致样式丑陋的问题
            //加小白点，指示器（这里的小圆点定义在了drawable下的选择器中了，也可以用小图片代替）
            pointView = new View(mC);
            pointView.setBackgroundResource(R.drawable.carousel_point); //使用选择器设置背景
            LayoutParams layoutParams = new LayoutParams(16, 16);
            if (i != 0){
                //如果不是第一个点，则设置点的左边距
                layoutParams.leftMargin = 10;
            }
            pointView.setEnabled(false); //默认都是暗色的
            ll_point.addView(pointView, layoutParams);
        }
        if(imageViews.size()<=1)viewPager.setOffscreenPageLimit(0);
        if(imageViews.size()>0) LogUtil.i(TAG,"initView完成");
    }

    /**
     * 开启轮播
     */
    private  void startCarousel(){
        Log.i(TAG,"CarouselView轮播开启,isRunning="+isRunning);
        new Thread(){
            @Override
            public void run() {
                while(isRunning){
                    try {
                        Thread.sleep(intervalsTime);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    //页面滑动
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    //新的页面被选中
    @Override
    public void onPageSelected(int position) {
        //当前的位置可能很大，为了防止下标越界，对要显示的图片的总数进行取余
        int newPosition = position % beans.size();
        //设置描述信息
        if (beans.get(newPosition).getImgDescs()!=null)tv_desc.setText(beans.get(newPosition).getImgDescs());
        else tv_desc.setText("");
        //设置小圆点为高亮或暗色
        ll_point.getChildAt(lastPosition).setEnabled(false);
        ll_point.getChildAt(newPosition).setEnabled(true);
        lastPosition = newPosition; //记录之前的点
    }
    //页面滑动状态发生改变
    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private class CarouseAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //从集合中获得图片
            final int newPosition = position % imageViews.size(); //数组中总共有5张图片，超过数组长度时，取摸，防止下标越界
            ImageView imageView = imageViews.get(newPosition);
            //把图片添加到container中
            container.addView(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_onImageClickListener.onClick(beans.get(newPosition));
                }
            });

            //把图片返回给框架，用来缓存
            return imageView;
        }
        //销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //object:刚才创建的对象，即要销毁的对象
            container.removeView((View) object);
        }

    }

    public interface OnImageClickListener{
        void onClick(CarouselBean bean);
    }

    public void setOnImageClickListener(OnImageClickListener listener){
        this.m_onImageClickListener=listener;

    }


    public static class CarouselBean{

        private int imgResource;
        private String imgUrl;
        private String imgDescs;

        private String skipId;

        @Override
        public String toString() {
            return "CarouselBean{" +
                    "imgResource=" + imgResource +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", imgDescs='" + imgDescs + '\'' +
                    ", skipId='" + skipId + '\'' +
                    '}';
        }

        public String getSkipId() {
            return skipId;
        }

        public void setSkipId(String skipId) {
            this.skipId = skipId;
        }

        public int getImgResource() {
            return imgResource;
        }

        public void setImgResource(int imgResource) {
            this.imgResource = imgResource;
        }

        public String getImgDescs() {
            return imgDescs;
        }

        public void setImgDescs(String imgDescs) {
            this.imgDescs = imgDescs;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public CarouselBean(int imgResource, String imgDescs) {
            this.imgResource = imgResource;
            this.imgDescs = imgDescs;
        }

        public CarouselBean(){

        }
        public CarouselBean(int imgResource) {
            this.imgResource = imgResource;
        }

        public CarouselBean(String imgUrl,String imgDescs){
            this.imgUrl=imgUrl;
            this.imgDescs=imgDescs;
        }
        public CarouselBean(String imgUrl){
            this.imgUrl=imgUrl;
        }
    }

    /**
     * 单纯渐变动画
     */
    private class AlphaPageTransformer implements ViewPager.PageTransformer{
        private float mMinAlpha = 0.5f;
        @Override
        public void transformPage(View view, float position) {
            float factor;
            if (position < -1) {
                view.setAlpha(mMinAlpha);
            } else if (position <= 1) { // [-1,1]
                if (position <= 0){ //[-1，0)
                    factor = mMinAlpha + (1 - mMinAlpha) * (1 + position);
                    view.setAlpha(factor);
                } else{//[0，1]
                    factor = mMinAlpha + (1 - mMinAlpha) * (1 - position);
                    view.setAlpha(factor);
                }
            }else { // (1,+Infinity]
                view.setAlpha(mMinAlpha);
            }
        }
    }
    /**
     * 渐变+缩放动画
     */
    private class ScaleMagic implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.90f;
        private static final float MIN_ALPHA = 0.5f;
        @Override
        public void transformPage(View page, float position) {
            if (position < -1 || position > 1) {
                page.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else if (position <= 1) { // [-1,1]
                if (position < 0) {
                    float scaleX = 1 + 0.1f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                } else {
                    float scaleX = 1 - 0.1f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                }
                float scaleFactor = Math.max(MIN_ALPHA, 1 - Math.abs(position));
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_ALPHA) / (1 - MIN_ALPHA) * (1 - MIN_ALPHA));
            }
        }
    }

    /**
     * 旋转+渐变
     */
    private class RotateMagic implements ViewPager.PageTransformer {
        private float mMaxRotate = 15.0f;
        private float MIN_ALPHA = 0.7f;
        @Override
        public void transformPage(View view, float position) {
            if (position < -1) { // [-Infinity,-1)
                view.setRotation(mMaxRotate * -1);
                view.setPivotX(view.getWidth());
                view.setPivotY(view.getHeight());
            } else if (position <= 1) { // [-1,1]
                if (position <= 0){ //[0，-1]
                    view.setPivotX(view.getWidth() * (0.5f + 0.5f * (-position)));
                    view.setPivotY(view.getHeight());
                    view.setRotation(mMaxRotate * position);
                } else{//[1,0]
                    view.setPivotX(view.getWidth() * 0.5f * (1 - position));
                    view.setPivotY(view.getHeight());
                    view.setRotation(mMaxRotate * position);
                }
                float scaleFactor = Math.max(MIN_ALPHA, 1 - Math.abs(position));
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_ALPHA) / (1 - MIN_ALPHA) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                view.setRotation(mMaxRotate);
                view.setPivotX(view.getWidth() * 0);
                view.setPivotY(view.getHeight());
            }
        }
    }
    /**
     * 左边离开
     */
    private class LeftLeave implements ViewPager.PageTransformer {
        private final float MIN_SCALE = 0.75f;
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }

    }
    /**
     * 缩小右边离开
     */
    private class ScaleRightLeave implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }

    }
}
