package com.wusy.wusyproject

import com.wusy.wusylibrary.base.BaseActivity

import com.wusy.wusylibrary.view.CarouselView
import kotlinx.android.synthetic.main.activity_home.*


class MainActivity : BaseActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_home
    }

    override fun findView() {

    }

    override fun init() {
        var list = ArrayList<CarouselView.CarouselBean>()
        list.add(CarouselView.CarouselBean(R.mipmap.ic_launcher))
        list.add(CarouselView.CarouselBean(R.mipmap.ic_launcher))

        carouselView.init(list, CarouselView.ANIM_LEFTLEAVE)
        carouselView.setIsRunningCarousel(false)
    }

}
