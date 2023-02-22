package com.example.trainlivelocation.ui

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.LocationDetails
import com.example.domain.repo.LocationListener
import com.example.domain.usecase.GetLocationLive
import com.example.domain.usecase.GetLocationTrackBackgroundService
import com.example.domain.usecase.StartLocationUpdate
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.security.acl.Owner
import javax.inject.Inject

@HiltViewModel
class LiveLocationViewModel @Inject constructor(
    private val getLocationLive: GetLocationLive,
    private val startLocationUpdate: StartLocationUpdate,
    private val getLocationTrackBackgroundService: GetLocationTrackBackgroundService,
    private val context:Context
) :ViewModel(){
    var locationBackgroundservice:Intent?=null
    val _locationMutableData: MutableLiveData<LocationDetails> = MutableLiveData(null)
    lateinit var  locationLiveData:LiveData<LocationDetails>
    private val TAG:String?="LiveLocationViewModel"

    private lateinit var locationManager: LocationManager
    private lateinit var activity:Activity
    lateinit var liveLocationListener: LiveLocationListener
    //get activity context from fragment
    fun setbaseActivity(baseActivity: Activity) {
        activity = baseActivity
    }

    //handle share train Location
    fun onBtnShareTrainLocationClicked(view: View){
      liveLocationListener.onBtnShareTrainLocationClicked()
    }

    //handle track train Location
    fun onBtnTrackTrainLocationClicked(view: View){
//        startLocationUpdateRequest()
//        viewModelScope.launch{
//           var location=getLocationLive()
//            locationLiveData=location
//            _locationMutableData.postValue(locationLiveData.value)
//            Log.i(TAG,"locationLiveData set success")
//            liveLocationListener.onBtnTrackTrainLocationClicked()
//        }
//            liveLocationListener.onBtnTrackTrainLocationClicked()
        }

    fun startLocationUpdateRequest(){
        viewModelScope.launch {
            startLocationUpdate()
        }
    }


    fun setLocationBackgroundServices(){
        viewModelScope.launch {
            locationBackgroundservice= Intent(activity,getLocationTrackBackgroundService()::class.java)
        }
    }

    fun startLocationService(){
        setLocationBackgroundServices()
        activity.startService(locationBackgroundservice)
    }
    fun stopLocationService(lifecycleOwner: LifecycleOwner){
        setLocationBackgroundServices()
        activity.stopService(locationBackgroundservice)
    }


    }

//    fun setLocationRepository() {
//        locationRepository = getLocationLive()
//    }




