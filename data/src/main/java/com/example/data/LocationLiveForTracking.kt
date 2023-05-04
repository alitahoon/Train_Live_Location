package com.example.data

import Resource
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
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

class LocationLiveForTracking(private val context: Context) : LiveData<LocationDetails>() {
    private var location: Location? = null
    private val TAG:String?="LocationLiveForTracking"
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationRequest: com.google.android.gms.location.LocationRequest? = null

    private val _locationData: MutableLiveData<Location?> = MutableLiveData(null)
        val locationData: LiveData<Location?> = _locationData
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
        this.location = location
        location?.let { location ->
            value = LocationDetails(location.longitude.toFloat(), location.latitude.toFloat())
            this._locationData.value=location
            EventBus.getDefault()
                .post(LocationDetails(location.longitude.toFloat(), location.latitude.toFloat()))
            Log.i(
                "Location is",
                location.longitude.toString() + "***" + location.latitude.toString()
            )
        }
    }

    public fun gitLocation(callback: (LocationDetails) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            Log.i(TAG,location!!.longitude.toString()+" "+location!!.latitude.toString())
            callback(LocationDetails(this.location!!.longitude.toFloat(),this.location!!.longitude.toFloat()))
        }, 1000)
    }

    internal fun startLocationUpdate() {
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
        createLocationRequest()
        fusedLocationClient.requestLocationUpdates(
            locationRequest!!,
            locationCallback,
            Looper.getMainLooper()
        )
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

    companion object {
        val ONE_MINUTE: Long = 6000
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest?.interval = 60000
        locationRequest?.fastestInterval = 60000 / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

}