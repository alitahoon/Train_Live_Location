package com.example.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.example.domain.entity.LocationDetails
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.greenrobot.eventbus.EventBus

class LocationLive(private val context: Context) : LiveData<LocationDetails>() {
    private val TAG:String?="LocationLive"
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    //    private var locationRequest: com.google.android.gms.location.LocationRequest? = null
    private var interval:Long?=null
    override fun onActive() {
        super.onActive()
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
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location.also {
                setLocationData(it)
            }
        }
    }

    private fun setLocationData(location: Location?) {
        location?.let { location ->
            value = LocationDetails(location.longitude.toFloat(), location.latitude.toFloat())
            EventBus.getDefault().post(LocationDetails(location.longitude.toFloat(), location.latitude.toFloat()))
            Log.i("Location is", location.longitude.toString() +"***"+location.latitude.toString())
        }
    }

      fun startLocationUpdate(locationRequest: LocationRequest) {
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
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
     fun stopLocationLiveUpdate(){
        Log.i(TAG,"stopLocationLiveUpdate")
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


    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    fun setInterVal(interval:Long){
        this.interval=interval
    }

//    private fun createLocationRequest() {
//        locationRequest = LocationRequest.create()
//        locationRequest?.interval = interval!!
//        locationRequest?.fastestInterval = interval!!/4
//        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    }

}