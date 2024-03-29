package com.example.trainlivelocation.ui

import Resource
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.trainlivelocation.utli.LocationTrackBackgroundService
import com.example.domain.entity.LocationDetails
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentShareLocationBinding
import com.example.trainlivelocation.utli.Train_Dialog_Listener
import com.example.trainlivelocation.utli.displaySnackbarSuccess
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


@AndroidEntryPoint
class ShareLocationFeature : Fragment(), Train_Dialog_Listener {
    private var TAG: String? = "liveLocationFeatureFragment"
    private val args by navArgs<ShareLocationFeatureArgs>()

    private lateinit var binding: FragmentShareLocationBinding
    private val shareLocationViewModel: ShareLocationViewModel? by activityViewModels()

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        shareLocationViewModel?.stopLocationLiveUpdate()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        shareLocationViewModel!!.stopLocationLiveUpdate()
//        shareLocationViewModel?.stopLocationService(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareLocationBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = shareLocationViewModel
            }
        shareLocationViewModel?.setbaseActivity(requireActivity())
//        shareLocationViewModel?.startUpdate()
        setObservers()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        shareLocationViewModel!!.stopLocationLiveUpdate()
    }
    private fun setObservers() {
        //get user Location From GPS
        shareLocationViewModel!!.startSharingtrainLocation.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                }
                is Resource.Success->{
//                    locationbckgroundSharingService= Intent(requireActivity(),
//                        LocationTrackBackgroundService::class.java)
//                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
//                        ContextCompat.startForegroundService(requireContext(),locationbckgroundSharingService!!)
//                    }else{
//                        Log.e(TAG,"else brunch...")
//                    }
                }
                else -> {}
            }
        })


        shareLocationViewModel!!.sharedTrainLocation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.i(TAG, "Waiting for Sharing Location...")
                }
                is Resource.Success -> {
                    Log.i(TAG, "Location Sharing successfully...")
                }
                is Resource.Failure -> {
                    Log.i(TAG, "Location Shared Failed -> ${it.error}")
                }
                else -> {

                }
            }
        })

        shareLocationViewModel!!.btnShareTrainLocationClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var locationbckgroundSharingService= Intent(requireActivity(),
                    LocationTrackBackgroundService::class.java)
                if ( binding.shareLocationBtnShareTrainLocation.text.equals("Share Location")){
                    binding.shareLocationBtnShareTrainLocation.setBackgroundColor(resources.getColor(R.color.textAlarmColor))
                    binding.shareLocationBtnShareTrainLocation.setText(R.string.stop_sharing)
                    binding.shareLocationLottiSharing.visibility=View.VISIBLE
                    displaySnackbarSuccess(
                        requireContext(),
                        binding.root,
                        "Successfully Sharing Loctaion ",
                        R.raw.location_share_success,
                        R.color.PrimaryColor
                    )
//                    shareLocationViewModel!!.startSharing(args.userModel.id,binding.shareLocationTxtTrainId.text.toString().toInt())
                    locationbckgroundSharingService.putExtra("trainId",binding.shareLocationTxtTrainId.text.toString().toInt())
                    locationbckgroundSharingService.putExtra("userId",args.userModel.id)
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                        ContextCompat.startForegroundService(requireContext(),locationbckgroundSharingService!!)
                    }else{
                        Log.e(TAG,"else brunch...")
                    }
//                Log.i(TAG, "Get user location -> Success ${it!!.longitude},${it!!.latitude}")
                }else{
                    binding.shareLocationBtnShareTrainLocation.setBackgroundColor(resources.getColor(R.color.PrimaryColor))
                    binding.shareLocationBtnShareTrainLocation.setText("Share Location")
                    binding.shareLocationLottiSharing.visibility=View.GONE
//                    shareLocationViewModel!!.stopLocationLiveUpdate()
//                    shareLocationViewModel!!.stopLocationService(this)
                    requireActivity().stopService(locationbckgroundSharingService)
                }


            }
        })

        shareLocationViewModel!!.txtTrainIdClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var dialog = ChooseTrainDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
            }
        })
    }

    companion object {

    }

    @Subscribe
    fun getLocationLive(locationDetails: LocationDetails) {
        Log.e(
            TAG,
            locationDetails.latitude.toString() + "////" + locationDetails.longitude.toString()
        )
//        shareLocationViewModel?.uplaodLocationToApi(
//            locationDetails.longitude,
//            locationDetails.latitude,
//            args.userModel.id
//        )

    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding.shareLocationTxtTrainId.setText("${trainId!!}")
    }


//
////    override fun onBtnTrackTrainLocationClicked() {
////        Log.i("Location_is", "begain")
////        lifecycleScope.launch {
////            shareLocationViewModel?.locationLiveData?.observe(this@ShareLocationFeature,
////                Observer {
////                    Log.i(TAG, it.longitude.toString() + " " + it.latitude.toString())
////                })
////        }
//    }


}