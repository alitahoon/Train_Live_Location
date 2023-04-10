package com.example.trainlivelocation.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.userResponseItem
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val context: Context,

) :ViewModel(){
    private val sharedPrefFile = "UserToken"


    private val _userData: MutableLiveData<userResponseItem?> = MutableLiveData(null)
    val userData: LiveData<userResponseItem?> = _userData


    fun getUserDataFromsharedPreference() {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        _userData.postValue(
            userResponseItem(
            userSharedPreferences.getString("address","empty")!!,
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
        ))
    }
}