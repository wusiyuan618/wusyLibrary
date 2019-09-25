package com.wusy.wusylibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wusy.wusylibrary.R;

import java.util.ArrayList;
import java.util.List;

public class NumberKeyBoxView extends View implements View.OnTouchListener {
    private final String TAG="NumberKeyBoxView";
    private Paint mPaint;
    private Context mContext;
    private int mFontSize=30;//字体大小
    private int mRectWidth=150;//宫格按钮宽度
    private int mRectHeight=200;//宫格按钮高度
    private int mBaseInterval=1;//边框（间距）大小
    private int mButtonColor= Color.parseColor("#4032C09D");//按钮颜色
    private int mFontColor= Color.parseColor("#FFFFFF");//字体颜色
    private int mBorderColor= Color.parseColor("#32c09d");//边框颜色
    private List<Coordinate> coordinates;//宫格坐标集合，用于点击事件绑定
    private NumberKeyBoxViewClick numberKeyBoxViewClick;//对外提供点击接口
    public NumberKeyBoxView(Context context) {
        this(context,null);
    }

    public NumberKeyBoxView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberKeyBoxView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    @SuppressLint("NewApi")
    public NumberKeyBoxView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext=context;
        init();
    }
    private void init(){
        mPaint=new Paint();
        setOnTouchListener(this);
        coordinates=new ArrayList<>();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
        int width = 0;
        int height= 0 ;
        if (widthMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接赋值
            width = widthSize;
            this.mRectWidth=width/3-2*mBaseInterval;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
            width = (int) (getPaddingLeft() + mRectWidth*3+3*mBaseInterval + getPaddingRight());
        }
        //高度跟宽度处理方式一样
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
            this.mRectHeight=height/4-2*mBaseInterval;
        } else {
            height = (int) (getPaddingTop() + mRectHeight*4+5*mBaseInterval + getPaddingBottom());
        }
        //保存测量宽度和测量高度
        setMeasuredDimension(width, height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //数据组装
        if(coordinates.size()==0) dataInit();//防止作为POP显示时，重复绘制

        //开始绘制
        drawKeyBoxView(canvas);
    }
    /**
     * 绘制数据组装函数
     */
    private void dataInit(){
        for(int i=1;i<10;i++){
            int widthIndex=(i%3)==0 ? 3 : (i%3);//当前行数
            int heightIndex=(i-1)==0? 1:(i-1)/3+1;//当前列数
            //绘制宫格
            Coordinate coordinate=new Coordinate(
                    widthIndex*mBaseInterval+(widthIndex-1)*mRectWidth,
                    mBaseInterval*heightIndex+(heightIndex-1)*mRectHeight,
                    widthIndex*(mBaseInterval+mRectWidth),
                    (mBaseInterval +mRectHeight)* heightIndex,
                    i+"",
                    mRectWidth/2+mBaseInterval*widthIndex+mRectWidth*(widthIndex-1),
                    mRectHeight/2+(mFontSize/3+mBaseInterval*heightIndex)+(heightIndex-1)*mRectHeight
            );
            coordinates.add(coordinate);
        }
        //0键
        Coordinate coordinate0=new Coordinate(2*mBaseInterval + mRectWidth,
                4*mBaseInterval + 3 * mRectHeight,
                2*mBaseInterval + 2 * mRectWidth,
                4*mBaseInterval + 4 * mRectHeight,
                "0",
                mRectWidth/2+mBaseInterval*2+mRectWidth,
                mRectHeight/2+(mFontSize/3+mBaseInterval*4)+3*mRectHeight);
        coordinates.add(coordinate0);
        //删除键
        Coordinate coordinateDelete=new Coordinate(mBaseInterval,
                4*mBaseInterval + 3 * mRectHeight,
                mBaseInterval + mRectWidth,
                4*mBaseInterval + 4 * mRectHeight,
                "delete",
                mRectWidth/2-10,
                mRectHeight/2+(mFontSize/3+mBaseInterval*4)+3*mRectHeight-15);
        coordinates.add(coordinateDelete);
        //确定键
        Coordinate coordinateOk=new Coordinate(3*mBaseInterval + 2 * mRectWidth,
                4*mBaseInterval + 3 * mRectHeight,
                3*mBaseInterval + 3 * mRectWidth,
                4*mBaseInterval + 4 * mRectHeight,
                "ok",
                mRectWidth/2+mBaseInterval*3+mRectWidth*2-10,
                mRectHeight/2+(mFontSize/3+mBaseInterval*4)+3*mRectHeight-15);
        coordinates.add(coordinateOk);
    }
    /**
     * 绘制函数
     * @param canvas
     */
    @SuppressLint("NewApi")
    private void drawKeyBoxView(Canvas canvas){
        int column=3;//总列数
        int row=coordinates.size()/column;//总行数
        mPaint.setTextSize(mFontSize);// 设置字体大小
        mPaint.setStrokeWidth(2);
        mPaint.setTextAlign(Paint.Align.CENTER);

        //绘制边框
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBaseInterval);
        for (int i=0;i<=column;i++){
            canvas.drawLine(i*(mRectWidth+mBaseInterval),0,
                    i*(mRectWidth+mBaseInterval),row*mRectHeight+row*mBaseInterval,mPaint);
        }
        for (int i=0;i<=row;i++){
            canvas.drawLine(0,i*(mRectHeight+mBaseInterval),
                    mRectWidth*column+column*mBaseInterval,i*(mRectHeight+mBaseInterval),mPaint);
        }

        for (Coordinate coordinate:coordinates){
            //画按钮
            mPaint.setColor(mButtonColor);
            canvas.drawRect(coordinate.getLeft(), coordinate.getTop(),
                    coordinate.getRight(), coordinate.getBottom(), mPaint);
            //画内容
            if(coordinate.getValue().equals("ok")){
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.keyboxok),
                        coordinate.getCenterX(),coordinate.getCenterY(),mPaint);
            }else if(coordinate.getValue().equals("delete")){
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.keyboxdelete),
                        coordinate.getCenterX(),coordinate.getCenterY(),mPaint);
            }else{
                mPaint.setColor(mFontColor);
                canvas.drawText(coordinate.getValue(),coordinate.centerX,coordinate.centerY, mPaint);
            }
        }
    }

    /**
     * 利用坐标实现点击事件
     * @param x
     * @param y
     */
    public void handClick(float x,float y){
        for (Coordinate coordinate:coordinates){
            if(coordinate.getLeft()<x&&x<coordinate.getRight()&&
                    coordinate.getTop()<y&&y<coordinate.getBottom()){
                Log.i(TAG,"\n您点击了键盘，值为:"+coordinate.getValue()+
                        "\n起始坐标("+coordinate.getTop()+","+coordinate.getTop()+")" +
                        "\n结束坐标("+coordinate.getRight()+","+coordinate.getBottom()+")");
                if(numberKeyBoxViewClick!=null)numberKeyBoxViewClick.click(coordinate.getValue());
                else Log.e(TAG,"您没有绑定点击事件，通过实现NumberKeyBoxViewClick接口完成绑定");
                return;
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                Log.i(TAG,event.getX()+","+event.getY());
                handClick(event.getX(),event.getY());
                break;
        }
        return false;
    }

    /**
     * 点击时间对外开始接口回调
     */
    public interface NumberKeyBoxViewClick{
        void click(String value);
    }

    public void setNumberKeyBoxViewClick(NumberKeyBoxViewClick numberKeyBoxViewClick) {
        this.numberKeyBoxViewClick = numberKeyBoxViewClick;
    }

    /**
     * 坐标实体类
     */
    public class Coordinate{
        private float left;
        private float top;
        private float right;
        private float bottom;
        private String value;
        private float centerX;
        private float centerY;

        public Coordinate(float left, float top, float right, float bottom, String value) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.value = value;
        }

        public Coordinate(float left, float top, float right, float bottom, String value, float centerX, float centerY) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.value = value;
            this.centerX = centerX;
            this.centerY = centerY;
        }

        public float getCenterX() {
            return centerX;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public float getCenterY() {
            return centerY;
        }

        public void setCenterY(float centerY) {
            this.centerY = centerY;
        }

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getTop() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float getRight() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
