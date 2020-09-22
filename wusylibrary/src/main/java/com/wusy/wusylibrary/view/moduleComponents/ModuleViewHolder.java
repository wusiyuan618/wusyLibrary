package com.wusy.wusylibrary.view.moduleComponents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wusy.wusylibrary.R;

/**
 * Created by DalaR on 2017/11/27.
 */

public class ModuleViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_title,tv_content;
    public ImageView img;
    public RelativeLayout ll;
    public ModuleViewHolder(View itemView) {
        super(itemView);
        ll=  itemView.findViewById(R.id.moduleview_item_ll);
        img= itemView.findViewById(R.id.moduleview_item_img);
        tv_title=  itemView.findViewById(R.id.moduleview_item_tv_top);
    }
}
