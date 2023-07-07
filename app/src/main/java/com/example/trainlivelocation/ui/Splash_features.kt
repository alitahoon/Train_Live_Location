package com.example.trainlivelocation.ui

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSplashFeaturesBinding
import com.example.trainlivelocation.utli.setFirstTimeOpenSharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import pub.devrel.easypermissions.PermissionRequest.Builder;


@AndroidEntryPoint
class Splash_features : Fragment() ,EasyPermissions.PermissionCallbacks {
    private val TAG: String?="Splash_features"
    private val REQUSET_CODE_Camera:Int=102
    private val REQUSET_CODE_location:Int=103
    private val REQUSET_CODE_background_location:Int=104
    private val REQUSET_CODE_Forground_location:Int=105
    private val REQUSET_CODE_IMAGE:Int=101


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
        setFirstTimeOpenSharedPreferences(requireContext(),true)
        setObservers()
        return binding!!.root
    }

    private fun setObservers() {
        splash_features_ViewModel.btnNextClick.observe(viewLifecycleOwner, Observer {
            if(it!!){
                when(splash_features_ViewModel.featuresCounter){
                    1 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.alarm)
                        binding!!.splashFeaturePara.setText("Set alarms to the station to notify the user when the train reaches it.")
                        binding!!.splashFeatureTitle.setText("Notification Alarm")
                        splash_features_ViewModel.incrementCounter()
                    }
                    2 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.news)
                        binding!!.splashFeaturePara.setText("news of the station daily")
                        binding!!.splashFeatureTitle.setText("Notification News")
                        splash_features_ViewModel.incrementCounter()
                    }
                    3 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.notification)
                        binding!!.splashFeaturePara.setText("Show description about the station when the train reaches it")
                        binding!!.splashFeatureTitle.setText("Notification ")
                        splash_features_ViewModel.incrementCounter()
                    }
                    4 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.post)
                        binding!!.splashFeaturePara.setText("When user miss anything or someone missed in the train you can share a post to find it.")
                        binding!!.splashFeatureTitle.setText("Notification Post")
                        splash_features_ViewModel.incrementCounter()
                    }
                    5 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.location_track)
                        binding!!.splashFeaturePara.setText("Show the train location track between station.")
                        binding!!.splashFeatureTitle.setText("Notification Location Track")
                        splash_features_ViewModel.incrementCounter()
                    }
                    6 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.chat)
                        binding!!.splashFeaturePara.setText("Allows users to communicate with each other.")
                        binding!!.splashFeatureTitle.setText("Notification Chat")
                        splash_features_ViewModel.incrementCounter()
                    }
                    7 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.emergamcy)
                        binding!!.splashFeaturePara.setText("Allows users to communicate with doctors in the same train.")
                        binding!!.splashFeatureTitle.setText("Notification Emergamcy")
                        splash_features_ViewModel.incrementCounter()
                    }
                    8 ->{
                        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION
                        ,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION)
                        requestPermissions(requireActivity(), 123, *permissions)
                    }
                }
            }
        })
    }

    fun requestPermissions(activity: Activity, requestCode: Int, vararg perms: String) {
        EasyPermissions.requestPermissions(
            Builder(activity, requestCode, *perms)
                .setRationale("Permissions")
                .setPositiveButtonText("Ok")
                .setNegativeButtonText("Cancel")
                .build()
        )
    }


    companion object {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i(TAG,"onPermissionsGranted")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.i(TAG,"onPermissionsDenied")
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