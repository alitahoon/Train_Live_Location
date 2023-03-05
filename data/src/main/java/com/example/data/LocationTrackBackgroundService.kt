package com.example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.Location_Request
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject
@AndroidEntryPoint
class LocationTrackBackgroundService : LifecycleService() {
    companion object {
        private val CHANNEL_ID:String? = "124"
        private val NOTIFICATION_ID:Int? = 106
    }
    @Inject
    lateinit var  apiService: ApiService
    var trainId: Int? = null
    var userId: Int? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job + coroutineExceptionHandler)
    private lateinit var locationLive: LocationLive
    private val TAG: String? = "LocationTrackForegroundService"
    private var loction: LocationDetails? = null
    override fun onCreate() {
        super.onCreate()

        locationLive = LocationLive(this)
        locationLive.startLocationUpdate()
        onNewLocation()

    }
    fun setTrainId_userId(trainid:Int?,userId:Int?){
        this.trainId=trainid
        this.userId=userId
        Log.e(TAG,trainid.toString()+" "+userId.toString())
    }

    fun onNewLocation() {
        Log.e(TAG,"onNewLocation")
        locationLive.observe(this, Observer {
            Log.i(TAG, it.longitude.toString() + " " + it.latitude.toString())
            loction = it
            EventBus.getDefault().post(LocationDetails(
                latitude = loction?.latitude!!,
                longitude = loction?.longitude!!
            ))
            Log.e(TAG,loction?.latitude.toString()+" "+loction?.longitude.toString())
            scope.launch {
                //post location in api
                var result=apiService.AddLocation(Location_Request(loction!!.latitude,loction!!.longitude,trainId!!,userId!!))
                if (result.isSuccessful){
                    if (result.body()!=null){
                        Log.e(TAG,"Location send to api")
                    }
                }
            }



        })
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        job.cancel()
    }
}