package com.example.trainlivelocation.ui

import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class PostsViewModel :ViewModel(){
    var pageChangeListener: OnPageChangeListener? = null
    var pagerAdapter: SomeFragmentPagerAdapter? = null
}