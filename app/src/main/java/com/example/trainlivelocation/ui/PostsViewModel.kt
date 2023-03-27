package com.example.trainlivelocation.ui

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class PostsViewModel :ViewModel(){
    var pageChangeListener: OnPageChangeListener? = null
    var pagerAdapter: PostsSomeFragmentPagerAdapter? = null



}