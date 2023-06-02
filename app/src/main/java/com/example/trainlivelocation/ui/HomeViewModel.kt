package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.example.trainlivelocation.utli.SingleLiveEvent
import android.view.View
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.NotificatonToken
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val context: Context,
    private val sendUserNotificationTokenToFirebase: SendUserNotificationTokenToFirebase,
    private val getTrainLocationInForgroundService: GetTrainLocationInForgroundService,
    private val subscribeToNewTopic: SubscribeToNewTopic,
    private val getAllStations: GetAllStations,
    private val getLocationDirctionFromGoogleMapsApi: GetLocationDirctionFromGoogleMapsApi
) : ViewModel() {
    private val TAG: String = "HomeViewModel"
    private val sharedPrefFile = "UserToken"
    var locationBtn = SingleLiveEvent<Boolean>()
    var postsBtn = SingleLiveEvent<Boolean>()
    var locationCardBtn = SingleLiveEvent<Boolean>()
    var btnTicketClicked = SingleLiveEvent<Boolean>()
    var btnEmergancyClicked = SingleLiveEvent<Boolean>()
    var chooseTrainTxtClicked = SingleLiveEvent<Boolean>()
    var passengersbtnClicked = SingleLiveEvent<Boolean>()

    private var MAP_VIEW_Bundle: Bundle? = null

    var trainId: String? = null

    private var _sendingNotificationToken: MutableLiveData<Resource<String?>> =
        MutableLiveData(null)
    var sendingNotificationToken: LiveData<Resource<String?>> = _sendingNotificationToken


    private val _trainbackgroundTrackingServices: MutableLiveData<Resource<LifecycleService>?> =
        MutableLiveData(null)
    val trainbackgroundTrackingServices: LiveData<Resource<LifecycleService>?> =
        _trainbackgroundTrackingServices


    private val _stations: MutableLiveData<Resource<ArrayList<StationResponseItem>>?> =
        MutableLiveData(null)
    val stations: LiveData<Resource<ArrayList<StationResponseItem>>?> = _stations


    private val _dirction: MutableLiveData<Resource<DirectionsResult>?> =
        MutableLiveData(null)
    val dirction: LiveData<Resource<DirectionsResult>?> = _dirction

    fun onLocationBtn(view: View) {
        locationBtn.postValue(true)
    }

    fun onChooseTrainTxtClicked(view: View) {
        chooseTrainTxtClicked.postValue(true)
    }

    fun onbtnEmergancyClicked(view: View) {
        btnEmergancyClicked.postValue(true)
    }

    fun onPostsBtn(view: View) {
        postsBtn.postValue(true)
    }

    fun onBtnTicketClicked(view: View) {
        btnTicketClicked.postValue(true)
    }

    fun onLocationCardBtn(view: View) {
        locationCardBtn.postValue(true)
    }

    fun onPassengersbtnClicked(view: View) {
        passengersbtnClicked.postValue(true)
    }


    fun getTrainLocationInbackground(trainId: Int?) {
        _trainbackgroundTrackingServices.value = Resource.Loading
        viewModelScope.launch {
            getTrainLocationInForgroundService(trainId) {
                _trainbackgroundTrackingServices.value = it
            }
        }
    }

    fun getMAP_VIEW_KEY(): Bundle? {
        MAP_VIEW_Bundle?.putString("MapViewBundleKey", "103")
        return MAP_VIEW_Bundle
    }

    fun sendingTokenToFirebase(token: NotificatonToken?) {
        _sendingNotificationToken.value = Resource.Loading
        viewModelScope.launch {
            sendUserNotificationTokenToFirebase(token) {
                _sendingNotificationToken.value = it
            }
        }
    }

    fun subscribeToNewTopic() {
        viewModelScope.launch() {
            subscribeToNewTopic("doctors") {
                Log.i(TAG, "${it}")
            }
        }
    }

    fun getLocationDirctions(
        origin: LatLng,
        destination: LatLng
    ) {
        viewModelScope.launch {
            _dirction.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                getLocationDirctionFromGoogleMapsApi(origin,destination){
                    val child2=launch(Dispatchers.Main) {
                        _dirction.value=it
                    }
                }
            }
            child1.join()
        }
    }


    fun getAllStation() {
        viewModelScope.launch {
            _stations.value = Resource.Loading
            val child1 = launch(Dispatchers.IO) {
                getAllStations {
                    val child2 = launch(Dispatchers.Main) {
                        _stations.value = it
                    }
                }
            }
            child1.join()
        }
    }


}