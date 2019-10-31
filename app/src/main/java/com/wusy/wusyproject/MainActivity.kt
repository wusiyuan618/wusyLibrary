package com.wusy.wusyproject

import com.wusy.wusylibrary.base.BaseActivity

import com.wusy.wusylibrary.view.CarouselView
import kotlinx.android.synthetic.main.activity_home.*


class MainActivity :BaseActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_home
    }

    override fun findView() {
    }

    override fun init() {
        var list=ArrayList<CarouselView.CarouselBean>()
        list.add(CarouselView.CarouselBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572006936821&di=5a419685258e546f8ed8f21d55a666b3&imgtype=0&src=http%3A%2F%2Fwww.china2000.org%2FUploadFiles%2F2016%2F2%2F2016122611550134319.jpg"))
        list.add(CarouselView.CarouselBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572006953048&di=cb665b7f38eb525f47a914d46a183226&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20180518%2Fa3f4a31ff0864f9fb0b2a9fca4af615d_th.jpg"))
        list.add(CarouselView.CarouselBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572006986117&di=f7e1ebe7ce23e790352acb0cf18e6cf3&imgtype=0&src=http%3A%2F%2Fwww.bodawb.com%2Fres%2Fupload%2FBdNews%2F20161101%2F2016KAOEMi2MxS.jpg"))
//        list.add(CarouselView.CarouselBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572006999139&di=a8cefa55d80a3a0d276b01bcb0bb0225&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F00678rgojw1fay1f43qfgj317r0ryjw5.jpg"))
        carouselView.init(list, CarouselView.ANIM_LEFTLEAVE)
        carouselView.setIsRunningCarousel(false)
    }

}
