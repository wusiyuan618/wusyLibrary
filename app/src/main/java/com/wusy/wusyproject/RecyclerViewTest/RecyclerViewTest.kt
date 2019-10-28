package com.wusy.wusyproject.RecyclerViewTest

import android.support.v7.widget.LinearLayoutManager
import com.wusy.wusylibrary.base.BaseActivity
import com.wusy.wusyproject.R
import kotlinx.android.synthetic.main.activity_recyclertest.*

class RecyclerViewTest:BaseActivity(){
    var pageIndex = 0
    lateinit var adapter:RecyclerTestAdapter

    override fun getContentViewId(): Int {
        return R.layout.activity_recyclertest
    }

    override fun findView() {

    }

    override fun init() {
        recyclerView.layoutManager=LinearLayoutManager(this)
        adapter=RecyclerTestAdapter(this)
        recyclerView.adapter=adapter
        adapter.setEmptyText("更改后的信息")
        adapter.setEmptyImage(R.mipmap.ic_launcher)
        refreshLayout.setOnRefreshListener {
            adapter.list= getList(isClear = true, isShowAnim = false) as MutableList<String>?
            adapter.notifyDataSetChanged()
            refreshLayout.finishRefresh()
        }
        refreshLayout.setOnLoadMoreListener {
            pageIndex++
            (adapter.list as ArrayList<String>).addAll(getList(isClear = false, isShowAnim = false))
            adapter.notifyDataSetChanged()
            refreshLayout.finishLoadMore()

        }
    }
    private fun getList(isClear: Boolean, isShowAnim: Boolean):ArrayList<String> {
        if (isClear) {
            pageIndex = 0
            adapter.list.clear()
        }
        return ArrayList<String>().apply {
            for (i in 0..20){
                add("i am data $i")
            }
        }
    }

}
