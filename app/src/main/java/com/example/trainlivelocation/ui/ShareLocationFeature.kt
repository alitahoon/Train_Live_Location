package com.example.trainlivelocation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.domain.entity.LocationDetails
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentShareLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShareLocationFeature.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ShareLocationFeature : Fragment() ,LiveLocationListener{
    private var TAG:String?="liveLocationFeatureFragment"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentShareLocationBinding
    private val shareLocationViewModel:ShareLocationViewModel? by activityViewModels()

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
        shareLocationViewModel?.stopLocationService(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding= FragmentShareLocationBinding.inflate(inflater,container,false)
          .apply {
              this.viewmodel=shareLocationViewModel
          }
        shareLocationViewModel?.setbaseActivity(requireActivity())
        binding.viewmodel?.liveLocationListener=this
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Feature_live_location.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShareLocationFeature().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    @Subscribe
    fun getLocationLive(locationDetails: LocationDetails){
        Log.e(TAG,locationDetails.latitude.toString()+"////"+locationDetails.longitude.toString())
        shareLocationViewModel?.uplaodLocationToApi(locationDetails.longitude,locationDetails.latitude)    }

    override fun onBtnShareTrainLocationClicked() {
        shareLocationViewModel?.setbaseActivity(requireActivity())
        shareLocationViewModel?.setLiveLocation()
        shareLocationViewModel?.startUpdate()
    }

    override fun onBtnTrackTrainLocationClicked() {
        Log.i("Location_is","begain")
        lifecycleScope.launch {
            shareLocationViewModel?.locationLiveData?.observe(this@ShareLocationFeature,
            Observer {
                Log.i(TAG,it.longitude.toString()+" "+it.latitude.toString())
            })
        }
    }

    @Subscribe
    fun recieveLocationEvent(locationDetails: LocationDetails){
        Log.i(TAG,"Location Latitude --> ${locationDetails?.latitude}\nLocation longitude --> ${locationDetails?.longitude}")
    }

}