package com.wusy.wusylibrary.view.bottomSelect;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DalaR on 2017/11/20.
 * 底部选择器，多用于首页。与BottomSelectBean一起使用。
 * 使用中需要构建BottomSelectBean的集合list,至少需要初始化标题信息，是否选中，默认图片，选中图片以及Item对应的Fragment
 * 可选择传入实现点击Listen
 * 在Activity中调用addFragmentForItem和createLayout两个方法即可
 * 2018-6-7修改了加载方式，不会一次性对全部Fragment进行添加和加载，当第一次选中目标模块时，再加载该模块。修改方法：addFragmentForItem（）和changeShowFragment（）
 */

public class BottomSelectView extends LinearLayout {
    private List<BottomSelectBean> list = new ArrayList<>();
    //整个BottomSelectView的布局对象
    public LinearLayout ll_view;
    private BottomSelectViewClickListener clickListener;

    public BottomSelectView(Context context) {
        this(context, null);
    }


    public BottomSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_bottomview, this);
        findview();
        init(context);
    }

    private void findview() {
        ll_view = (LinearLayout) findViewById(R.id.view_bottomview);
    }

    private void init(Context context) {

    }

    public List<BottomSelectBean> getList() {
        return list;
    }

    public void setList(List<BottomSelectBean> list) {
        this.list = list;
    }

    /**
     * 构建底部Item 利用java构建weight为1的LinearLaytou然后向里面添加文件和图片。
     * 并且实现点击事件，默认调用样式切换方法以及碎片切换方法。并对外提供点击接口
     *
     * @param context
     * @param list    BottomSelectBean的实体类的集合
     * @param manager 碎片管理器，在处理Fragment与各个Item构建点击链接时使用
     * @param layout  fragment使用的Layout。即fragment显示的地方
     */
    public void createLayout(Context context, final List<BottomSelectBean> list, final FragmentManager manager, final int layout) {
        this.list = list;
        addFragmentForItem(layout, manager, list);
        for (int i = 0; i < list.size(); i++) {
            final BottomSelectBean bean = list.get(i);
            LinearLayout inearLayout = new LinearLayout(context);
            inearLayout.setOrientation(LinearLayout.VERTICAL);
            inearLayout.setGravity(Gravity.CENTER);
            LayoutParams params_inearLayout = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            params_inearLayout.weight = 1;

            ImageView imageView = new ImageView(context);
            LayoutParams params_imageView = new LayoutParams(63, 63);
            params_imageView.topMargin = 18;
            if (bean.getTitle() == null) {
                params_imageView = new LayoutParams(126, 126);
                params_imageView.topMargin = 9;
                params_imageView.bottomMargin = 9;
            }

            TextView textView = new TextView(context);
            textView.setTextSize(10);
            textView.setText(bean.getTitle());
            LayoutParams params_textView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params_textView.topMargin = 10;
            params_textView.bottomMargin = 12;
            if (bean.isSelect()) {
                imageView.setImageResource(bean.getSelectIcon());
                textView.setTextColor(getResources().getColor(R.color.selectColor));
            } else {
                imageView.setImageResource(bean.getNormalIcon());
                textView.setTextColor(getResources().getColor(R.color.normalColor));
            }
            inearLayout.addView(imageView, params_imageView);
            if (bean.getTitle() != null) {
                inearLayout.addView(textView, params_textView);
            }

            ll_view.addView(inearLayout, params_inearLayout);
            inearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getTitle() != null) {
                        changeSelectItem(list, bean);
                        changeShowFragment(layout, list, bean, manager);
                    }
                    if (CommonUtil.isNull(bean.getListener())) {
                        bean.getListener().clickListener();
                    }
                }
            });
            bean.setImageView(imageView);
            bean.setTextView(textView);
            bean.setLinearLayout(inearLayout);
        }
    }

    /**
     * 底部Item切换改变样式的方法
     *
     * @param list       所有底部Item的集合
     * @param selectBean 被点中的Item
     */
    private void changeSelectItem(List<BottomSelectBean> list, BottomSelectBean selectBean) {
        for (int i = 0; i < list.size(); i++) {
            if (selectBean.getTitle() != null) {
                if (selectBean.getTitle().equals(list.get(i).getTitle())) {
                    list.get(i).setSelect(true);
                    list.get(i).getTextView().setTextColor(getResources().getColor(R.color.selectColor));
                    list.get(i).getImageView().setImageResource(list.get(i).getSelectIcon());
                } else {
                    list.get(i).setSelect(false);
                    list.get(i).getTextView().setTextColor(getResources().getColor(R.color.normalColor));
                    list.get(i).getImageView().setImageResource(list.get(i).getNormalIcon());
                }
            }

        }
    }

    /**
     * 改变当前显示的Fragment的方法
     *
     * @param list       配置数据集合
     * @param selectBean 选中的Item的实体类
     * @param manager    Fragment管理器
     */
    private void changeShowFragment(int layout, List<BottomSelectBean> list, BottomSelectBean selectBean, FragmentManager manager) {
        if (manager == null) return;
        FragmentTransaction transaction = manager.beginTransaction();
        if (selectBean.getFragment() != null) {
            for (int i = 0; i < list.size(); i++) {
                if (selectBean.getTitle().equals(list.get(i).getTitle())) {
                    if (list.get(i).isAdd()) {
                        if (list.get(i).getFragment() != null)
                            transaction.show(list.get(i).getFragment());
                    } else {
                        transaction.add(layout, list.get(i).getFragment());
                        list.get(i).setAdd(true);
                        if (list.get(i).getFragment() != null)
                            transaction.show(list.get(i).getFragment());
                    }
                } else {
                    if (list.get(i).getFragment() != null)
                        transaction.hide(list.get(i).getFragment());
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getFragment() != null) transaction.hide(list.get(i).getFragment());
            }
        }
        transaction.commit();
    }


    /**
     * 为各个Item添加Fragment的方法。
     *
     * @param layout
     * @param manager
     * @param list
     */
    private void addFragmentForItem(int layout, FragmentManager manager, List<BottomSelectBean> list) {
        Log.i("wsy", list.toString());
        FragmentTransaction transaction = manager.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelect() && !list.get(i).isAdd()) {
                transaction.add(layout, list.get(i).getFragment());
                list.get(i).setAdd(true);
                transaction.commit();
                return;
            }
        }
    }

    public void showRedIcon(int position) {
        if (list.get(position).getRedIcon() != 0) {
            list.get(position).getImageView().setImageResource(list.get(position).getRedIcon());
        }
    }

    public void addDefaultView(int position, FragmentManager manager,int layout) {
        if (list.get(position).getFragment() != null) {
            if (!list.get(position).isAdd()) {
                FragmentTransaction ft = manager.beginTransaction();
                ft.add(layout,list.get(position).getFragment());
                list.get(position).setAdd(true);
                ft.hide(list.get(position).getFragment());
                ft.commit();
            }
        }
    }

    /**
     * 对外提供的Item的点击事件实现接口
     */
    public interface BottomSelectViewClickListener {
        void clickListener();
    }

    public BottomSelectViewClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(BottomSelectViewClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
