package com.wusy.wusylibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.TypedValue;
import android.view.View;

/**
 * 闹钟表盘
 *
 * @author AJian
 */
public class ClockView extends View {

    private static final int LONG_LINE_HEIGHT = 35;
    private static final int SHORT_LINE_HEIGHT = 25;
    private Paint mCirclePaint, mLinePaint;
    private DrawFilter mDrawFilter;
    private int mHalfWidth, mHalfHeight;

    // 圆环线宽度
    private int mCircleLineWidth, mHalfCircleLineWidth;
    // 直线刻度线宽度
    private int mLineWidth, mHalfLineWidth;
    // 长线长度
    private int mLongLineHeight;
    // 短线长度
    private int mShortLineHeight;
    // 刻度线的左、上位置
    private int mLineLeft, mLineTop;

    // 刻度线的下边位置
    private int mLineBottom;
    // 用于控制刻度线位置
    private int mFixLineHeight;

    public ClockView(Context context) {
        super(context);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);

        mCircleLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        mHalfCircleLineWidth = mCircleLineWidth;
        mLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());
        mHalfLineWidth = mLineWidth / 2;

        mFixLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());

        mLongLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                LONG_LINE_HEIGHT,
                getResources().getDisplayMetrics());
        mShortLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                SHORT_LINE_HEIGHT,
                getResources().getDisplayMetrics());
        initPaint();
    }

    private void initPaint() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);
        // 将画笔设置为空心
        mCirclePaint.setStyle(Paint.Style.STROKE);
        // 设置画笔宽度
        mCirclePaint.setStrokeWidth(mCircleLineWidth);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 设置画笔宽度
        mLinePaint.setStrokeWidth(mLineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);
        super.onDraw(canvas);
        // 绘制表盘
        drawCircle(canvas);
        // 绘制刻度
        drawLines(canvas);
    }

    /**
     * 绘制刻度
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        for (int i = 0; i <= 360; i++) {
            if (i % 30 == 0) {
                mLineBottom = mLineTop + mLongLineHeight;
                mLinePaint.setStrokeWidth(mLineWidth);
            } else {
                mLineBottom = mLineTop + mShortLineHeight;
                mLinePaint.setStrokeWidth(mHalfLineWidth);
            }

            if (i % 6 == 0) {
                canvas.save();
                canvas.rotate(i, mHalfWidth, mHalfHeight);
                canvas.drawLine(mLineLeft, mLineTop, mLineLeft, mLineBottom, mLinePaint);
                canvas.restore();
            }
        }
    }

    /**
     * 绘制表盘
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mHalfWidth, mHalfHeight, mHalfWidth - mHalfCircleLineWidth, mCirclePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHalfWidth = w / 2;
        mHalfHeight = h / 2;

        mLineLeft = mHalfWidth - mHalfLineWidth;
        mLineTop = mHalfHeight - mHalfWidth + mFixLineHeight;
    }
}
