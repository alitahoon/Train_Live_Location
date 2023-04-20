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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.bumptech.glide.Glide
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivityMainBinding
import com.example.trainlivelocation.databinding.HeaderNavMenuLayoutBinding
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel? by viewModels()
    private val TAG: String? = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor =
            resources.getColor(R.color.DarkPrimaryColor)
        setBottomBarIcons()
        binding.bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                Log.i(TAG, position.toString())
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {

            }
        })
        mainActivityViewModel!!.getUserDataFromsharedPreference()
        setObservers()
        binding.mainActivityBtnDrawerMenu.setOnClickListener {
            binding.mainActivityDrwerLayout.openDrawer(GravityCompat.START)
        }
        //handle navbar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        navController.
        addOnDestinationChangedListener(object :NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                when(destination.id){
                    R.id.trainLocationInMap->{
                        binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                        binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                        binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                        binding.mainActivityFragmentHeaderNavFrName.setText("Train Location")
                    }
                    R.id.posts2->{
                        binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                        binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                        binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                        binding.mainActivityFragmentHeaderNavFrName.setText("Posts")                    }
                    R.id.shareLocationFeature->{
                        binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                        binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                        binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                        binding.mainActivityFragmentHeaderNavFrName.setText("Share Location")                    }
                    R.id.trackLocationFeature->{
                        binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                        binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                        binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                        binding.mainActivityFragmentHeaderNavFrName.setText("Track Location")                    }
                    R.id.home2->{
                        binding.mainActivityLayoutAfterLoading.setVisibility(View.VISIBLE)
                        binding.mainActivityBtnDrawerMenu.setVisibility(View.VISIBLE)
                        binding.mainActivityFragmentHeaderNav.setVisibility(View.GONE)
                    }
                }
            }

        })



        setContentView(binding.root)
    }


    private fun setObservers() {
        mainActivityViewModel!!.menuBtnClicked.observe(this) {
            if (it == true) {
                toast("hhhh")
                binding.mainActivityDrwerLayout.openDrawer(GravityCompat.START)
            }
        }

        mainActivityViewModel!!.userData.observe(this) {
            if (it != null) {

                val menuHeader = binding.mainActivityNavigationView.getHeaderView(0)
                val nametxt =
                    menuHeader.findViewById(R.id.nav_menu_header_profile_name) as TextView
                val joptxt =
                    menuHeader.findViewById(R.id.nav_menu_header_profile_jop) as TextView

                nametxt.setText(it!!.name)
                joptxt.setText(it!!.jop)
                binding.mainActivityCiProfileName.setText(it!!.name)
                binding.mainActivityCiProfileJop.setText(it!!.jop)
                mainActivityViewModel!!.getProfileImage("profileImages/+20${it.phone}")
            }
        }


        mainActivityViewModel!!.userProfileImageUri.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                    binding.mainActivityFragmentHeaderNav.setVisibility(View.GONE)
                    binding.mainActivityLayoutBeforeLoading.setVisibility(View.VISIBLE)
                }
                is Resource.Success -> {
                    val menuHeader = binding.mainActivityNavigationView.getHeaderView(0)
                    val ProfileImage =
                        menuHeader.findViewById(com.example.trainlivelocation.R.id.nav_menu_header_profile_image) as ImageView

                    binding.mainActivityLayoutAfterLoading.setVisibility(View.VISIBLE)
                    binding.mainActivityLayoutBeforeLoading.setVisibility(View.GONE)
                    Glide.with(this)
                        .load(it.data)
                        .into(binding.mainActivityCiProfileImage)
                    Glide.with(this)
                        .load(it.data)
                        .into(ProfileImage)

                }
                is Resource.Failure -> {
                    Log.e(TAG, "Failure:On Loading profile Image -> ${it.error}")
                }
                else -> {
                    Log.e(TAG, "Failure:On Loading profile Image -> else brunch${it}")
                }
            }
        }

    }

    private fun setBottomBarIcons() {
        binding.bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        binding.bottomNavigationBar
            .addItem(BottomNavigationItem(R.drawable.notifications, "Notification"))
            .setActiveColor(R.color.white)
            .addItem(BottomNavigationItem(R.drawable.home, "Home")).setActiveColor(R.color.white)
            .addItem(BottomNavigationItem(R.drawable.location_icon, "Location"))
            .setActiveColor(R.color.white)
            .setFirstSelectedPosition(1)
            .initialise()
    }


}