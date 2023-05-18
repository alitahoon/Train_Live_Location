package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.trainlivelocation.utli.SingleLiveEvent
import android.view.View
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.NotificatonToken
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetTrainLocationInForgroundService
import com.example.domain.usecase.SendUserNotificationTokenToFirebase
import com.example.domain.usecase.SubscribeToNewTopic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val context: Context,
    private val sendUserNotificationTokenToFirebase:SendUserNotificationTokenToFirebase,
    private val getTrainLocationInForgroundService: GetTrainLocationInForgroundService,
    private val subscribeToNewTopic: SubscribeToNewTopic
) :ViewModel() {
    private val TAG:String="HomeViewModel"
    private val sharedPrefFile = "UserToken"
    var locationBtn= SingleLiveEvent<Boolean>()
    var postsBtn= SingleLiveEvent<Boolean>()
    var locationCardBtn= SingleLiveEvent<Boolean>()
    var btnTicketClicked= SingleLiveEvent<Boolean>()
    var btnEmergancyClicked= SingleLiveEvent<Boolean>()
    var chooseTrainTxtClicked= SingleLiveEvent<Boolean>()
    var passengersbtnClicked= SingleLiveEvent<Boolean>()


    var trainId:String?=null

    private var _sendingNotificationToken:MutableLiveData<Resource<String?>> = MutableLiveData(null)
    var sendingNotificationToken:LiveData<Resource<String?>> = _sendingNotificationToken



    private val _trainbackgroundTrackingServices: MutableLiveData<Resource<LifecycleService>?> = MutableLiveData(null)
    val trainbackgroundTrackingServices: LiveData<Resource<LifecycleService>?> = _trainbackgroundTrackingServices

     fun onLocationBtn(view: View){
        locationBtn.postValue(true)
    }

    fun onChooseTrainTxtClicked(view: View){
        chooseTrainTxtClicked.postValue(true)
    }
    fun onbtnEmergancyClicked(view: View){
        btnEmergancyClicked.postValue(true)
    }
     fun onPostsBtn(view: View){
        postsBtn.postValue(true)
    }
    fun onBtnTicketClicked(view: View){
        btnTicketClicked.postValue(true)
    }
     fun onLocationCardBtn(view:View){
        locationCardBtn.postValue(true)
    }

    fun onPassengersbtnClicked(view:View){
        passengersbtnClicked.postValue(true)
    }


    fun getTrainLocationInbackground(trainId:Int?){
        _trainbackgroundTrackingServices.value=Resource.Loading
        viewModelScope.launch {
            getTrainLocationInForgroundService(trainId){
                _trainbackgroundTrackingServices.value=it
            }
        }
    }

    fun sendingTokenToFirebase(token: NotificatonToken?){
        _sendingNotificationToken.value=Resource.Loading
        viewModelScope.launch {
            sendUserNotificationTokenToFirebase(token){
                _sendingNotificationToken.value=it
            }
        }
    }

    fun subscribeToNewTopic(){
        viewModelScope.launch (){
            subscribeToNewTopic("doctors"){
                Log.i(TAG,"${it}")
            }
        }
    }


}