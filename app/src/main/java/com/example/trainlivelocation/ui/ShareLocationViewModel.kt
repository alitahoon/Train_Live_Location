package com.example.trainlivelocation.ui

import Resource
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
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareLocationViewModel @Inject constructor(
    private val getUserLocationLive: GetUserLocationLive,
    private val addLiveLoctationToApi: AddLiveLoctationToApi,
    private val startLocationUpdate: StartLocationUpdate,
    private val stopLocationUpdate: StopLocationUpdate,
//    private val getLocationTrackBackgroundService: GetLocationTrackBackgroundService,
    private val context: Context
) : ViewModel() {
    var trainId: String? = null
    var locationBackgroundservice: Intent? = null
    val _locationMutableData: MutableLiveData<LocationDetails> = MutableLiveData(null)
    lateinit var locationLiveData: LiveData<LocationDetails>
    private val TAG: String? = "ShareLocationViewModel"
    private lateinit var activity: Activity
    var btnShareTrainLocationClicked= SingleLiveEvent<Boolean>()
    var txtTrainIdClicked= SingleLiveEvent<Boolean>()

    private val _sharedTrainLocation: MutableLiveData<Resource<Location_Request_with_id>?> = MutableLiveData(null)
    val sharedTrainLocation: LiveData<Resource<Location_Request_with_id>?> = _sharedTrainLocation

    private val _startSharingtrainLocation: MutableLiveData<Resource<LifecycleService>?> = MutableLiveData(null)
    val startSharingtrainLocation: LiveData<Resource<LifecycleService>?> = _startSharingtrainLocation

    //get activity context from fragment
    fun setbaseActivity(baseActivity: Activity) {
        activity = baseActivity
    }

    //handle share train Location
    fun onTxtTrainIdClicked(view: View) {
        txtTrainIdClicked.postValue(true)
    }
    fun onBtnShareTrainLocationClicked(view: View) {
        btnShareTrainLocationClicked.postValue(true)
    }
    fun setLiveLocation(){
        viewModelScope.launch {
            getUserLocationLive{
                locationLiveData=it
            }
        }
    }

    //handle track train Location
//
//    fun startUpdate(){
//        viewModelScope.launch {
//            startLocationUpdate(6000)
//        }
//    }
//    fun startSharing(userId: Int?,trainId:Int?) {
//        _startSharingtrainLocation.value=Resource.Loading
//        viewModelScope.launch {
//            getLocationTrackBackgroundService(trainId!!,userId!!){
//                _startSharingtrainLocation.value=it
//            }
//        }
//    }



    fun stopLocationService(lifecycleOwner: LifecycleOwner) {

    }

    fun uplaodLocationToApi(longtude: Float, latitude: Float,userId:Int?) {
        Log.e(TAG,"uplaodLocationToApi")
        Log.e(TAG,longtude.toString()+" "+latitude.toString())
        _sharedTrainLocation.value=Resource.Loading
        viewModelScope.launch {
           addLiveLoctationToApi(Location_Request(latitude,longtude,trainId!!.toInt(),userId!!)){
               _sharedTrainLocation.value=it
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




