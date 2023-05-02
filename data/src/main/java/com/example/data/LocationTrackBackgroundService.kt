package com.example.data

import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.Location_Request
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LocationTrackBackgroundService(private val apiService: ApiService) : LifecycleService() {
    private lateinit var handler: Handler
    private lateinit var runnable: java.lang.Runnable
    private  var job: Job=Job()


    companion object {
        var train: Int? = null
        var user: Int? = null
        private val CHANNEL_ID:String? = "124"
        private val NOTIFICATION_ID:Int? = 106
    }

    private lateinit var locationLive: LocationLive
    private val TAG: String? = "LocationTrackForegroundService"
    private var loction: LocationDetails? = null
    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        locationLive = LocationLive(this)
        var  locationRequest=LocationRequest()
        locationRequest?.interval = 6000
        locationRequest?.fastestInterval = 6000 / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationLive.startLocationUpdate(locationRequest)
        onNewLocation()

    }
    fun setTrainId_userId(trainid:Int?,userId:Int?){
        train=trainid
        user=userId
        Log.e(TAG,trainid.toString()+" "+userId.toString())
    }

    fun onNewLocation() {
        Log.e(TAG,"onNewLocation")
        locationLive.observe(this, Observer {
            Log.i(TAG, it.longitude.toString() + " " + it.latitude.toString())
            loction = it
            uploadLocationToApi(Location_Request(it.longitude,it.latitude,train!!,user!!))
            EventBus.getDefault().post(LocationDetails(
                latitude = loction?.latitude!!,
                longitude = loction?.longitude!!
            ))
            Log.e(TAG,loction?.latitude.toString()+" "+loction?.longitude.toString())




        })
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }
    private fun uploadLocationToApi(locationRequest: Location_Request) {
        val coroutineScope = CoroutineScope(Dispatchers.Main + job)

        coroutineScope.launch {
            try {
                val response = apiService.AddLocation(locationRequest)
                if (response.isSuccessful){
                    if (response.body()!=null){
                        Log.i(TAG,"Location uploaded successfully  ${response.body()}")
                    }
                }else{
                    Log.e(TAG,"${response.message()}")
                }
            } catch (e: Exception) {
                // Handle the exception here
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        locationLive.stopLocationLiveUpdate()
        stopSelf()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMyEvent(locationDetails: LocationDetails) {
        Log.i(TAG,"Location uploaded successfully..")
        uploadLocationToApi(Location_Request(locationDetails.latitude,locationDetails.longitude,train!!,user!!))
    }
}