package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.bumptech.glide.Glide
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivityMainBinding
import com.example.trainlivelocation.databinding.HeaderNavMenuLayoutBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel:MainActivityViewModel? by viewModels()
    private val TAG:String?="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = resources.getColor(com.example.trainlivelocation.R.color.basicColor)
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
        mainActivityViewModel!!.getUserDataFromsharedPreference()
        setObservers()

        setContentView(binding.root)

    }

    private fun setObservers() {
        mainActivityViewModel!!.menuBtnClicked.observe(this){
            if (it==true){
                Log.e(TAG,"menuBtnClicked")
                binding.mainActivityDrwerLayout.openDrawer(GravityCompat.START)
            }
        }

        mainActivityViewModel!!.userData.observe(this){
            if (it!= null){

                val menuHeader = binding.mainActivityNavigationView.getHeaderView(0)
                val nametxt=menuHeader.findViewById(com.example.trainlivelocation.R.id.nav_menu_header_profile_name) as TextView
                val joptxt=menuHeader.findViewById(com.example.trainlivelocation.R.id.nav_menu_header_profile_jop) as TextView

                nametxt.setText(it!!.name)
                joptxt.setText(it!!.jop)
                binding.mainActivityCiProfileName.setText(it!!.name)
                binding.mainActivityCiProfileJop.setText(it!!.jop)
                mainActivityViewModel!!.getProfileImage("profileImages/+20${it.phone}")
            }
        }


        mainActivityViewModel!!.userProfileImageUri.observe(this){
            when(it){
                is Resource.Loading->{
                    binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                    binding.mainActivityLayoutBeforeLoading.setVisibility(View.VISIBLE)
                }
                is Resource.Success->{
                    val menuHeader = binding.mainActivityNavigationView.getHeaderView(0)
                    val ProfileImage=menuHeader.findViewById(com.example.trainlivelocation.R.id.nav_menu_header_profile_image) as ImageView

                    binding.mainActivityLayoutAfterLoading.setVisibility(View.VISIBLE)
                    binding.mainActivityLayoutBeforeLoading.setVisibility(View.GONE)
                    Glide.with(this)
                        .load(it.data)
                        .into( binding.mainActivityCiProfileImage)
                    Glide.with(this)
                        .load(it.data)
                        .into(ProfileImage)

                }
                is Resource.Failure->{
                    Log.e(TAG,"Failure:On Loading profile Image -> ${it.error}")
                }
                else -> {
                    Log.e(TAG,"Failure:On Loading profile Image -> else brunch${it}")
                }
            }
        }

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