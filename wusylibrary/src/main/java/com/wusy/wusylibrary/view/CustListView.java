package com.wusy.wusylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * 
 * 自定义listView 用于解决与ScrollView 的冲突问题
 * 
 * @author Administrator
 * 
 */
public class CustListView extends ListView {

	public CustListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

 	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 	int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	} 

}
