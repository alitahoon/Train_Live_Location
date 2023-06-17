package com.example.trainlivelocation.utli

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.data.ApiService
import com.example.data.LocationLive
import com.example.data.LocationLiveForTracking
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.Location_Request
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class LocationTrackBackgroundService() : LifecycleService() {
    private lateinit var handler: Handler
    private lateinit var runnable: java.lang.Runnable
    lateinit var  myObserver:Observer<LocationDetails>
    private var job: Job = Job()

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var locationLiveForTracking: LocationLiveForTracking

    @Inject
     lateinit var locationLive: LocationLive

    companion object {
        var train: Int? = null
        var user: Int? = null
        private val CHANNEL_ID: String? = "124"
        private val NOTIFICATION_ID: Int? = 106
        lateinit var wakeLock: PowerManager.WakeLock
        private var loction: LocationDetails? = null
    }



    private val TAG: String? = "LocationTrackBackgroundService"
    private var notificationManager: NotificationManager? = null
    override fun onCreate() {
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Background location Track",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
        startForeground(NOTIFICATION_ID!!,getNotification())


        super.onCreate()
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                acquire()
            }
        }

        var locationRequest = LocationRequest()
        locationRequest?.interval = 6000
        locationRequest?.fastestInterval = 6000 / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationLive.startLocationUpdate(locationRequest)
//        Log.i(TAG,"location from locationLiveForTracking")
//        locationLiveForTracking.startLocationUpdate()
//
        onNewLocation()
    }

    fun setTrainId_userId(trainid: Int?, userId: Int?) {
        train = trainid
        user = userId
        Log.i(TAG, "set data from locationLiveForTracking")
        Log.e(TAG, trainid.toString() + " " + userId.toString())

    }

    fun getNotification(): Notification {
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID!!)
                .setContentTitle("Train Location Update")
                .setContentText("Location is sharing now Thank You...")
//                .setContentText("Location Latitude --> ${loction?.latitude}\nLocation longitude --> ${loction?.longitude}")
                .setSmallIcon(com.google.android.material.R.drawable.ic_clear_black_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(null) // Set sound to null to disable notification sound
                .setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID!!)
        }
        return notification.build()
    }


    fun onNewLocation() {
        Log.e(TAG,"onNewLocation")
        myObserver=Observer<LocationDetails>{
            Log.i(TAG, it.longitude.toString() + " " + it.latitude.toString())
            loction = it
            uploadLocationToApi(Location_Request(it.longitude,it.latitude, train!!, user!!))
            EventBus.getDefault().post(LocationDetails(
                latitude = loction?.latitude!!,
                longitude = loction?.longitude!!
            ))
            Log.e(TAG, loction?.latitude.toString()+" "+ loction?.longitude.toString())


        }
        locationLive.observe(this, myObserver)
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val trainID=intent!!.getIntExtra("trainId",0)
        val userID=intent.getIntExtra("userId",0)
        setTrainId_userId(trainID,userID)
        Log.i(TAG,"trainID ${trainID} , userID ${userID}")
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
        locationLive.removeObserver(myObserver)
        wakeLock.release()
        stopSelf()
    }


}