package com.example.trainlivelocation.ui

import android.content.Context
import android.content.SharedPreferences
import com.example.trainlivelocation.utli.SingleLiveEvent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.UserResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val context: Context
) :ViewModel() {
    private val sharedPrefFile = "UserToken"
    var locationBtn= SingleLiveEvent<Boolean>()
    var postsBtn= SingleLiveEvent<Boolean>()
    var locationCardBtn= SingleLiveEvent<Boolean>()
    var btnTicketClicked= SingleLiveEvent<Boolean>()
    var chooseTrainTxtClicked= SingleLiveEvent<Boolean>()

    var trainId:String?=null

    private val _userData: MutableLiveData<UserResponseItem?> = MutableLiveData(null)
    val userData: LiveData<UserResponseItem?> = _userData
     fun onLocationBtn(view: View){
        locationBtn.postValue(true)
    }

    fun onChooseTrainTxtClicked(view: View){
        chooseTrainTxtClicked.postValue(true)
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
    fun getUserDataFromsharedPreference() {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        _userData.postValue(
            UserResponseItem(
                userSharedPreferences.getString("userAddress","empty")!!,
                userSharedPreferences.getString("userBirthdate","empty")!!,
                userSharedPreferences.getString("userEmail","empty")!!,
                userSharedPreferences.getString("userGender","empty")!!,
                userSharedPreferences.getInt("userId",0),
                userSharedPreferences.getString("userJop","empty")!!,
                userSharedPreferences.getString("userName","empty")!!,
                userSharedPreferences.getString("userPassword","empty")!!,
                userSharedPreferences.getString("userPhone","empty")!!,
                userSharedPreferences.getString("userRole","empty")!!,
                userSharedPreferences.getInt("userStationId",0)
            )
        )
    }


}