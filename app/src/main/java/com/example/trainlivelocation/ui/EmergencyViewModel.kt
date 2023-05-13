package com.example.trainlivelocation.ui

import Resource
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.*
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class EmergencyViewModel @Inject constructor(
    private val getDoctorInTrain: GetDoctorInTrain,
    private val sendDoctorNotificationUsingFCM: SendDoctorNotificationUsingFCM,
    private val getUserNotificationTokenFromFirebase: GetNotificationTokenFromFirebase,
    private val pushNewTopicNotification: PushNewTopicNotification
):ViewModel() {

    private val _notificationToken: MutableLiveData<Resource<String?>> = MutableLiveData(null)
    val notificationToken: LiveData<Resource<String?>> = _notificationToken

    private val _userLocation: MutableLiveData<Resource<String>?> = MutableLiveData(null)
    val userLocation: LiveData<Resource<String>?> = _userLocation

    private val _doctors: MutableLiveData<Resource<ArrayList<DoctorResponseItem>>?> = MutableLiveData(null)
    val doctors: LiveData<Resource<ArrayList<DoctorResponseItem>>?> = _doctors

    private val _sentNotification: MutableLiveData<Resource<String>?> = MutableLiveData(null)
    val sentNotification: LiveData<Resource<String>?> = _sentNotification

    var txtChooseTrainIdClicked = SingleLiveEvent<Boolean>()
    var trainid: String? = null

    fun ontxtChooseTrainIdClicked(view: View){
        txtChooseTrainIdClicked.postValue(true)
    }

    fun getDoctors(trainId:Int?){
        _doctors.value=Resource.Loading
        viewModelScope.launch {
            getDoctorInTrain(trainId!!){
                _doctors.value=it
            }
        }
    }

    fun sentDoctorNotification(token:NotificatonToken,serverKey:String?,doctorNotification: DoctorNotification){
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
        _sentNotification.value=Resource.Loading
        viewModelScope.launch {
            pushNewTopicNotification(PushNotification(NotificationData("doctors",doctorNotification.content),token.token!!)){
                _sentNotification.value=it
            }
        }

    }

    fun getNotificationToken(userPhone:String?){
        _notificationToken.value=Resource.Loading
        viewModelScope.launch {
            getUserNotificationTokenFromFirebase(userPhone){
                _notificationToken.value=it
            }
        }
    }
}