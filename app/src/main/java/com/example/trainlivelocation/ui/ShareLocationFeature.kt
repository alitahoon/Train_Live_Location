package com.example.trainlivelocation.ui

import android.animation.Animator
import android.animation.Animator.AnimatorListener
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
import com.example.trainlivelocation.databinding.FragmentShareLocationBinding
import com.example.trainlivelocation.utli.LiveLocationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


@AndroidEntryPoint
class ShareLocationFeature : Fragment() , LiveLocationListener {
    private var TAG:String?="liveLocationFeatureFragment"

    private lateinit var binding: FragmentShareLocationBinding
    private val shareLocationViewModel:ShareLocationViewModel? by activityViewModels()

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        shareLocationViewModel?.stopLocationLiveUpdate()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
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
      binding= FragmentShareLocationBinding.inflate(inflater,container,false)
          .apply {
              this.viewmodel=shareLocationViewModel
          }
        shareLocationViewModel?.setbaseActivity(requireActivity())
        binding.viewmodel?.liveLocationListener=this
        return binding.root
    }

    companion object {

    }
    @Subscribe
    fun getLocationLive(locationDetails: LocationDetails){
        Log.e(TAG,locationDetails.latitude.toString()+"////"+locationDetails.longitude.toString())
        shareLocationViewModel?.uplaodLocationToApi(locationDetails.longitude,locationDetails.latitude)

    }

    override fun onBtnShareTrainLocationClicked() {
        shareLocationViewModel?.setLiveLocation()
        shareLocationViewModel?.startUpdate()
        binding.shareLocationTxtPara.setVisibility(View.VISIBLE)
        binding.shareLocationLotti.setVisibility(View.VISIBLE)
        binding.shareLocationLotti.addAnimatorListener(object : AnimatorListener{
            override fun onAnimationStart(p0: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(p0: Animator) {
                binding.shareLocationLotti.setVisibility(View.GONE)
            }

            override fun onAnimationCancel(p0: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator) {
                TODO("Not yet implemented")
            }

        })

//        shareLocationViewModel?.startLocationService()
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



}