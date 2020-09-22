package com.wusy.wusylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustGridView extends GridView {
    public CustGridView(Context context) {
        super(context);
    }

    public CustGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
