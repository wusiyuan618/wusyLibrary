package com.wusy.wusyproject.RecyclerViewTest

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.wusy.wusylibrary.base.BaseRecyclerAdapter
import com.wusy.wusyproject.R

class RecyclerTestAdapter(context: Context):BaseRecyclerAdapter<String>(context){
    override fun onMyCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerTestViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_recyclerview_test, parent, false)
        )
    }

    override fun onMyBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        if(holder is RecyclerTestViewHolder){
            var thisHolder=holder as RecyclerTestViewHolder
            thisHolder.tv.text=list[position]
        }
    }
    inner class RecyclerTestViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tv)
    }
}
