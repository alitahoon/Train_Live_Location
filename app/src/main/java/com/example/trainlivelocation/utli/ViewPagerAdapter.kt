package com.example.trainlivelocation.utli

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: FragmentActivity?) :
    FragmentStateAdapter(fragment!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    fun addFrag(fragment: Fragment?, title: String?) {
        mFragmentList.add(fragment!!)
        mFragmentTitleList.add(title!!)
    }

    override fun getItemCount(): Int =mFragmentList.size

    override fun createFragment(position: Int): Fragment =mFragmentList.get(position)


}