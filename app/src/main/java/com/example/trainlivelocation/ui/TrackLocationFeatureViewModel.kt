package com.example.trainlivelocation.ui

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.Location_Response
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.example.trainlivelocation.utli.TrackLocationListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackLocationFeatureViewModel @Inject constructor(
    private val getLiveLoctationFromApi: GetLiveLoctationFromApi,
    private var getLocationTrackForegroundService: GetLocationTrackForegroundService,
    private val getUserLocation: GetUserLocation,
    private val startLocationUpdate: StartLocationUpdate,
    private val stopLocationUpdate: StopLocationUpdate
) : ViewModel() {
    private var TAG: String? = "TrackLocationFeatureViewModel"


    private val _userLocationMuta: MutableLiveData<LocationDetails?> = MutableLiveData(null)
    val userLocation: LiveData<LocationDetails?> = _userLocationMuta

    val _distanceMuta: MutableLiveData<Double?> = MutableLiveData(null)
    val distanceLiveData: LiveData<Double?> = _distanceMuta

    lateinit var trackLocationListener: TrackLocationListener
    val _trainLocationMuta: MutableLiveData<Location_Response?> =
        MutableLiveData(null)

    val trainLocationLive: LiveData<Location_Response?> =
        _trainLocationMuta

    var btnTrackLocationFeature = SingleLiveEvent<Boolean>()
    var txtChooseTrainIdClicked = SingleLiveEvent<Boolean>()
    var trainid: String? = null

    private lateinit var activity: Activity
    var locationForegrondservice: Intent? = null

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
        viewModelScope.launch {
            Log.e(TAG, "setLocationForgroundServices")
            getLocationTrackForegroundService(trainid?.toInt())
            locationForegrondservice =
                Intent(activity, getLocationTrackForegroundService::class.java)
        }
    }

    public fun getTrainLocationFromApi() {
        viewModelScope.launch {
//            var result = getLiveLoctationFromApi(trainid!!.toInt())
//            if (result.isSuccessful) {
//                if (result.body() != null) {
//                    Log.e(TAG, "success")
//                    _trainLocationMuta.postValue(result.body())
//                } else {
//                    Log.e(TAG, result.errorBody().toString())
//                    Toast.makeText(activity, result.errorBody().toString(), Toast.LENGTH_SHORT)
//                        .show()
//                }
//            } else {
//                Toast.makeText(activity, result.message(), Toast.LENGTH_SHORT).show()
//                Log.e(TAG, result.message())
//            }
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


}