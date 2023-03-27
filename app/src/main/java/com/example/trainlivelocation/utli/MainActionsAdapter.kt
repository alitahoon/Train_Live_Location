package com.example.trainlivelocation.utli

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.trainlivelocation.ui.AllPosts
import com.example.trainlivelocation.ui.CriticalPost
import javax.inject.Inject


class MainActionsAdapter {

    @Inject
    lateinit var mContext:Context

    private val critcal = 0
    private val nonCritcal = 1


    private val TABS = intArrayOf(critcal, nonCritcal)




    fun getItem(position: Int): Fragment? {
        when (TABS[position]) {
            critcal -> return CriticalPost()
            nonCritcal -> return AllPosts()
        }
        return null
    }

    fun getCount(): Int {
        return TABS.size
    }

    fun getPageTitle(position: Int): CharSequence? {
        when (TABS[position]) {
            critcal -> return mContext?.getResources()?.getString(com.example.trainlivelocation.R.string.critcal)
            nonCritcal -> return mContext?.getResources()?.getString(com.example.trainlivelocation.R.string.nonCritcal)
        }
        return null
    }
}