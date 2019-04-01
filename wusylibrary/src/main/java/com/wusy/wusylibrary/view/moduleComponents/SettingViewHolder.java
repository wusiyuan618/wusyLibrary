package com.wusy.wusylibrary.view.moduleComponents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wusy.wusylibrary.R;

/**
 * Created by DalaR on 2017/11/27.
 */

public class SettingViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_title,tv_detail;
    public ImageView img;
    public SettingViewHolder(View itemView) {
        super(itemView);
        img= (ImageView) itemView.findViewById(R.id.item_setting_img);
        tv_title= (TextView) itemView.findViewById(R.id.item_setting_title);
        tv_detail= (TextView) itemView.findViewById(R.id.item_setting_detail);
    }
}
