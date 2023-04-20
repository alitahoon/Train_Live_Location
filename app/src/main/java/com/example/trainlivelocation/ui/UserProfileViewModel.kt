package com.example.trainlivelocation.ui

import Resource
import android.util.Log
import androidx.lifecycle.*
import com.example.domain.entity.StationResponseItem
import com.example.domain.usecase.GetStationById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getStationById: GetStationById
) : ViewModel() {
    private val TAG:String?="UserProfileViewModel"

    var stationNameValue= MutableLiveData<String>()

    private val _stationName: MutableLiveData<Resource<StationResponseItem?>> =
        MutableLiveData(null)
    val stationName: LiveData<Resource<StationResponseItem?>> = _stationName


    fun getStationName(stationId: Int?) {
        _stationName.value=Resource.Loading
        viewModelScope.launch {
            getStationById(stationId) {
                _stationName.value=it
            }
        }
    }



}