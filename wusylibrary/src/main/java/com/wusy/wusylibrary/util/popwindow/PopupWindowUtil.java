package com.wusy.wusylibrary.util.popwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.base.BaseMyAdapter;

import java.util.List;

/**
 * Created by DalaR on 2017/11/24.
 * 2018-4-20 增加了更多了弹窗样式
 */

public class PopupWindowUtil {
    private static PopupWindowUtil util;
    private CommonPopupWindow popupWindow;

    private PopupWindowUtil(){

    }
    public synchronized static PopupWindowUtil getInstance(){
        if(util==null){
            util=new PopupWindowUtil();
        }
        return util;
    }

    /**
     * 构建弹窗，出现在目标View的左下角，与目标View左对齐。可以修改xoff更改
     * @param view 目标view
     * @param context 上下文参数
     * @param listener 为弹窗设计点击事件。实现ViewInterface接口，有两个参数，第一个View是目标view，
     *                 第二个layoutId是弹窗的layoutId，用语判断具体layout
     * @param layout  弹窗的
     */
    public void showBottomPop(View view, Context context, CommonPopupWindow.ViewInterface listener,int layout) {
        if (isCanClose()) closePopupWindow();
        popupWindow = new CommonPopupWindow.Builder(context)
                .setView(layout)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimRight)
                .setViewOnclickListener(listener)
                .create();
        popupWindow.showAsDropDown(view,0,0);
    }

    /**
     * 构建弹窗，仿IOS，从下往上弹出.弹窗中间采用LIstView构建内容
     * @param context 上下文参数
     * @param list 内容列表
     * @param onItemClickListener 内容的点击事件
     */
    public void showSelectPop(Context context, List<PopSelectBean> list,
                              AdapterView.OnItemClickListener onItemClickListener) {
        if (isCanClose()) closePopupWindow();
        //获取弹窗View
        View upView = LayoutInflater.from(context).inflate(R.layout.popwindow_select, null);
        //为弹窗View的列表填充数据
        ListView listView= (ListView) upView.findViewById(R.id.popwindow_select_listview);
        listView.setAdapter(new PopSelectAdapter(context,list));
        listView.setOnItemClickListener(onItemClickListener);
        //为弹窗View的取消按钮设置点击事件
        upView.findViewById(R.id.popwindow_select_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopupWindow();
            }
        });
        //使弹窗View的遮盖部分添加点击事件，点击关闭弹窗
        upView.findViewById(R.id.pop_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopupWindow();
            }
        });
        //测量View的宽高
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        upView.measure(w, h);
        //构建弹窗
        popupWindow = new CommonPopupWindow.Builder(context)
                .setView(upView)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .create();
//        //显示弹窗
        popupWindow.showAtLocation(((Activity)context).findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 利用showBottomPop构建一个评价的弹窗，具有点赞和踩一下两个按钮
     * @param view
     * @param context
     * @param listener
     */
    public void showEvaluationPop(View view, Context context, CommonPopupWindow.ViewInterface listener){
        showBottomPop(view,context,listener,R.layout.popwindow_evaluation);
    }

    /**
     * 关闭弹窗
     */
    public void closePopupWindow(){
        if(isCanClose()){
            popupWindow.dismiss();
            popupWindow=null;
        }
    }

    /**
     * 判断弹窗是否可以关闭
     * @return
     */
    private boolean isCanClose(){
        return popupWindow!=null&&popupWindow.isShowing();
    }

    /**
     * PopSelect窗口的实体类，可拓展实现更多需求
     */
    public static class PopSelectBean{
        private String content;

        public PopSelectBean(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * PopSelect窗口的ListView适配器
     */
    class PopSelectAdapter extends BaseMyAdapter<PopSelectBean>{


        public PopSelectAdapter(Context mC, List<PopSelectBean> list) {
            super(mC, list);
        }

        @Override
        public View initView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            if(view==null){
                view=LayoutInflater.from(getmC()).inflate(R.layout.popwindow_select_item,null);
                viewHolder=new ViewHolder();
                viewHolder.tv= (TextView) view.findViewById(R.id.popwindow_select_item_content);
                view.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.tv.setText(getList().get(position).getContent());
            return view;
        }
        class ViewHolder{
            TextView tv;
        }
    }
}
