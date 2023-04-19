package com.example.trainlivelocation.utli

import androidx.viewpager2.widget.ViewPager2

class ViewPager2PageChangeCallback(private val listener: (Int) -> Unit) :
    ViewPager2.OnPageChangeCallback() {

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        when (position) {
            0 -> listener.invoke(5) // 0th element is a fake element in your list. When 0th element is opened(from 1 to 0) automatically open last element(5th index)
            6 -> listener.invoke(1) // 6th element is a fake element in your list. When 6th element is opened(from 5 to 6) automatically open first element(1th index)
        }
    }
}