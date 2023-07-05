package com.example.trainlivelocation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentSplashFeaturesBinding
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AppSettingsDialog.Builder




@AndroidEntryPoint
class Splash_features : Fragment() {
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
                    }
                    2 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.news)
                        binding!!.splashFeaturePara.setText("news of the station daily")
                        binding!!.splashFeatureTitle.setText("Notification News")
                    }
                    3 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.notification)
                        binding!!.splashFeaturePara.setText("Show description about the station when the train reaches it")
                        binding!!.splashFeatureTitle.setText("Notification ")
                    }
                    4 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.post)
                        binding!!.splashFeaturePara.setText("When user miss anything or someone missed in the train you can share a post to find it.")
                        binding!!.splashFeatureTitle.setText("Notification Post")
                    }
                    5 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.location_track)
                        binding!!.splashFeaturePara.setText("Show the train location track between station.")
                        binding!!.splashFeatureTitle.setText("Notification Location Track")
                    }
                    6 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.chat)
                        binding!!.splashFeaturePara.setText("Allows users to communicate with each other.")
                        binding!!.splashFeatureTitle.setText("Notification Chat")
                    }
                    7 -> {
                        binding!!.splashFeatureLotti.setAnimation(R.raw.emergamcy)
                        binding!!.splashFeaturePara.setText("Allows users to communicate with doctors in the same train.")
                        binding!!.splashFeatureTitle.setText("Notification Emergamcy")
                    }
                }
            }
        })
    }

    fun requestPermissions(){
        EasyPermissions.requestPermissions(
            Builder(this, RC_CAMERA_AND_LOCATION, perms)
                .setRationale("Permissions")
                .setPositiveButtonText("Ok")
                .setNegativeButtonText("Cancel")
                .build()
        )
    }

//    private fun showDialog(permissions: String, name: String, requestCode: Int) {
//        val builder= AlertDialog.Builder(activity)
//        builder.apply {
//            setMessage("Permission to access your $name is required to use this app ")
//            setTitle("Permission required")
//            setPositiveButton("OK"){dialog,which ->
//                ActivityCompat.requestPermissions(activity, arrayOf(permissions),requestCode)
//            }
//            val dialog=builder.create()
//            dialog.show()
//        }
//    }
//
//    private fun checkSelfPermissions(permissions:String,name:String,requestCode: Int){
//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            when{
//                ContextCompat.checkSelfPermission(activity,permissions)== PackageManager.PERMISSION_GRANTED->{
//                    Toast.makeText(activity,"$name permissin granted", Toast.LENGTH_SHORT).show()
//                }
//                shouldShowRequestPermissionRationale(permissions)-> showDialog(permissions,name,requestCode)
//                else-> ActivityCompat.requestPermissions(activity, arrayOf(permissions),requestCode)
//            }
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        fun innerCheck(name: String){
//            if (grantResults.isNotEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(activity,"$name permission Refused",Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(activity,"$name permission Accepted",Toast.LENGTH_SHORT).show()
//                if (requestCode==REQUSET_CODE_IMAGE){
//                }
//            }
//        }
//        when (requestCode){
//            REQUSET_CODE_IMAGE->innerCheck("Read External Storage")
//            REQUSET_CODE_Camera->innerCheck("open Camera")
//            REQUSET_CODE_location->innerCheck("Access location ")
//            REQUSET_CODE_background_location->innerCheck("Access background location ")
//            REQUSET_CODE_Forground_location->innerCheck("Access Forground Services ")
//        }
//
//    }

    companion object {
    }
}