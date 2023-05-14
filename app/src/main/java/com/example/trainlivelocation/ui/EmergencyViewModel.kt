package com.example.trainlivelocation.ui

import Resource
import android.location.Location
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.*
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyViewModel @Inject constructor(
    private val getDoctorInTrain: GetDoctorInTrain,
    private val sendDoctorNotificationUsingFCM: SendDoctorNotificationUsingFCM,
    private val getUserNotificationTokenFromFirebase: GetNotificationTokenFromFirebase,
    private val pushNewTopicNotification: PushNewTopicNotification,
    private val getUserCurrantLocationJustOnce: GetUserCurrantLocationJustOnce
) : ViewModel() {
    private val TAG = "EmergencyViewModel"

    private val _notificationToken: MutableLiveData<Resource<String?>> = MutableLiveData(null)
    val notificationToken: LiveData<Resource<String?>> = _notificationToken

//    private val _userLocation: MutableLiveData<Resource<String>?> = MutableLiveData(null)
//    val userLocation: LiveData<Resource<String>?> = _userLocation

    private val _doctors: MutableLiveData<Resource<ArrayList<DoctorResponseItem>>?> =
        MutableLiveData(null)
    val doctors: LiveData<Resource<ArrayList<DoctorResponseItem>>?> = _doctors

    private val _sentNotification: MutableLiveData<Resource<String>?> = MutableLiveData(null)
    val sentNotification: LiveData<Resource<String>?> = _sentNotification

    private val _userLocation: MutableLiveData<Resource<Location>> = MutableLiveData(null)
    val userLocation: LiveData<Resource<Location>> = _userLocation

    var txtChooseTrainIdClicked = SingleLiveEvent<Boolean>()
    var trainid: String? = null

    fun ontxtChooseTrainIdClicked(view: View) {
        txtChooseTrainIdClicked.postValue(true)
    }

    fun getDoctors(trainId: Int?) {
        _doctors.value = Resource.Loading
        viewModelScope.launch {
            getDoctorInTrain(trainId!!) {
                _doctors.value = it
            }
        }
    }

    fun sentDoctorNotification(
        token: NotificatonToken,
        serverKey: String?,
        doctorNotification: DoctorNotification,
        location: Location
    ) {
//        _sentNotification.value=Resource.Loading
//        viewModelScope.launch {
//           val child1=viewModelScope.launch (Dispatchers.IO){
//                sendDoctorNotificationUsingFCM(token,serverKey,doctorNotification){
//                    val child2=viewModelScope.launch(Dispatchers.Main) {
//                        _sentNotification.value=it                    }
//                }
//            }
//            child1.join()
//        }
        _sentNotification.value = Resource.Loading
        viewModelScope.launch {
            pushNewTopicNotification(
                PushNotification(
                    DoctorNotificationData(
                        "doctors",
                        doctorNotification.content, location.latitude, location.longitude
                    ), token.token!!
                )
            ) {
                _sentNotification.value = it
            }
        }

    }

    fun getUserLocation(

    ) {
        _userLocation.value = Resource.Loading
        viewModelScope.launch {
            getUserCurrantLocationJustOnce {
                _userLocation.value=it
            }
        }
    }

    fun getNotificationToken(userPhone: String?) {
        _notificationToken.value = Resource.Loading
        viewModelScope.launch {
            getUserNotificationTokenFromFirebase(userPhone) {
                _notificationToken.value = it
            }
        }
    }

}