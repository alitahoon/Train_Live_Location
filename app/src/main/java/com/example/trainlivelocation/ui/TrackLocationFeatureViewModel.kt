package com.example.trainlivelocation.ui

import Resource
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LocationLive
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.Location_Request
import com.example.domain.entity.Location_Response
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.LocationTrackBackgroundService
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.example.trainlivelocation.utli.TrackLocationListener
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class TrackLocationFeatureViewModel @Inject constructor(
    private val getLiveLoctationFromApi: GetLiveLoctationFromApi,
    private var getLocationTrackForegroundService: GetLocationTrackForegroundService,
    private val getUserLocation: GetUserLocation,
    private val startLocationUpdate: StartLocationUpdate,
    private val stopLocationUpdate: StopLocationUpdate,
    private val locationLive: LocationLive,
    private val gettingTrainlocationFromApi: GettingTrainlocationFromApi

) : ViewModel() {
    private var TAG: String? = "TrackLocationFeatureViewModel"

    private var MAP_VIEW_Bundle: Bundle? = null

    private val _userLocationMuta: MutableLiveData<LocationDetails?> = MutableLiveData(null)
    val userLocation: LiveData<LocationDetails?> = _userLocationMuta

    private val _trainLocation: MutableLiveData<Resource<Location_Response>?> = MutableLiveData(null)
    val trainLocation: LiveData<Resource<Location_Response>?> = _trainLocation



    val userLiveLocation = locationLive

    val _distanceMuta: MutableLiveData<Double?> = MutableLiveData(null)
    val distanceLiveData: LiveData<Double?> = _distanceMuta

    lateinit var trackLocationListener: TrackLocationListener
    val _trainLocationMuta: MutableLiveData<Location_Response?> =
        MutableLiveData(null)

    val trainLocationLive: LiveData<Location_Response?> =
        _trainLocationMuta

    var btnTrackLocationFeature = SingleLiveEvent<Boolean>()
    var txtChooseTrainIdClicked = SingleLiveEvent<Boolean>()
    var trainid = MutableLiveData<String>()

    private lateinit var activity: Activity
    var locationForegrondservice: Intent? = null


    fun getMAP_VIEW_KEY(): Bundle? {
        MAP_VIEW_Bundle?.putString("MapViewBundleKey", "102")
        return MAP_VIEW_Bundle
    }

    fun setbaseActivity(baseActivity: Activity) {
        activity = baseActivity
    }

    public fun onBtnTrackTrainLocationClicked(view: View) {
        btnTrackLocationFeature.postValue(true)
    }
    fun onTxtChooseTrainIdClicked(view: View){
        txtChooseTrainIdClicked.postValue(true)
    }

    fun setLocationForgroundServices() {
//        viewModelScope.launch {
//            Log.e(TAG, "setLocationForgroundServices")
//            getLocationTrackForegroundService(trainid?.toInt())
//            locationForegrondservice =
//                Intent(activity, getLocationTrackForegroundService::class.java)
//        }
    }

    public fun getTrainLocationFromApi() {
        viewModelScope.launch {

            getLiveLoctationFromApi(18){
                Log.i(TAG,"${it}")
            }
        }
    }

    fun startTrackLocationForgroundService() {
        Log.e(TAG, "startTrackLocationForgroundService")
        setLocationForgroundServices()
        activity.startService(locationForegrondservice)
    }

    fun stopTrackLocationForgroundService() {
        activity.stopService(locationForegrondservice)
    }

    fun getUserCurrantLocation() {
        viewModelScope.launch {
//            getUserLocation(){
//                location ->
//                _userLocationMuta.postValue(location)
//            }
            startLocationUpdate(6000)
        }
    }




    //new flow for tracking location
    fun startGettingUserLocation(){

        var locationRequest = LocationRequest()
        locationRequest?.interval = 6000
        locationRequest?.fastestInterval = 6000 / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationLive.startLocationUpdate(locationRequest)
    }

    fun stopGettingUserLocation() {
        Log.e(TAG,"startObservingLocation")
        locationLive.stopLocationLiveUpdate()
    }

    fun gettingTrainLocaion(lifecycleOwner: LifecycleOwner){
        trainid.observe(lifecycleOwner, Observer {
            viewModelScope.launch {
                Log.i(TAG,"trainId : ${trainid}")
                _trainLocation.value=Resource.Loading
                val child1=launch (Dispatchers.IO){
                    gettingTrainlocationFromApi(it!!.toInt()){
                        val child2=launch (Dispatchers.Main){
                            _trainLocation.value=it
                        }
                    }
                }
                child1.join()
            }
        })

    }

}