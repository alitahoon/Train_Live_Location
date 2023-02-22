package com.example.trainlivelocation.ui

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.domain.entity.LocationDetails
import com.example.trainlivelocation.databinding.FragmentLiveLocationFeatureBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.serialDescriptor
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [liveLocationFeature.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class liveLocationFeature : Fragment() ,LiveLocationListener{
    private var TAG:String?="liveLocationFeatureFragment"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentLiveLocationFeatureBinding
    private val liveLocationViewModel:LiveLocationViewModel? by activityViewModels()

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
        liveLocationViewModel?.stopLocationService(this)
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
      binding= FragmentLiveLocationFeatureBinding.inflate(inflater,container,false)
          .apply {
              this.viewmodel=liveLocationViewModel
          }
        liveLocationViewModel?.setbaseActivity(requireActivity())
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
            liveLocationFeature().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onBtnShareTrainLocationClicked() {
        liveLocationViewModel?.startLocationService()
    }

    override fun onBtnTrackTrainLocationClicked() {
        Log.i("Location_is","begain")
        lifecycleScope.launch {
            liveLocationViewModel?.locationLiveData?.observe(this@liveLocationFeature,
            Observer {
                Log.i(TAG,it.longitude+" "+it.latitude)
            })
        }
    }

    @Subscribe
    fun recieveLocationEvent(locationDetails: LocationDetails){
        Log.i(TAG,"Location Latitude --> ${locationDetails?.latitude}\nLocation longitude --> ${locationDetails?.longitude}")
    }

}