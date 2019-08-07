package com.wusy.wusylibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.wusy.wusylibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10/010.
 * 如何使用-----》Test
 * xml：
 * <com.wusy.wusylibrary.view.ColorCircleProgressView
 *         xmlns:app="http://schemas.android.com/apk/res-auto"
 *         android:id="@+id/lightCCP"
 *         android:layout_width="200dp"
 *         android:layout_height="200dp"
 *         android:layout_centerHorizontal="true"
 *         android:layout_marginBottom="0dp"
 *         android:layout_marginEnd="0dp"
 *         android:layout_marginStart="0dp"
 *         android:layout_marginTop="0dp"
 *         app:StartAngle="135"
 *         app:ViewAngle="270"
 *         app:circle_stroke_width="5dp"
 *         app:color_mid_1="@color/yellow"
 *         app:color_mid_2="@color/orange"
 *         app:color_mid_3="@color/blue"
 *         app:color_start="@color/main_green"
 *         app:dial_color="@color/tran_green"
 *         app:dial_count="5"
 *         app:dial_height="4dp"
 *         app:dial_text_size="17dp"
 *         app:layout_constraintBottom_toBottomOf="parent"
 *         app:layout_constraintEnd_toEndOf="parent"
 *         app:layout_constraintStart_toStartOf="parent"
 *         app:layout_constraintTop_toTopOf="parent"
 *         app:layout_constraintVertical_bias="0.50"
 *         app:point_color="@color/white_3"
 *         app:point_radio="5dp"
 *         app:round="false"
 *         app:view_padding="20dp"
 *         />
 */

public class ColorCircleProgressView extends View {
    // 刻度盘的格数
    private final static int SCALE_COUNT = 12;

    private final static float PI = 3.14f;
    // 控件的宽
    private int viewWidth;
    private int viewHeight;

    // 圆环画笔
    private Paint mCirclePaint;
    private Paint mCircleImpressionPaint;
    // 移动点画笔
    private Paint mPointPaint;
    // 刻度盘画笔
    private Paint mDialPaint;
    // 温度标识画笔
    private Paint mDialTextPaint;

    private float mCircleRadius;

    // 刻度盘的格子数
    private float mDialCount;
    private float mDialPerDegree;
    // 刻度盘半径
    private float mDialRadius;

    private int mDialColors;

    private float mDialHeight, mDialPadding;
    /** 预植 0-100 */
    private int mDefaultPercent = -1;

    // 最小
    private int mMinDialValue = 0;
    // 最大
    private int mMaxDialValue = 100;

    private float mDialTextSize;

    private float mCircleStrokeWith;
    private boolean mIsRound;
    private boolean isDial;
    private int mColor01;
    private int mColor02;
    private int mColor03;
    private int mColor04;
    private int mColor05;
    private int mColor06;
    private float mViewAngle;
    private float mStartAngle;
    private float mEndAngle;

    private float mViewPadding;
    private int mPointColor;
    private float mPointRaido;
    private float circleCenterX;
    private float circleCenterY;
    private float mPointAngle;
    // 只会是在手拨动Ui的时候触发
    private OnProgressListener mOnProgressListener;
    private int[] mSweepGradientColors;
    private RuntimeException mException;

    private Drawable mPointPopupDrawable;
    private Drawable mExtraPointPopupDrawable;
    private int mExtraProgress = -1;
    /**
     * 是否能触摸
     */
    private boolean isTouch = true;

    private Matrix mSweepGradientMatrix;
    /*是否有底环*/
    private boolean isHaveImpression = true;

    public ColorCircleProgressView(Context context) {
        super(context);
    }

    public ColorCircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /*获取属性集合*/
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ColorCircleProgressView, 0, 0);

       /*渐变颜色值*/
        mColor01 = typedArray.getColor(R.styleable.ColorCircleProgressView_color_start, 0);
        mColor02 = typedArray.getColor(R.styleable.ColorCircleProgressView_color_mid_1, 0);
        mColor03 = typedArray.getColor(R.styleable.ColorCircleProgressView_color_mid_2, 0);
        mColor04 = typedArray.getColor(R.styleable.ColorCircleProgressView_color_mid_3, 0);
        mColor05 = typedArray.getColor(R.styleable.ColorCircleProgressView_color_mid_4, 0);
        mColor06 = typedArray.getColor(R.styleable.ColorCircleProgressView_color_end, 0);

        mSweepGradientColors = controlSweepGradientColors();

        /*圆环角度，开始的角度，到边框的距离*/
        mViewAngle = typedArray.getInteger(R.styleable.ColorCircleProgressView_ViewAngle, 270);
        mStartAngle = typedArray.getInteger(R.styleable.ColorCircleProgressView_StartAngle, 135);
        mViewPadding = typedArray.getDimension(R.styleable.ColorCircleProgressView_view_padding, 10);

        mMaxDialValue = typedArray.getInteger(R.styleable.ColorCircleProgressView_dial_max_value, 100);
        mMinDialValue = typedArray.getInteger(R.styleable.ColorCircleProgressView_dial_min_value, 0);

        /*圆环的大小及是否圆角*/
        mCircleStrokeWith = typedArray.getDimension(R.styleable.ColorCircleProgressView_circle_stroke_width, dp2px(5));
        mIsRound = typedArray.getBoolean(R.styleable.ColorCircleProgressView_round, true);

        /*Point的颜色和大小*/
        mPointColor = typedArray.getColor(R.styleable.ColorCircleProgressView_point_color, Color.WHITE);
        mPointRaido = typedArray.getDimension(R.styleable.ColorCircleProgressView_point_radio, dp2px(5));
        isDial = typedArray.getBoolean(R.styleable.ColorCircleProgressView_isDial, true);

        mDialColors = typedArray.getColor(R.styleable.ColorCircleProgressView_dial_color, Color.BLACK);
        mDialHeight = typedArray.getDimension(R.styleable.ColorCircleProgressView_dial_height, dp2px(5));
        mDialPadding = typedArray.getDimension(R.styleable.ColorCircleProgressView_dial_pad, 0);

        mDialCount = typedArray.getInteger(R.styleable.ColorCircleProgressView_dial_count, SCALE_COUNT);
        mDialPerDegree = mViewAngle / mDialCount;

        mDialTextSize = typedArray.getDimension(R.styleable.ColorCircleProgressView_dial_text_size, dp2px(5));

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int typedArrayIndex = typedArray.getIndex(i);
            if (typedArrayIndex == R.styleable.ColorCircleProgressView_point_popup) {
                mPointPopupDrawable = typedArray.getDrawable(typedArrayIndex);
            }
        }

        mPointAngle = mStartAngle;
        if (mException != null) {
            throw mException;
        }
    }

    private void initCanvas() {
        //使宽高相同
        viewWidth = viewHeight = Math.min(getWidth(), getHeight());

        /*把宽高赋值给全局变量,得到圆心的坐标*/
        circleCenterX = viewWidth >> 1; // 除以二的意思
        circleCenterY = viewHeight >> 1;

        // 圆环外沿半径
        mCircleRadius = circleCenterX - (0 + mViewPadding);
        // 刻度盘半径
        mDialRadius = mCircleRadius;

        if (mDefaultPercent != -1) {
            mPointAngle = getProgressToAngle(mDefaultPercent);
            mDefaultPercent = -1;
        }
    }

    private float getProgressToAngle(int progress) {
        return (int) (progress * (mViewAngle / 100) + mStartAngle);
    }

    private void setDialPaint() {
        mDialPaint = new Paint();
        mDialPaint.setAntiAlias(true);
        mDialPaint.setStrokeWidth(dp2px(1));
        mDialPaint.setStyle(Paint.Style.STROKE);
    }

    private void setPointPaint() {
        mPointPaint = new Paint();
        mPointPaint.setColor(mPointColor);
        mPointPaint.setAntiAlias(true);               /*抗锯齿*/
    }

    private void setCirclePaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);  /*画笔为线条线条*/
        mCirclePaint.setStrokeWidth(mCircleStrokeWith);     /*线条的宽*/
        mCirclePaint.setAntiAlias(true);               /*抗锯齿*/

        if (mIsRound) {
            mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        }  /*是否圆角*/
    }

    private void setCircleImpressionPaint() {
        mCircleImpressionPaint = new Paint();
        mCircleImpressionPaint.setStyle(Paint.Style.STROKE);  /*画笔为线条线条*/
        mCircleImpressionPaint.setStrokeWidth(mCircleStrokeWith);     /*线条的宽*/
        mCircleImpressionPaint.setAntiAlias(true);               /*抗锯齿*/
        mCircleImpressionPaint.setColor(mDialColors);
        if (mIsRound) {
            mCircleImpressionPaint.setStrokeCap(Paint.Cap.ROUND);
        }  /*是否圆角*/
    }

    private void setDialTextPaint() {
        mDialTextPaint = new Paint();
        mDialTextPaint.setAntiAlias(true);
        mDialTextPaint.setTextSize(mDialTextSize);
        mDialTextPaint.setColor(mDialColors);
        mDialTextPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initCanvas();
           /*设置圆环画笔*/
        setCirclePaint();
         /*设置圆环底环画笔*/
        setCircleImpressionPaint();

        /*设置移动点的画笔*/
        setPointPaint();
        /*设置低环的画笔*/
        setDialPaint();
        /*设置刻度的画笔*/
        setDialTextPaint();

        /*绘制圆环*/
        drawCircle(canvas);
        if (isDial) {
        /*绘制刻度盘*/
            drawDial(canvas);
        /* 绘制刻度值 */
            drawDialText(canvas);
        }

        /*绘制游标点*/
        drawPoint(canvas);

        extraDrawPoint(canvas);
    }

    private void drawCircle(Canvas canvas) {
        canvas.save();
        /*设置线性渐变*/
        if (mSweepGradientColors.length >= 2) {
            mSweepGradientMatrix = new Matrix();
            // 旋转渐变的角度 始终保持开始角度是从color_start颜色开始
            mSweepGradientMatrix.setRotate(-(360 - mViewAngle) / 2 + mStartAngle, circleCenterX, circleCenterY);
            SweepGradient sweepGradient = new SweepGradient(circleCenterX, circleCenterY, mSweepGradientColors, null);
            sweepGradient.setLocalMatrix(mSweepGradientMatrix);
            mCirclePaint.setShader(sweepGradient);
        } else {
            mCirclePaint.setColor(mColor01);
        }
        /*定义圆环的所占的矩形区域:注意view一定为正方形*/
        RectF rectF = new RectF(0 + mViewPadding, 0 + mViewPadding, viewWidth - mViewPadding, viewWidth - mViewPadding);
        /*根据矩形区域画扇形:因为sweep的起点在右边中心处，所以先旋转90度画布*/
        //        canvas.rotate(mOffsetAngle, circleCenterX, circleCenterY);
        if (!isHaveImpression) {
            canvas.drawArc(rectF, mStartAngle, mViewAngle, false, mCirclePaint);
        } else {
            float circleAngleWidth = getCircleAngleWidth();
            //这里采用的画法是：先绘制进度部分，再绘制剩余部分，并没有先将底色完整的画完
            canvas.drawArc(rectF, mStartAngle, circleAngleWidth, false, mCirclePaint);
            canvas.drawArc(rectF, mPointAngle, mViewAngle - circleAngleWidth, false, mCircleImpressionPaint);
        }
        canvas.restore();
    }

    // 拖动的点和开始点的角度差
    private float getCircleAngleWidth() {
        float circleAngleWidth;
        if (mStartAngle > mPointAngle) {
            circleAngleWidth = 360 - mStartAngle + mPointAngle;
        } else {
            circleAngleWidth = mPointAngle - mStartAngle;
        }
        return circleAngleWidth;
    }

    private void drawDial(Canvas canvas) {
        canvas.save();
        canvas.translate(circleCenterX, circleCenterY);
        canvas.rotate(mStartAngle);
        // 底盘
        mDialPaint.setColor(mDialColors);
        for (int i = 0; i <= mDialCount; i++) {
            canvas.drawLine(mDialRadius - mDialHeight - mCircleStrokeWith, 0, mDialRadius - mCircleStrokeWith / 2 - mDialPadding, 0, mDialPaint);
            canvas.rotate(mDialPerDegree);
        }
        canvas.restore();
    }

    private void drawDialText(Canvas canvas) {
        canvas.save();
        canvas.translate(circleCenterX, circleCenterY);
        canvas.rotate(mStartAngle + 90);

        // 底盘
        for (int i = 0; i <= mDialCount; i++) {
            int curValue = (int) (i * (mMaxDialValue - mMinDialValue) / mDialCount);
            float tempFlagWidth = mDialTextPaint.measureText(curValue + "");
            canvas.drawText(curValue + "", 0 - tempFlagWidth / 2, -mDialRadius + mDialHeight + mDialTextSize + mCircleStrokeWith, mDialTextPaint);
            canvas.rotate(mDialPerDegree);
        }

        canvas.restore();
    }

    int mProgress;

    private void drawPoint(Canvas canvas) {
        canvas.save();

        //if (mOnProgressListener != null) {
        if (mStartAngle < mEndAngle) {
            mProgress = (int) ((mPointAngle - mStartAngle) / (mViewAngle / 100));
        } else {
            if (mPointAngle - mStartAngle < 0) {
                mProgress = (int) ((mPointAngle - mStartAngle + 360) / (mViewAngle / 100));
            } else {
                mProgress = (int) ((mPointAngle - mStartAngle) / (mViewAngle / 100));
            }
        }

        //mOnProgressListener.onScrollingListener(mProgress);
        //}

        float radius = ((viewWidth - mViewPadding - mViewPadding) / 2);
        float pointCircleCenterX = (float) (circleCenterX + radius * Math.cos(mPointAngle * PI / 180));
        float pointCircleCenterY = (float) (circleCenterY + radius * Math.sin(mPointAngle * PI / 180));
        canvas.drawCircle(pointCircleCenterX, pointCircleCenterY, mPointRaido, mPointPaint);
            /*绘制popup src*/
        drawPopupDrawable(canvas, pointCircleCenterX, pointCircleCenterY, mPointPopupDrawable);
        canvas.restore();
    }

    /**
     *
     * @param canvas
     */
    private void extraDrawPoint(Canvas canvas) {
        if (mExtraProgress < 0) return;

        canvas.save();
        float pointPaint = getProgressToAngle(mExtraProgress);
        float radius = (viewWidth - mViewPadding - mViewPadding) / 2;
        float pointCircleCenterX = (float) (circleCenterX + radius * Math.cos(pointPaint * PI / 180));
        float pointCircleCenterY = (float) (circleCenterY + radius * Math.sin(pointPaint * PI / 180));
        canvas.drawCircle(pointCircleCenterX, pointCircleCenterY, mPointRaido, mPointPaint);
        drawPopupDrawable(canvas, pointCircleCenterX, pointCircleCenterY, mExtraPointPopupDrawable);
        canvas.restore();
    }

    private void drawPopupDrawable(Canvas canvas, float pointCircleCenterX, float pointCircleCenterY, Drawable mPointPopupDrawable) {
        if (mPointPopupDrawable != null) {
            int pointPopupLeft = (int) (pointCircleCenterX - mPointPopupDrawable.getIntrinsicWidth() / 2);
            int pointPopupTop = (int) (pointCircleCenterY - mPointPopupDrawable.getIntrinsicHeight() - mPointRaido);
            mPointPopupDrawable.setBounds(pointPopupLeft, pointPopupTop, pointPopupLeft + mPointPopupDrawable.getIntrinsicWidth(),
                    pointPopupTop + mPointPopupDrawable.getIntrinsicHeight());
            mPointPopupDrawable.draw(canvas);
        }
    }

    private int[] controlSweepGradientColors() {
        List<Integer> colorIntegerLists = new ArrayList<>();
        if (mColor01 != 0) {
            colorIntegerLists.add(mColor01);
        }
        if (mColor02 != 0) {
            colorIntegerLists.add(mColor02);
        }
        if (mColor03 != 0) {
            colorIntegerLists.add(mColor03);
        }
        if (mColor04 != 0) {
            colorIntegerLists.add(mColor04);
        }
        if (mColor05 != 0) {
            colorIntegerLists.add(mColor05);
        }
        if (mColor06 != 0) {
            colorIntegerLists.add(mColor06);
        }

        return toIntArray(colorIntegerLists);
    }

    private int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouch) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mOnProgressListener != null) {
                        mOnProgressListener.onScrollStartListener(mProgress);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mOnProgressListener != null) {
                        mOnProgressListener.onScrollingListener(mProgress);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mOnProgressListener != null) {
                        mOnProgressListener.onScrollEndListener(mProgress);
                    }
                    break;
            }
        /*获取点击位置的坐标*/
            float actionX = event.getX();
            float actionY = event.getY();

        /*根据坐标转换成对应的角度*/
            float getX0 = actionX - circleCenterX;
            float getY0 = actionY - circleCenterY;
        /*01：左下角区域*/
            if (getX0 <= 0 && getY0 >= 0) {
                float tan_x = getX0 * (-1);
                float tan_y = getY0;
                double atan = Math.atan(tan_x / tan_y);
                mPointAngle = (int) Math.toDegrees(atan) + 90;
            }

        /*02：左上角区域*/
            if (getX0 <= 0 && getY0 <= 0) {
                float tan_x = getX0 * (-1);
                float tan_y = getY0 * (-1);
                double atan = Math.atan(tan_y / tan_x);
                mPointAngle = (int) Math.toDegrees(atan) + 180;
            }

        /*03：右上角区域*/
            if (getX0 >= 0 && getY0 <= 0) {
                float tanX = getX0;
                float tanY = getY0 * (-1);
                double atan = Math.atan(tanX / tanY);
                mPointAngle = (int) Math.toDegrees(atan) + 270;
            }

        /*04：右下角区域*/
            if (getX0 >= 0 && getY0 >= 0) {
                float tan_x = getX0;
                float tan_y = getY0;
                double atan = Math.atan(tan_y / tan_x);
                mPointAngle = (int) Math.toDegrees(atan);
            }

            //  控制拖动越界的处理
            mEndAngle = mStartAngle + mViewAngle > 360 ? mStartAngle + mViewAngle - 360 : mStartAngle + mViewAngle;

            // start 0 end 270
            if (mStartAngle < mEndAngle && !(mPointAngle < mEndAngle && mPointAngle > mStartAngle)) {
                if (Math.abs(mStartAngle - mPointAngle) > Math.abs(mEndAngle - mPointAngle)) {
                    mPointAngle = mEndAngle;
                } else {
                    mPointAngle = mStartAngle;
                }
            }

            // e.g. start 135 end 45
            if (mStartAngle > mEndAngle && !((mStartAngle <= mPointAngle && mPointAngle <= 360) || (mPointAngle >= 0 && mPointAngle <= mEndAngle))) {
                if (Math.abs(mStartAngle - mPointAngle) > Math.abs(mEndAngle - mPointAngle)) {
                    mPointAngle = mEndAngle;
                } else {
                    mPointAngle = mStartAngle;
                }
            }

            /*得到点的角度后进行重绘*/
            invalidate();
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (w < h) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

    public interface OnProgressListener {
        void onScrollStartListener(int progress);

        void onScrollingListener(int progress);

        void onScrollEndListener(int progress);
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    public void setDefaultPercent(int defaultPercent) {
        this.mDefaultPercent = defaultPercent;
        invalidate();
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    public Drawable getPointPopupDrawable() {
        return mPointPopupDrawable;
    }

    public void setPointPopupDrawable(Drawable pointPopupDrawable) {
        mPointPopupDrawable = pointPopupDrawable;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        mOnProgressListener = onProgressListener;
    }

    public boolean isHaveImpression() {
        return isHaveImpression;
    }

    public void setHaveImpression(boolean haveImpression) {
        isHaveImpression = haveImpression;
    }

    public int getExtraProgress() {
        return mExtraProgress;
    }

    public void setExtraProgress(int extraProgress) {
        mExtraProgress = extraProgress;
    }

    public Drawable getExtraPointPopupDrawable() {
        return mExtraPointPopupDrawable;
    }

    public void setExtraPointPopupDrawable(Drawable extraPointPopupDrawable) {
        mExtraPointPopupDrawable = extraPointPopupDrawable;
    }

    public int getMaxDialValue() {
        return mMaxDialValue;
    }

    public void setMaxDialValue(int maxDialValue) {
        mMaxDialValue = maxDialValue;
    }
}