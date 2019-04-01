package com.wusy.wusylibrary.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIAO RONG on 2018/4/20.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> list;
    private Context context;
    private onRecyclerItemClickLitener onRecyclerItemClickLitener;
    public BaseRecyclerAdapter(Context context){
        this.context=context;
        list=new ArrayList<>();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onMyCreateViewHolder(parent,viewType);
    }

    public abstract RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(onRecyclerItemClickLitener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerItemClickLitener.onRecyclerItemClick(holder,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onRecyclerItemClickLitener.onRecyclerItemLongClick(holder,position);
                    return false;
                }
            });
        }
        onMyBindViewHolder(holder,position);
    }

    public abstract void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     *   指定位置添加item
     */
    public void addData(int position,T data) {
        list.add(position, data);
        notifyItemInserted(position);
    }


    /**
     * 指定位置移除item
     */
    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public interface onRecyclerItemClickLitener {
        void onRecyclerItemClick(RecyclerView.ViewHolder view, int position);
        void onRecyclerItemLongClick(RecyclerView.ViewHolder view , int position);
    }
    public void setOnRecyclerItemClickLitener(onRecyclerItemClickLitener onRecyclerItemClickLitener)
    {
        this.onRecyclerItemClickLitener = onRecyclerItemClickLitener;
    }

    /**
     * 为RecyclerView添加上拉加载更多的实现接口
     * firstVisibleItem=页面显示的第一个Item的Position
     * visibleItemCount=页面显示的Item的数量
     * totalItemCount=总共的Item的数量
     * previousTotal=与totalItemCount做比较，用于判断是否可以执行加载
     * loading=是否处于加载中
     * currentPage=页数
     * firstVisibleItem+visibleItemCount=totalItemCount 即拉倒了最底部。
     * 当页面刷新时，必须将previousTotal变为0.否则无法执行上拉加载
     */
    public abstract static class LoadMoreRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        private int previousTotal = 0;
        private boolean loading = true;
        int firstVisibleItem, visibleItemCount, totalItemCount;
        private int currentPage = 1;

        private LinearLayoutManager mLinearLayoutManager;
        public LoadMoreRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }
        }
        public abstract void onLoadMore(int currentPage);
        public void clearPreviousTotal(){
            previousTotal=0;
        }
    }
}
