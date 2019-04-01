package com.wusy.wusylibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;


/**
 * Created by XIAO RONG on 2018/8/23.
 * 这是一个能够实时向主View提供滑动距离Y值的ScrollView（滑动的高度）
 * 能够运用此ScrollView实现在内部View固定顶部布局的需求
 */

public class FixedHeadScrollView extends ScrollView{
    public FixedHeadScrollViewListener listener;
    private int scrollDistanceY;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    int newY=getScrollY();//在手指离开屏幕的短暂时间间隔后再次获取Y值
                    if(newY!=scrollDistanceY){//如果不相等，说明手指离开后，屏幕仍然在滑动,继续更新scrollDistanceY的值
                        scrollDistanceY=newY;
                        listener.sendDistanceY(scrollDistanceY);
                        handler.sendEmptyMessageDelayed(1,5);
                    }
                    break;
            }
        }
    };
    public FixedHeadScrollView(Context context) {
        this(context,null);
    }

    public FixedHeadScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FixedHeadScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_UP){
            handler.sendEmptyMessageDelayed(1,30);
        }
        scrollDistanceY=getScrollY();
        listener.sendDistanceY(scrollDistanceY);
        return super.onTouchEvent(ev);

    }

    public interface  FixedHeadScrollViewListener{
        void sendDistanceY(int distance);
    }

    public void setFixedHeadScrollViewListener(FixedHeadScrollViewListener listener) {
        this.listener = listener;
    }
}
