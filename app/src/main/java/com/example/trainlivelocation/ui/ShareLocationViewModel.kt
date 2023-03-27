package com.example.trainlivelocation.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.*
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.LiveLocationListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareLocationViewModel @Inject constructor(
    private val getLocationLive: GetLocationLive,
    private val addLiveLoctationToApi: AddLiveLoctationToApi,
    private val startLocationUpdate: StartLocationUpdate,
    private val stopLocationUpdate: StopLocationUpdate,
    private val getLocationTrackBackgroundService: GetLocationTrackBackgroundService,
    private val context: Context
) : ViewModel() {
    var trainId: String? = null
    var locationBackgroundservice: Intent? = null
    val _locationMutableData: MutableLiveData<LocationDetails> = MutableLiveData(null)
    lateinit var locationLiveData: LiveData<LocationDetails>
    private val TAG: String? = "ShareLocationViewModel"
    private lateinit var activity: Activity
    lateinit var liveLocationListener: LiveLocationListener

    private val _trainLocationMeta: MutableLiveData<Location_Request_with_id?> = MutableLiveData(null)
    val trainLocationLive: LiveData<Location_Request_with_id?> = _trainLocationMeta

    //get activity context from fragment
    fun setbaseActivity(baseActivity: Activity) {
        activity = baseActivity
    }

    //handle share train Location
    fun onBtnShareTrainLocationClicked(view: View) {
        liveLocationListener.onBtnShareTrainLocationClicked()
    }
    fun setLiveLocation(){
        viewModelScope.launch {
            locationLiveData=getLocationLive()
        }
    }

    //handle track train Location

    fun startUpdate(){
        viewModelScope.launch {
            startLocationUpdate(6000)
        }
    }
    fun setLocationBackgroundServices() {
        viewModelScope.launch {
            locationBackgroundservice =
                Intent(activity, getLocationTrackBackgroundService(trainId!!.toInt(), 2)::class.java)
        }
    }

    fun startLocationService() {
        setLocationBackgroundServices()
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            activity.startForegroundService(locationBackgroundservice)
        }
        activity.startService(locationBackgroundservice)
    }

    fun stopLocationService(lifecycleOwner: LifecycleOwner) {
        activity.stopService(locationBackgroundservice)
    }

    fun uplaodLocationToApi(longtude: Float, latitude: Float) {
        Log.e(TAG,"uplaodLocationToApi")
        Log.e(TAG,longtude.toString()+" "+latitude.toString())
        viewModelScope.launch {
           var result=addLiveLoctationToApi(Location_Request(longtude, latitude,trainId!!.toInt(),2))
            if (result.isSuccessful){
                if (result.body()!=null){
                    _trainLocationMeta.postValue(result.body())
                }
            }else{
                Log.e(TAG,result.message())
            }
        }
    }

    public fun stopLocationLiveUpdate(){
        viewModelScope.launch {
            stopLocationUpdate
        }
    }


}

//    fun setLocationRepository() {
//        locationRepository = getLocationLive()
//    }




