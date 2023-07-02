package com.example.trainlivelocation.ui

import Resource
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Location_Response
import com.example.domain.usecase.GetUserCurrantLocationLive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorLocationInMapViewModel @Inject constructor(
    private var getUserCurrantLocationLive: GetUserCurrantLocationLive
) : ViewModel() {
    private var TAG: String? = "DoctorLocationInMapViewModel"
    private var MAP_VIEW_Bundle: Bundle? = null

    private val _distance = MutableLiveData<String>()
    val distance: LiveData<String> = _distance
    private val _cars = MutableLiveData<String>()
    val cars: LiveData<String> = _cars


    private val _userCurrantLocation: MutableLiveData<Resource<Location>> = MutableLiveData(null)
    val userCurrantLocation: LiveData<Resource<Location>> = _userCurrantLocation


    fun getMAP_VIEW_KEY(): Bundle? {
        MAP_VIEW_Bundle?.putString("MapViewBundleKey", "104")
        return MAP_VIEW_Bundle
    }

    fun getCurrantLocation(patientLocation: Location_Response) {
        _userCurrantLocation.value = Resource.Loading
        viewModelScope.launch {
            getUserCurrantLocationLive() {
                when (it) {
                    is Resource.Loading -> {
                        Log.i(TAG, "getting currant location...")
                    }
                    is Resource.Success -> {
                        Log.i(TAG, "Success got currant location : ${it.data}")
                        _userCurrantLocation.value = it
                        if (it != null) {
                            var results = FloatArray(1)
                            Log.e(TAG, "startLat ${it.data.latitude}")
                            Log.e(TAG, "startLon ${it.data.longitude}")
                            Log.e(TAG, "endLat ${patientLocation.latitude}")
                            Log.e(TAG, "endLon ${patientLocation.longitude}")

                            Location.distanceBetween(
                                it.data.latitude,
                                it.data.longitude,
                                patientLocation.latitude,
                                patientLocation.longitude,
                                results
                            )


                            Log.e(TAG, "distanceInMeter ${results[0]}")
                            _distance.value="Distance : ${results[0]} M"
                            val carsNumber = (results[0] / 10).toInt()
                            _cars.value="car Number : ${carsNumber}"
                        }
                    }
                    is Resource.Failure -> {
                        Log.i(TAG, "Failed to get currant location : ${it.error}")
                    }
                    else -> {}
                }
            }
        }
    }
}