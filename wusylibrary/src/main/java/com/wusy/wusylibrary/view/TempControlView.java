package com.wusy.wusylibrary.view;

/**
 * Created by Administrator on 2017/3/9/009.
 * 使用实例：
 *  <com.wusy.wusylibrary.view.TempControlView
 *         android:id="@+id/tempControlView"
 *         android:layout_width="300dp"
 *         android:layout_height="300dp"
 *         android:layout_marginBottom="8dp"
 *         android:layout_marginLeft="8dp"
 *         android:layout_marginRight="8dp"
 *         android:layout_marginTop="8dp"
 *         app:button_src="@mipmap/ic_launcher"
 *         app:dial_padding="16dp"
 *         app:layout_constraintBottom_toBottomOf="parent"
 *         app:layout_constraintHorizontal_bias="0.5"
 *         app:layout_constraintLeft_toLeftOf="parent"
 *         app:layout_constraintRight_toRightOf="parent"
 *         app:layout_constraintTop_toTopOf="parent"
 *         app:layout_constraintVertical_bias="0.5"
 *         app:scale_height="15dp"/>
 *
 *  tempControlView!!.setTemp(16, 31, 18)
 *         tempControlView.setOnTempChangedListener {
 *             Log.i("wsy","当前温度：$it")
 *         }
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.wusy.wusylibrary.R;

/**
 * 温度控制
 */
public class TempControlView extends View {
    // PER_SCALE_DEGREE * SCALE_COUNT  = 270

    // 4 60 4.5f
    // 8 120 2.25f

    // 1℃ 所占的格子数 一共15个温度 分配在270°角里面
    private final static int PER_TEMP_SCALE = 8;
    // 刻度格子总数 60*4.5 =270
    private final static int SCALE_COUNT = 120;
    // 一格子所占度数
    private final static float PER_SCALE_DEGREE = 2.25f;

    // 控件宽
    private int width;
    // 控件高
    private int height;
    // 刻度盘半径
    private float mDialRadius;
    // 刻度高
    private float mScaleHeight;
    // 刻度盘与旋钮的间隙
    private float mDialPadding;
    private float mButtonWidth;
    private float mButtonHeight;
    private float mRatio;

    // 刻度盘画笔
    private Paint mDialPaint;
    // 旋转按钮画笔
    private Paint mButtonPaint;

    // 温度
    private int mTemperature = 15;
    // 当前游标
    private int mIndex;
    // 最低温度
    private int minTemp = 15;
    // 最高温度
    private int maxTemp = 30;
    // 四格（每格4.5度，共18度）代表温度1度
    private int angleRate = PER_TEMP_SCALE;
    // 按钮图片
    private Drawable mButtonDrawable;
    // 按钮图片阴影
    private Drawable mButtonDrawableShadow;
    // 抗锯齿
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;
    // 温度改变监听
    private OnTempChangedListener mOnTempChangedListener;

    // 以下为旋转按钮相关
    // 当前按钮旋转的角度
    private float rotateAngle = (mIndex) * angleRate * PER_SCALE_DEGREE;
    // 当前的角度
    private float currentAngle;
    // 刻度盘选中的颜色
    private int mDialSelectColor;
    // 刻度盘的颜色
    private int mDialColor;


    public TempControlView(Context context) {
        this(context, null);
    }

    public TempControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TempControlView, 0, 0);
        mDialPadding = typedArray.getDimension(R.styleable.TempControlView_dial_padding, dp2px(5));
        mDialSelectColor = typedArray.getColor(R.styleable.TempControlView_dial_select_color, 0xFF00ffc0);
        mDialColor = typedArray.getColor(R.styleable.TempControlView_dial_original_color, 0xffEAF6F5);
        mScaleHeight = typedArray.getDimension(R.styleable.TempControlView_scale_height, dp2px(10));

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.TempControlView_button_src) {
                mButtonDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.TempControlView_button_shadow_src) {
                mButtonDrawableShadow = typedArray.getDrawable(attr);
            }
        }
        init();
    }

    private void init() {
        mDialPaint = new Paint();
        mDialPaint.setAntiAlias(true);
        mDialPaint.setStrokeWidth(dp2px(4));
        mDialPaint.setStyle(Paint.Style.STROKE);

        mButtonPaint = new Paint();
        mButtonPaint.setAntiAlias(true);
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initCanvas();
        drawDial(canvas);
        drawButton(canvas);
        drawShadow(canvas);
    }

    private void initCanvas() {
        width = height = Math.min(getWidth(), getHeight());

        mButtonWidth = width - (mDialPadding + mScaleHeight) * 2 - getPaddingLeft() - getPaddingRight();

        mButtonHeight = height - (mDialPadding + mScaleHeight) * 2 - getPaddingTop() - getPaddingBottom();
        // 刻度盘内半径
        mDialRadius = (int) (mScaleHeight + mButtonHeight / 2 + mDialPadding);
    }

    /**
     * 绘制刻度盘
     *
     * @param canvas 画布
     */
    private void drawDial(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 逆时针旋转135-2度
        canvas.rotate(-133);
        // 底盘
        mDialPaint.setColor(mDialColor);
        for (int i = 0; i < SCALE_COUNT; i++) {
            canvas.drawLine(0, -mDialRadius, 0, -mDialRadius + mScaleHeight, mDialPaint);
            canvas.rotate(PER_SCALE_DEGREE);
        }

        canvas.rotate(90);
        mDialPaint.setColor(mDialSelectColor);
        if (mIndex <= maxTemp - minTemp && mIndex >= 0) {
            for (int i = 0; i < mIndex * angleRate; i++) {
                canvas.drawLine(0, -mDialRadius, 0, -mDialRadius + mScaleHeight, mDialPaint);
                canvas.rotate(PER_SCALE_DEGREE);
            }
        }
        canvas.restore();
    }

    /**
     * 绘制旋转按钮
     *
     * @param canvas 画布
     */
    private void drawButton(Canvas canvas) {
        canvas.save();
        // 按钮宽高
        //int buttonWidth = mButtonDrawable.getIntrinsicWidth();
        //int buttonHeight = mButtonDrawable.getIntrinsicHeight();
        /**
         * 使用矩阵可以达到同样的效果
         */
        //Matrix matrix = new Matrix();
        //// 设置按钮位置
        //matrix.setTranslate(buttonWidth / 2, buttonHeight / 2);
        //// 设置旋转角度
        //matrix.preRotate(45 + rotateAngle);
        //// 按钮位置还原，此时按钮位置在左上角
        //matrix.preTranslate(-buttonWidth / 2, -buttonHeight / 2);
        ////// 将按钮移到中心位置
        //matrix.postTranslate((width - buttonWidth) / 2, (height - buttonHeight) / 2);

        ////设置抗锯齿
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        //canvas.setMatrix(matrix);
        //int buttonX =- buttonWidth / 2;
        //int buttonY = - buttonHeight / 2;
        //mButtonDrawable.setBounds(0, 0, 0 + buttonWidth, 0 + buttonHeight);

        int buttonX = (int) (width / 2 - mButtonWidth / 2);
        int buttonY = (int) (height / 2 - mButtonHeight / 2);

        mRatio = mButtonWidth / mButtonDrawable.getIntrinsicWidth();

        mButtonDrawable.setBounds(buttonX, buttonY, (int) (buttonX + mButtonWidth), (int) (buttonY + mButtonHeight));
        canvas.rotate(45 + rotateAngle, width / 2, height / 2);
        mButtonDrawable.draw(canvas);
        canvas.restore();
    }

    private void drawShadow(Canvas canvas) {
        if (mButtonDrawableShadow != null) {
            canvas.save();

            // 按钮阴影宽高
            int buttonShadowWidth = (int) (mButtonDrawableShadow.getIntrinsicWidth() * mRatio);
            int buttonShadowHeight = (int) (mButtonDrawableShadow.getIntrinsicHeight() * mRatio);
            int buttonShadowX = width / 2 - buttonShadowWidth / 2;
            int buttonShadowY = height / 2 - buttonShadowHeight / 2;

            // 绘制按钮阴影
            mButtonDrawableShadow.setBounds(buttonShadowX, buttonShadowY, buttonShadowX + buttonShadowWidth, buttonShadowY + buttonShadowHeight);
            mButtonDrawableShadow.draw(canvas);
            canvas.restore();
        }
    }

    private boolean isDown;
    private boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                float downX = event.getX();
                float downY = event.getY();
                currentAngle = calcAngle(downX, downY);
                break;

            case MotionEvent.ACTION_MOVE:
                isMove = true;
                float targetX;
                float targetY;
                downX = targetX = event.getX();
                downY = targetY = event.getY();
                float angle = calcAngle(targetX, targetY);

                // 滑过的角度增量
                float angleIncreased = angle - currentAngle;
                // 防止越界
                if (angleIncreased < -270) {
                    //Logger.e("angleIncreased angleIncreased < -270   " + angleIncreased);
                    angleIncreased = angleIncreased + 360;
                } else if (angleIncreased > 270) {
                    //Logger.e("angleIncreased angleIncreased > 270   " + angleIncreased);
                    angleIncreased = angleIncreased - 360;
                }

                increaseAngle(angleIncreased);
                //decreaseAngle(angleIncreased);

                currentAngle = angle;
                invalidate();
                //if (mOnTempChangingListener != null) mOnTempChangingListener.onTouchFinish(mTemperature);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isDown && isMove) {
                    correctDegree();
                    invalidate();
                    // 回调温度改变监听
                    if (mOnTempChangedListener != null) {
                        mOnTempChangedListener.change(mTemperature);
                    }
                    isDown = false;
                    isMove = false;
                }
                break;
            }
        }
        return true;
    }

    /**
     * 纠正指针位置
     */
    private void correctDegree() {
        if (rotateAngle >= 0 && rotateAngle <= 252) {
//        if (rotateAngle >= 0 && rotateAngle <= 270 + 45 / 2) {
            rotateAngle = mIndex * angleRate * PER_SCALE_DEGREE;
            mTemperature = (int) (rotateAngle / PER_SCALE_DEGREE) / angleRate + minTemp;
        } else if (rotateAngle > 252) {
//        } else if (rotateAngle > 270 + 45 / 2) {
            rotateAngle = 252;
//            rotateAngle = 315;
            mIndex = maxTemp - minTemp;
            Log.i("tag","==============="+rotateAngle);
            mTemperature = (int) (rotateAngle / PER_SCALE_DEGREE) / angleRate + minTemp;
//            mTemperature = -1;
        } else if (rotateAngle < 0) {
            rotateAngle = 0;
            mIndex = 0;
            mTemperature = minTemp;
        }
    }

    /**
     * 以按钮圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     *
     * @param targetX x坐标
     * @param targetY y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        float x = targetX - width / 2;
        float y = targetY - height / 2;
        double radian;

        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }

    /**
     * 增加旋转角度
     *
     * @param angle 增加的角度
     */
    private void increaseAngle(float angle) {

        rotateAngle += angle;

        if (rotateAngle < 0) {
            rotateAngle = 0;
        }
        if (rotateAngle > 360) {
            rotateAngle = 315;
        }

        if (rotateAngle >= 0 && rotateAngle <= 270) {
            mIndex = (int) (rotateAngle / PER_SCALE_DEGREE) / angleRate;
        } else {
            mIndex = maxTemp - minTemp;
        }
    }

    /**
     * 设置温度
     *
     * @param minTemp 最小温度
     * @param maxTemp 最大温度
     * @param curTemp 设置的温度
     */
    public void setTemp(int minTemp, int maxTemp, int curTemp) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.mTemperature = curTemp;
        this.mIndex = mTemperature - minTemp;
        this.angleRate = SCALE_COUNT / (maxTemp - minTemp);
        rotateAngle = (curTemp - minTemp) * angleRate * PER_SCALE_DEGREE;
        invalidate();
    }

    /**
     * 设置温度改变监听
     *
     * @param onTempChangedListener 监听接口
     */
    public void setOnTempChangedListener(OnTempChangedListener onTempChangedListener) {
        this.mOnTempChangedListener = onTempChangedListener;
    }

    ///**
    // * 设置温度改变监听
    // *
    // * @param onTempChangeListener 监听接口
    // */
    //public void setOnTempInChangeListener(OnTempChangingListener onTempChangeListener) {
    //    this.mOnTempChangingListener = onTempChangeListener;
    //}

    /**
     * 温度改变结束监听接口
     */
    public interface OnTempChangedListener {
        /**
         * 回调方法
         *
         * @param temp 温度
         */
        void change(int temp);
    }
    //
    ///**
    // * 温度改变中监听接口
    // */
    //public interface OnTempChangingListener {
    //    /**
    //     * 回调方法
    //     *
    //     * @param temp 温度
    //     */
    //    void onTouchFinish(int temp);
    //}

    public void setRotateAngle(float rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}