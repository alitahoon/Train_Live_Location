package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetImageFromFirebaseStorage
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val context: Context,
    private val getImageFromFirebaseStorage: GetImageFromFirebaseStorage
) :ViewModel(){
    private val TAG:String?="MainActivityViewModel"
    private val sharedPrefFile = "UserToken"
    var menuBtnClicked= SingleLiveEvent<Boolean>()

    var btnBack = SingleLiveEvent<Boolean>()



    private val _userProfileImageUri: MutableLiveData<Resource<Uri>?> = MutableLiveData(null)
    val userProfileImageUri: LiveData<Resource<Uri>?> = _userProfileImageUri



    fun onBtnBack(view: View){
        btnBack.value = true
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
        Log.i(TAG,"hhhh")
        menuBtnClicked.postValue(true)
    }
}