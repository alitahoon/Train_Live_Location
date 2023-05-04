package com.example.data

import Resource
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.domain.entity.LocationDetails
import com.google.android.gms.location.LocationServices
import org.greenrobot.eventbus.EventBus


class userLocation (private val context:Context){
    private val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val TAG:String?="userLocation"
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    @SuppressLint("MissingPermission")
    public fun getLocation(activity:Activity,callback: (LocationDetails)->Unit){
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener { location->
                        if (location != null) {
                            EventBus.getDefault().post(LocationDetails(location.longitude.toFloat(), location.latitude.toFloat()))
                            callback(LocationDetails(location.longitude.toFloat(), location.latitude.toFloat()))
                            Log.i(TAG,"Location passed successfully")
                        }

                    }
            }else
            {
                enableGps()
                Log.e(TAG,"Please enable location")

            }
    }
    fun  getLocationWithLocationManger(result: (Resource<LocationDetails>) -> Unit){
        // Get LocationManager object
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        // Create a criteria object to retrieve provider
        val criteria = Criteria()
        // Get the name of the best provider
        val provider = locationManager!!.getBestProvider(criteria, true)
        // Get Current Location
        val myLocation = if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            var myLocation=locationManager!!.getLastKnownLocation(provider!!)
            val myLatitude = myLocation!!.latitude
            val myLongitude = myLocation!!.longitude
            result.invoke(Resource.Success(LocationDetails(myLongitude.toFloat(),myLongitude.toFloat())))
        }

    }

    private fun enableGpsProvider():Boolean {
        var pacman: PackageManager = context.getPackageManager();
        var pacInfo: PackageInfo? = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (e: PackageManager.NameNotFoundException) {
            return false; //package not found
        }

        if (pacInfo != null) {
            for (actInfo in pacInfo.receivers) {
                //test if recevier is exported. if so, we can toggle GPS.
                if (actInfo.name == "com.android.settings.widget.SettingsAppWidgetProvider" && actInfo.exported) {
                    return true
                }
            }
        }
        return false
    }
    fun enableGps(){
        val intent = Intent("android.location.GPS_ENABLED_CHANGE")
        intent.putExtra("enabled", true)
        context.sendBroadcast(intent)
    }

}