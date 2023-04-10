package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.userResponseItem
import com.example.domain.usecase.GetImageFromFirebaseStorage
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val context: Context,
    private val getImageFromFirebaseStorage: GetImageFromFirebaseStorage
) :ViewModel(){
    private val sharedPrefFile = "UserToken"
    var menuBtnClicked= SingleLiveEvent<Boolean>()

    private val _userData: MutableLiveData<userResponseItem?> = MutableLiveData(null)
    val userData: LiveData<userResponseItem?> = _userData


    private val _userProfileImageUri: MutableLiveData<Resource<Uri>?> = MutableLiveData(null)
    val userProfileImageUri: LiveData<Resource<Uri>?> = _userProfileImageUri


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

    fun getProfileImage(imageRef:String?){
        _userProfileImageUri.value=Resource.Loading
        viewModelScope.launch {
            getImageFromFirebaseStorage(imageRef){
                _userProfileImageUri.value=it
            }
        }
    }

    fun onMenuBtnClicked(view: View){
        menuBtnClicked.postValue(true)
    }
}