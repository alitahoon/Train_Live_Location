package com.example.trainlivelocation.ui

import Resource
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.ClearUserSignDataFromDatabase
import com.example.domain.usecase.GetStationById
import com.example.domain.usecase.SendImageToFirebaseStorage
import com.example.domain.usecase.UpdateUserData
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getStationById: GetStationById,
    private val context: Context,
    private val updateUserData: UpdateUserData,
    private val clearUserSignDataFromDatabase: ClearUserSignDataFromDatabase,
    private val sendImageToFirebaseStorage: SendImageToFirebaseStorage

    ) : ViewModel() {
    private val TAG: String? = "UserProfileViewModel"
    var btnSaveUserDateClicked = SingleLiveEvent<Boolean>()

    var stationNameValue = MutableLiveData<String>()

    private val _stationName: MutableLiveData<Resource<StationResponseItem?>> =
        MutableLiveData(null)
    val stationName: LiveData<Resource<StationResponseItem?>> = _stationName

    private val _userUpdated: MutableLiveData<Resource<String?>> =
        MutableLiveData(null)
    val userUpdated: LiveData<Resource<String?>> = _userUpdated

    private val _clearUserData: MutableLiveData<Resource<String?>> =
        MutableLiveData(null)
    val clearUserData: LiveData<Resource<String?>> = _clearUserData
    val btnUserProfileImg = SingleLiveEvent<Boolean>()

    private var _sendingProfileImageResult: MutableLiveData<Resource<String>?> =
        MutableLiveData(null)
    var sendingProfileImageResult: LiveData<Resource<String>?> = _sendingProfileImageResult

    fun onBtnUserProfileImg (view: View){
        btnUserProfileImg.value=true
    }

    fun uploadProfileImage(profileImageUri: Uri, userPhone: String?) {
        _sendingProfileImageResult.value = Resource.Loading
        viewModelScope.launch {
            sendImageToFirebaseStorage(profileImageUri, "profileImages/${userPhone!!}") {
                Log.i(TAG, "${it}")
                _sendingProfileImageResult.value = it
            }

        }
    }


    fun getStationName(stationId: Int?) {
        _stationName.value = Resource.Loading
        viewModelScope.launch {
            getStationById(stationId) {
                _stationName.value = it
            }
        }
    }

    fun updateUserProfileData(
        userID: Int?,
        userRequest: RegisterUser
    ) {
        _userUpdated.value = Resource.Loading
        viewModelScope.launch {
            updateUserData(userID, userRequest) {
                _userUpdated.value = it
            }
        }
    }

    fun onBtnSaveUserDataClicked(view: View) {
        btnSaveUserDateClicked.postValue(true)
    }

    fun clearUserData() {
        viewModelScope.launch {
            _clearUserData.value=Resource.Loading
            val child1 = launch(Dispatchers.IO) {
                clearUserSignDataFromDatabase() {
                    val child2 = launch(Dispatchers.Main) {
                        _clearUserData.value=it
                        Log.e(TAG, "clearing user data ")
                    }
                }
            }
            child1.join()

        }
    }
    fun saveUserTokenInSharedPreferences(userItem: UserResponseItem){
        val userSharedPreferences : SharedPreferences =context.getSharedPreferences("UserToken",
            Context.MODE_PRIVATE)
        var editor=userSharedPreferences.edit()
        editor.putString("userName",userItem.name)
        editor.putString("userPhone",userItem.phone)
        editor.putString("userBirthdate",userItem.birthDate)
        editor.putString("userAddress",userItem.address)
        editor.putString("userJop",userItem.jop)
        editor.putString("userEmail",userItem.email)
        editor.putString("userRole",userItem.role)
        editor.putString("userGender",userItem.gender)
        editor.putString("userPassword",userItem.password)
        editor.putString("tokenForNotifications",userItem.tokenForNotifications)
        editor.putInt("userId",userItem.id)
        editor.apply()
        editor.commit()
    }

}