package com.example.data

import Resource
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class GetCurrantLocationJustOnce(private val context:Context) {
    private val TAG: String?="GetCurrantLocationJustOnce"
    private lateinit var  locationRequest:LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var  locationCallback:LocationCallback




    fun createLocationRequest(){
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun startGettingLocation(){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG,"Please allow location access")
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }

    fun getLocationCallBacks(result: (Resource<Location>) -> Unit){
         locationCallback = object : LocationCallback() {
             override fun onLocationResult(locationResult: LocationResult) {
                 super.onLocationResult(locationResult)
                 val location = locationResult.locations.first()
                 Log.e(TAG,"from LocationCallBacks location: ${location}")
                 // Handle the location update
                 if (isLocationEnabled()){
                     result.invoke(Resource.Success(location))
                 }else{
                     result.invoke(Resource.Failure("Please Enable Location..."))
                 }
                 // Stop location updates
                 fusedLocationClient.removeLocationUpdates(this)
             }


        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

}