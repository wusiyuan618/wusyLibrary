package com.wusy.wusylibrary.view.moduleComponents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wusy.wusylibrary.R;
import com.wusy.wusylibrary.base.BaseRecyclerAdapter;
import com.wusy.wusylibrary.util.ImageLoaderUtil;

import java.io.File;

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
        SettingViewHolder holder = new SettingViewHolder(LayoutInflater.from(
                getContext()).inflate(R.layout.item_settingview, parent, false));
        return holder;
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof SettingViewHolder){
            SettingViewHolder moduleViewHolder= (SettingViewHolder) holder;
            moduleViewHolder.tv_title.setText(getList().get(position).getTitle());
            moduleViewHolder.tv_detail.setText(getList().get(position).getContent());
            if(getList().get(position).getImageResource()!=0){
                moduleViewHolder.img.setImageResource(getList().get(position).getImageResource());
            }else{
                ImageLoaderUtil.getInstance(getContext()).loadingImage(getList().get(position).getImageUrl(),moduleViewHolder.img);
            }
            moduleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModuleViewBean bean=getList().get(position);
                    if(bean.getOnModuleViewItemClickListener()!=null){
                        bean.getOnModuleViewItemClickListener().itemClick();
                    }
                    if(bean.getC()!=null){
                        Intent intent=new Intent(getContext(),bean.getC());
                        if(getList().get(position).getBundle()!=null) intent.putExtras(getList().get(position).getBundle());
                        getContext().startActivity(intent);
                    }else if(position==2){
                        try {
                            clearWebViewCache();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(),"清除成功！",Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(getContext(),"建设中",Toast.LENGTH_SHORT).show();

                }
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

    public void clearWebViewCache(){

        //清理Webview缓存数据库
        try {
            getContext().deleteDatabase("webview.db");
            getContext().deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getContext().getFilesDir().getAbsolutePath());
        Log.e(TAG, "appCacheDir path="+appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getContext().getCacheDir().getAbsolutePath()+"/webviewCache");
        Log.e(TAG, "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }
}
