package com.example.trainlivelocation.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSplashFeaturesBinding
import com.example.trainlivelocation.utli.setFirstTimeOpenSharedPreferences
import com.example.trainlivelocation.utli.showCustomToast
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import pub.devrel.easypermissions.PermissionRequest.Builder;


@AndroidEntryPoint
class Splash_features : Fragment(), EasyPermissions.PermissionCallbacks{
    private val TAG: String? = "Splash_features"
    private val REQUSET_CODE_Camera: Int = 102
    private val REQUSET_CODE_location: Int = 103
    private val REQUSET_CODE_background_location: Int = 104
    private val REQUSET_CODE_Forground_location: Int = 105
    private val REQUSET_CODE_IMAGE: Int = 101
    private val REQUSET_CODE_STORAGE: Int = 102


    private var binding: FragmentSplashFeaturesBinding? = null
    private val splash_features_ViewModel: Splash_features_ViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashFeaturesBinding.inflate(inflater, container, false)
            .apply {
                this.viewModel = splash_features_ViewModel
            }
        setObservers()
        return binding!!.root
    }

    private fun setObservers() {
        splash_features_ViewModel.btnNextClick.observe(viewLifecycleOwner, Observer {
            if (it!!) {
                when (splash_features_ViewModel.getFeaturesCounter()) {
                    1 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.alarm)
                        binding!!.splashFeatureLotti.playAnimation()
                        //binding!!.splashFeatureLotti.repeatCount = LottieDrawable.INFINITE
                        binding!!.splashFeaturePara.setText("Set alarms to the station to notify the user when the train reaches it.")
                        binding!!.splashFeatureTitle.setText("Alarm")
                        splash_features_ViewModel.incrementCounter()
                        Log.i(TAG, "${splash_features_ViewModel.getFeaturesCounter()}")
                    }
                    2 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.news)
                        binding!!.splashFeatureLotti.playAnimation()
                        //binding!!.splashFeatureLotti.repeatCount = LottieDrawable.INFINITE
                        binding!!.splashFeaturePara.setText("news of the station daily")
                        binding!!.splashFeatureTitle.setText("News")
                        Log.i(TAG, "${splash_features_ViewModel.getFeaturesCounter()}")
                        splash_features_ViewModel.incrementCounter()
                        Log.i(TAG, "${splash_features_ViewModel.getFeaturesCounter()}")
                    }
                    3 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.notification)
                        binding!!.splashFeatureLotti.playAnimation()
                        //binding!!.splashFeatureLotti.repeatCount = LottieDrawable.INFINITE
                        binding!!.splashFeaturePara.setText("Show description about the station when the train reaches it")
                        binding!!.splashFeatureTitle.setText("Notification ")
                        splash_features_ViewModel.incrementCounter()
                    }
                    4 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.post)
                        binding!!.splashFeatureLotti.playAnimation()
                        //binding!!.splashFeatureLotti.repeatCount = LottieDrawable.INFINITE
                        binding!!.splashFeaturePara.setText("When user miss anything or someone missed in the train you can share a post to find it.")
                        binding!!.splashFeatureTitle.setText("Post")
                        splash_features_ViewModel.incrementCounter()
                    }
//                    5 -> {
//                        binding!!.splashFeatureLotti.setAnimation(R.raw.location_track)
//                        binding!!.splashFeatureLotti.playAnimation()
//                        //binding!!.splashFeatureLotti.repeatCount = LottieDrawable.INFINITE
//                        binding!!.splashFeaturePara.setText("Show the train location track between station.")
//                        binding!!.splashFeatureTitle.setText("Location Track")
//                        splash_features_ViewModel.incrementCounter()
//                    }
                    5 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.chat)
                        binding!!.splashFeatureLotti.playAnimation()
                        //binding!!.splashFeatureLotti.repeatCount = LottieDrawable.INFINITE
                        binding!!.splashFeaturePara.setText("Allows users to communicate with each other.")
                        binding!!.splashFeatureTitle.setText("Chat")
                        splash_features_ViewModel.incrementCounter()
                    }
                    6 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.emergamcy)
                        binding!!.splashFeatureLotti.playAnimation()
                        //binding!!.splashFeatureLotti.repeatCount = LottieDrawable.INFINITE
                        binding!!.splashFeaturePara.setText("Allows users to communicate with doctors in the same train.")
                        binding!!.splashFeatureTitle.setText("Emergamcy")
                        splash_features_ViewModel.incrementCounter()
                    }
                    7 -> {
                       if (hasPermissions()){
                           setFirstTimeOpenSharedPreferences(requireContext(),false)
                           findNavController().navigate(R.id.action_splash_features_to_splash2)
                       }else{
                           requsetPermissions()
                       }
                    }
                }
            }
        })
    }

    companion object {
    }

    fun requsetPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "You should accept this permissions to use our App ",
            REQUSET_CODE_location,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }
    fun hasPermissions():Boolean{
        return EasyPermissions.hasPermissions(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        setFirstTimeOpenSharedPreferences(requireContext(),false)
        findNavController().navigate(R.id.action_splash_features_to_splash2)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requsetPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward the result to EasyPermissions for handling
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}