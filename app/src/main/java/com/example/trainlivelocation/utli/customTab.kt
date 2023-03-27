package com.example.trainlivelocation.utli

import com.example.trainlivelocation.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class customTab(context: Context) : TabLayout(context) {

    override fun setupWithViewPager(viewPager: ViewPager?) {
        super.setupWithViewPager(viewPager)
        Log.i("TAG", "setupWithViewPager");
        if (viewPager?.getAdapter() == null) {
            return;
        }
        createTabIcons();
    }

    private fun createTabIcons() {
        Log.i("TAG", "createTabIcons")
        val tabOne = LayoutInflater.from(context).inflate(R.layout.post_custom_tab, null) as LinearLayout
        (tabOne.findViewById<View>(R.id.post_tab_title) as TextView).text = "Critical"
        getTabAt(0)!!.customView = tabOne
        val tabTwo = LayoutInflater.from(context).inflate(R.layout.post_custom_tab, null) as LinearLayout
        (tabOne.findViewById<View>(R.id.post_tab_title) as TextView).text = "none Critical"
        getTabAt(1)!!.customView = tabTwo

    }
}