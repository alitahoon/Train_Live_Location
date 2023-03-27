package com.example.trainlivelocation.utli

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener
import com.google.android.material.tabs.TabLayout


@BindingAdapter("setUpWithViewpager")
fun setUpWithViewpager(tabLayout: TabLayout, viewPager: ViewPager) {
    viewPager.addOnAdapterChangeListener(OnAdapterChangeListener { viewPager, oldAdapter, newAdapter ->
        if (oldAdapter == null && (newAdapter == null || newAdapter.count == 0)) {
            // this function will helpful when
            // we don't create viewpager immediately
            // when view created (this mean we create
            // will pager after a period time)
            return@OnAdapterChangeListener
        }
        tabLayout.setupWithViewPager(viewPager)
    })
}