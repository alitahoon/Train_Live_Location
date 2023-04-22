package com.example.trainlivelocation.ui

import Resource
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.domain.entity.RegisterUser
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.UserResponseItem
import com.example.domain.usecase.GetStationById
import com.example.domain.usecase.UpdateUserData
import com.example.trainlivelocation.utli.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getStationById: GetStationById,
    private val updateUserData: UpdateUserData
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


}