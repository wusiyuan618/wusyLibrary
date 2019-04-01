package com.wusy.wusylibrary.view.moduleComponents;
import com.wusy.wusylibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DalaR on 2017/11/27.
 */

public class ModuleViewTestData {
    public static List<ModuleViewBean> getModuleViewTextData_music(){
        List<ModuleViewBean> list=new ArrayList<>();
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"古典音乐","古典音乐真牛逼",null));
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"名族音乐","名族音乐真牛逼",null));
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"摇滚音乐","摇滚音乐真牛逼",null));
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"流行音乐","流行音乐真牛逼",null));
        return list;
    }
    public static List<ModuleViewBean> getModuleViewTextData_moive(){
        List<ModuleViewBean> list=new ArrayList<>();
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"喜剧","搞笑、欢乐",null));
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"爱情","感人、剧情",null));
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"动作","精彩、打斗",null));
        list.add(new ModuleViewBean(R.mipmap.test_defulet,"爱情动作","18禁。。。。",null));
        return list;
    }
}
