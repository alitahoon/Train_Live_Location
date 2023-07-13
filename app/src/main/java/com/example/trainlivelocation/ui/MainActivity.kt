package com.example.trainlivelocation.ui

import Resource
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.bumptech.glide.Glide
import com.example.domain.entity.*
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.ActivityMainBinding
import com.example.trainlivelocation.databinding.HeaderNavMenuLayoutBinding
import com.example.trainlivelocation.utli.HomeMapListener
import com.example.trainlivelocation.utli.OnBackPressedListener
import com.example.trainlivelocation.utli.getuserModelFromSharedPreferences
import com.example.trainlivelocation.utli.toast
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,HomeMapListener{
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel? by viewModels()
    private val TAG: String? = "MainActivity"
    var navController:NavController?=null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewmodel = mainActivityViewModel
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor =
            resources.getColor(R.color.DarkPrimaryColor)
        setBottomBarIcons()
        setHeaderData()
        setObservers()
        val headerBinding = DataBindingUtil.bind<HeaderNavMenuLayoutBinding>(binding.mainActivityNavigationView.getHeaderView(0))
        headerBinding?.context = this
        binding.mainActivityBtnDrawerMenu.setOnClickListener {
            binding.mainActivityDrwerLayout.openDrawer(GravityCompat.START)
        }
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.enter) // Specify the animation resource for enter animation
            .setExitAnim(R.anim.exit) // Specify the animation resource for exit animation
            .setPopEnterAnim(R.anim.pop_enter) // Specify the animation resource for pop enter animation
            .setPopExitAnim(R.anim.pop_exit) // Specify the animation resource for pop exit animation
            .build()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
         navController = navHostFragment.navController
        //handle navView
        binding.mainActivityNavigationView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.home_menu_profile -> {
                        val bundle = Bundle()
                        bundle.putParcelable("userModel", userModel)
                        Log.i(TAG, "$userModel")
                        navController!!.navigate(R.id.userProfile, bundle)
                        binding.mainActivityDrwerLayout.closeDrawer(GravityCompat.START);

                    }

                    R.id.home_menu_alarms ->{
                        navController!!.navigate(R.id.alarms,null,navOptions)
                        binding.mainActivityDrwerLayout.closeDrawer(GravityCompat.START);
                    }

                    R.id.home_menu_Settings->{
                        setHeader("Settings")
                        navController!!.navigate(R.id.settings,null,navOptions)
                        binding.mainActivityDrwerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                return true
            }
        })


        //handle navbar
        navController!!.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                when (destination.id) {
                    R.id.trainLocationInMap -> {
                        setHeader("Train Location")
                    }
                    R.id.posts2 -> {
                        setHeader("Posts")
                    }
                    R.id.shareLocationFeature -> {
                        setHeader("Share Location")
                    }
                    R.id.shareLocationFeature -> {
                        setHeader("Track Location")
                    }
                    R.id.userProfile -> {
                        setHeader("Profile")
                    }
                    R.id.trackLocationFeature -> {
                        setHeader("Track Location")
                    }
                    R.id.tickets -> {
                        setHeader("Ticket")
                    }
                    R.id.emergency -> {
                        setHeader("Emergency")
                    }
                    R.id.doctorLocationInMap -> {
                        setHeader("Patient Location")
                    }
                    R.id.home2 -> {
                        setHeader("home")
                    }
                    R.id.passengers -> {
                        setHeader("Passengers")
                    }
                    R.id.news -> {
                        setHeader("News")
                    }
                }
            }

        })

        binding.bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                when (position) {
                    2 -> {
                        binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                        binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                        binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                        binding.mainActivityFragmentHeaderNavFrName.setText("Inbox")

                        val bundle = Bundle()
                        bundle.putParcelable("userModel", userModel)
                        Log.i(TAG, "$userModel")
                        navController!!.navigate(R.id.inbox, bundle, navOptions)
                    }
                    1 -> {
                        setHeader("home")
                        navController!!.navigate(R.id.home2, null, navOptions)
                    }
                    0 -> {
                        setHeader("Alarms")
                        navController!!.navigate(R.id.alarms, null, navOptions)

                    }
                }
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {

            }
        })

        //handle notification load fragments
        fragmentToload=intent.getStringExtra("FRAGMENT_NAME")
        if (fragmentToload!= null) {
            when (fragmentToload) {
                 "DoctorLocationInMap" -> {
                     setHeader("Emergency")
                     Log.i(TAG,"Emergency DoctorLocationInMap")
                    val doctorNotificationData=intent.getSerializableExtra("doctorNotification") as DoctorNotificationData
                    val bundle = Bundle()
                    bundle.putSerializable(
                        "patientLocation",
                        doctorNotificationData
                    )
                    navController!!.navigate(R.id.doctorLocationInMap, bundle)
                }
                "AddPostFragment" -> {
                    setHeader("Posts")
//                    val trainID=intent.getIntExtra("trainID",0)
//                    Log.i(TAG,"trainID : ${trainID}")
//                    val postCritical=intent.getBooleanExtra("critical",false)
                    val notificationModel = intent.getSerializableExtra("notificationModel")
                    val bundle = Bundle()
                    bundle.putSerializable("postNotificationModel", notificationModel)
//                    bundle.putInt("patientLocation",trainID!!)
//                    bundle.putBoolean("patientLocation",postCritical)
                    navController!!.navigate(R.id.posts2, bundle)
                }
                "NewPostComment" -> {
                    val notificationModel: AddPostCommentNotificationData =
                        intent.getSerializableExtra("notificationModel") as AddPostCommentNotificationData
                    Log.i(TAG,"notificationModel -->${notificationModel}")
                    var dialog = Add_post_comment(
                        PostModelResponse(
                            notificationModel.adminId,
                            notificationModel.content,
                            notificationModel.critical,
                            notificationModel.date,
                            notificationModel.id,
                            " ",
                            notificationModel.imgId,
                            notificationModel.trainNumber,
                            notificationModel.userId,
                            notificationModel.userName,
                            notificationModel.userPhone
                        )

                    )
                    dialog.show(supportFragmentManager, "Add_post_comment")
                }
            }

        } else {
            Log.i(TAG, "FRAGMENT_NAME from notification is null")
        }


        setContentView(binding.root)
    }

    fun checkBackToLogin(){
        // Show a dialog confirming the user's intention to go back
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to go back?")
            .setPositiveButton("Yes") { dialog, _ ->
                super.onBackPressed() // Proceed with the default back button behavior
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog and stay on the current fragment
            }
            .show()
    }

    override fun onBackPressed() {
        val currentFragment = navController!!.currentDestination?.id
        if (currentFragment == R.id.home2) {
            // Handle the back button behavior for the specific fragment
            // For example, show a dialog or perform a custom action
//            checkBackToLogin()
        } else {
            super.onBackPressed()
        }
    }

    private fun setObservers() {
        mainActivityViewModel!!.menuBtnClicked.observe(this) {
            if (it == true) {
                binding.mainActivityDrwerLayout.openDrawer(GravityCompat.START)
            }
        }

        mainActivityViewModel!!.btnBack.observe(this, Observer {
            if(it!!){
                onBackPressed()
                Log.i(TAG, "btnBack")
            }
        })

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

    companion object {
        private var userModel: UserResponseItem? = null
        private var fragmentToload:String?=null
    }

    private fun setBottomBarIcons() {
        binding.bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        binding.bottomNavigationBar
            .addItem(BottomNavigationItem(R.drawable.alarm_icon_notifiction, "Alarms"))
            .setActiveColor(R.color.white)
            .addItem(BottomNavigationItem(R.drawable.home, "Home")).setActiveColor(R.color.white)
            .addItem(BottomNavigationItem(R.drawable.inbox, "Inbox"))
            .setActiveColor(R.color.white)
            .setFirstSelectedPosition(1)
            .initialise()
    }

    fun setHeader(title: String?) {
        when (title) {
            "home" -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.VISIBLE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.GONE)
            }
            "Ticket" -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNav.setBackgroundColor(resources.getColor(R.color.PrimaryColor))
                binding.mainActivityFragmentHeaderNavFrName.setText(title)
            }
            "Emergency" -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNav.setBackgroundColor(resources.getColor(R.color.PrimaryColor))
                binding.mainActivityFragmentHeaderNavFrName.setText(title)
            }
            "Share Location" -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNav.setBackgroundColor(resources.getColor(R.color.PrimaryColor))
                binding.mainActivityFragmentHeaderNavFrName.setText(title)
            }
            "Track Location" -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNav.setBackgroundColor(resources.getColor(R.color.PrimaryColor))
                binding.mainActivityFragmentHeaderNavFrName.setText(title)
            }"Patient Location" -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNav.setBackgroundColor(resources.getColor(R.color.PrimaryColor))
                binding.mainActivityFragmentHeaderNavFrName.setText(title)
            }"Settings" -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNav.setBackgroundColor(resources.getColor(R.color.PrimaryColor))
                binding.mainActivityFragmentHeaderNavFrName.setText(title)
            }
                else -> {
                binding.mainActivityLayoutAfterLoading.setVisibility(View.GONE)
                binding.mainActivityBtnDrawerMenu.setVisibility(View.GONE)
                binding.mainActivityFragmentHeaderNav.setVisibility(View.VISIBLE)
                binding.mainActivityFragmentHeaderNavFrName.setText(title)
            }
        }

    }

    fun setHeaderData() {
        userModel = getuserModelFromSharedPreferences()
        if (userModel != null) {

            val menuHeader = binding.mainActivityNavigationView.getHeaderView(0)
            val nametxt =
                menuHeader.findViewById(R.id.nav_menu_header_profile_name) as TextView
            val joptxt =
                menuHeader.findViewById(R.id.nav_menu_header_profile_jop) as TextView

            nametxt.setText(userModel!!.name)
            joptxt.setText(userModel!!.jop)
            binding.mainActivityCiProfileName.setText(userModel!!.name)
            binding.mainActivityCiProfileJop.setText(userModel!!.jop)
            mainActivityViewModel!!.getProfileImage("profileImages/+20${userModel!!.phone}")
        }
    }

    override fun onMapOpened() {
        binding.homeActionBar.setVisibility(View.GONE)
//        window.statusBarColor =
//            resources.getColor(R.color.statusBarcolorOnOpenMap)
    }

    override fun OnMapClosed() {
        binding.homeActionBar.setVisibility(View.VISIBLE)
//        window.statusBarColor =
//            resources.getColor(R.color.DarkPrimaryColor)
         }

    override fun onMoveFromHome() {
        binding.homeActionBar.setVisibility(View.VISIBLE)
    }


}