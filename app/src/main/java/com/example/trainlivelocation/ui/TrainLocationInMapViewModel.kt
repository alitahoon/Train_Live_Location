package com.example.trainlivelocation.ui

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.LocationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TrainLocationInMapViewModel @Inject constructor(
private val context:Context
):ViewModel() {
    private var MAP_VIEW_Bundle:Bundle?=null
    private val TAG:String?="TrainLocationInMapViewModel"

    //distance
    private val _distanceLiveData:MutableLiveData<Double?> = MutableLiveData(null)
    val distanceLiveData:LiveData<Double?> = _distanceLiveData

    //address
    private val _addressLiveData:MutableLiveData<String?> = MutableLiveData(null)
    val addressLiveData:LiveData<String?> = _addressLiveData

    public fun getMAP_VIEW_KEY():Bundle?{
        MAP_VIEW_Bundle?.putString("MapViewBundleKey","101")
        return MAP_VIEW_Bundle
    }

    fun getDistanceInMeter(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ) {
        var results = FloatArray(1)
        Log.e(TAG, "startLat ${startLat}")
        Log.e(TAG, "startLon ${startLon}")
        Log.e(TAG, "endLat ${endLat}")
        Log.e(TAG, "endLon ${endLon}")

        Location.distanceBetween(startLat, startLon, endLat, endLon, results)
        Log.e(TAG, "distanceInMeter ${results[0]}")

        _distanceLiveData.postValue(results[0].toDouble())
    }

    fun getAddress(location:LocationDetails){
        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            location.longitude.toDouble(),
            location.latitude.toDouble(),
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String =
            addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses!![0].locality
        val state: String = addresses!![0].adminArea
        val country: String = addresses!![0].countryName
        val postalCode: String = addresses!![0].postalCode
        val knownName: String = addresses!![0].featureName // Only if available else return NULL

        //get first name of state
        val stateArr=state.split(" ")

        _addressLiveData.postValue("${city},${stateArr[0]},${country}")
    }


}