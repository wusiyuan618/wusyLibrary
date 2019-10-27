package com.wusy.wusyproject.BottomViewTest;

import android.os.Bundle;

import com.wusy.wusylibrary.view.bottomSelect.BottomSelectBean;
import com.wusy.wusyproject.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by XIAO RONG on 2018/5/6.
 */

public class BottomViewDataUtil {
    private static BottomViewDataUtil mainDataUtil;
    private List<BottomSelectBean> bottomSelectBeanList;

    private BottomViewDataUtil() {

    }

    public static synchronized BottomViewDataUtil getInstance() {
        if (mainDataUtil == null) mainDataUtil = new BottomViewDataUtil();
        return mainDataUtil;
    }

    /**
     * 底部选择器构建数据
     *
     * @return
     */
    public List<BottomSelectBean> getBottomSelectData() {
        bottomSelectBeanList = new ArrayList<>();

        //首页构建数据
        BottomSelectBean home = new BottomSelectBean();
        home.setSelect(true);
        home.setTitle("首页");
        home.setNormalIcon(R.mipmap.icon_home_normal);
        home.setSelectIcon(R.mipmap.icon_home_selected);
        TestFragment fragment_main=new TestFragment();
        Bundle bundle_main=new Bundle();
        bundle_main.putString("text","我是首页");
        fragment_main.setArguments(bundle_main);
        home.setFragment(fragment_main);
        home.setListener(() -> {
        });
        //工具构建数据
        BottomSelectBean tool = new BottomSelectBean();
        tool.setSelect(false);
        tool.setTitle("工具");
        tool.setNormalIcon(R.mipmap.icon_home_normal);
        tool.setSelectIcon(R.mipmap.icon_home_selected);
        TestFragment fragment_tool=new TestFragment();
        Bundle bundle_tool=new Bundle();
        bundle_tool.putString("text","我是工具");
        fragment_tool.setArguments(bundle_tool);
        tool.setFragment(fragment_tool);
        tool.setListener(() -> {
        });
        //我的构建数据
        BottomSelectBean my = new BottomSelectBean();
        my.setSelect(false);
        my.setTitle("我的");
        my.setNormalIcon(R.mipmap.icon_home_normal);
        my.setSelectIcon(R.mipmap.icon_home_selected);
        TestFragment fragment_me=new TestFragment();
        Bundle bundle_me=new Bundle();
        bundle_me.putString("text","我是我的");
        fragment_me.setArguments(bundle_me);
        my.setFragment(fragment_me);
        my.setListener(() -> {
        });
        bottomSelectBeanList.add(home);
        bottomSelectBeanList.add(tool);
        bottomSelectBeanList.add(my);

        return bottomSelectBeanList;
    }
}
