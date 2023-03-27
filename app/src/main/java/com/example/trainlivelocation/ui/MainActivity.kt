package com.example.trainlivelocation.ui

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivityMainBinding
import com.example.trainlivelocation.databinding.ActivitySplashBinding
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG:String?="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = resources.getColor(R.color.basicColor)
        setBottomBarIcons()
        binding.bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                Log.i(TAG,position.toString())
            }
            override fun onTabUnselected(position: Int) {

            }
            override fun onTabReselected(position: Int) {

            }
        })

        setContentView(binding.root)

    }
    private fun setBottomBarIcons() {
        binding.bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        binding.bottomNavigationBar
            .addItem(BottomNavigationItem(R.drawable.notifications, "Notification")).setActiveColor(R.color.white)
            .addItem(BottomNavigationItem(R.drawable.home, "Home")).setActiveColor(R.color.white)
            .addItem(BottomNavigationItem(R.drawable.location_icon, "Location")).setActiveColor(R.color.white)
            .setFirstSelectedPosition(1)
            .initialise()
    }

}