package com.example.data

import Resource
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.entity.LocationDetails
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.greenrobot.eventbus.EventBus

class LocationServices(private  val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val TAG:String?="LocationLive"
    //    private var locationRequest: com.google.android.gms.location.LocationRequest? = null
    private var interval: Long? = null

    private var _locationResource: MutableLiveData<Resource<LocationDetails>>? =
        MutableLiveData(null)
    var locationResource: LiveData<Resource<LocationDetails>>? = _locationResource

    internal fun init(){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location.also {
                setLocationData(it)
                _locationResource!!.value =
                    Resource.Success(LocationDetails(it.longitude.toFloat(), it.latitude.toFloat()))
            }
        }.addOnFailureListener {
            _locationResource!!.value =
                Resource.Failure("Failed to get User Location -> ${it.message}")
        }
    }

    private fun setLocationData(location: Location?) {
        location?.let { location ->
             val value = LocationDetails(location.longitude.toFloat(), location.latitude.toFloat())
            EventBus.getDefault()
                .post(LocationDetails(location.longitude.toFloat(), location.latitude.toFloat()))
            Log.i(
                TAG,
                location.longitude.toString() + "***" + location.latitude.toString()
            )
        }
    }
    fun setInterVal(interval: Long) {
        this.interval = interval
    }

    internal fun getUserLiveLocation(): Resource<LocationDetails>{
        return locationResource!!.value!!
    }

    internal fun startLocationUpdate(locationRequest: LocationRequest) {
        Log.i(TAG,"startLocationUpdate")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
//        createLocationRequest()
        fusedLocationClient.requestLocationUpdates(
            locationRequest!!,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    internal fun stopLocationLiveUpdate() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }
}