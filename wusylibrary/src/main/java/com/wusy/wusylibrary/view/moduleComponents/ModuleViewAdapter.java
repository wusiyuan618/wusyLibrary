package com.wusy.wusylibrary.view.moduleComponents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.base.BaseRecyclerAdapter;
import com.wusy.wusylibrary.util.ImageLoaderUtil;

/**
 * Created by DalaR on 2017/11/27.
 */

public class ModuleViewAdapter extends BaseRecyclerAdapter<ModuleViewBean> {

    private static final String TAG = "ModuleViewAdapter";

    public ModuleViewAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        ModuleViewHolder holder = new ModuleViewHolder(LayoutInflater.from(
                getContext()).inflate(R.layout.item_tool, parent, false));
        return holder;
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ModuleViewHolder){
            ModuleViewHolder moduleViewHolder= (ModuleViewHolder) holder;
            moduleViewHolder.tv_title.setText(getList().get(position).getTitle());
            if(getList().get(position).getImageResource()!=0){
                moduleViewHolder.img.setImageResource(getList().get(position).getImageResource());
            }else{
                ImageLoaderUtil.getInstance(getContext()).loadingImage(getList().get(position).getImageUrl(),moduleViewHolder.img);
            }
            moduleViewHolder.itemView.setOnClickListener(view -> {
                ModuleViewBean bean=getList().get(position);
                if(bean.getOnModuleViewItemClickListener()!=null){
                    bean.getOnModuleViewItemClickListener().itemClick();
                }
                if(bean.getC()!=null){
                    Intent intent=new Intent(getContext(),bean.getC());
                    if(getList().get(position).getBundle()!=null) intent.putExtras(getList().get(position).getBundle());
                    getContext().startActivity(intent);
                }else Toast.makeText(getContext(),"建设中",Toast.LENGTH_SHORT).show();

            });
        }
    }
    public interface OnModuleViewItemClickListener{
        void itemClick();
    }
    public void updateModuleItemText(String text,int position){
        getList().get(position).setTitle(getList().get(position).getTitle()+" ("+text+")");
        notifyItemChanged(position);
    }
}
