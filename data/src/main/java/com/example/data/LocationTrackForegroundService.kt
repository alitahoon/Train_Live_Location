package com.example.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.domain.entity.LocationDetails
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.greenrobot.eventbus.EventBus

class LocationTrackForegroundService : LifecycleService() {
    companion object {
        private val CHANNEL_ID:String? = "123"
        private val NOTIFICATION_ID:Int? = 105
    }

    private var supervisorJob = SupervisorJob(parent = null)
    private lateinit var locationLive: LocationLive
    private val TAG: String? = "LocationTrackForegroundService"
    private var notificationManager: NotificationManager? = null
    private var loction: LocationDetails? = null
    override fun onCreate() {
        super.onCreate()
        locationLive = LocationLive(this)
        locationLive = LocationLive(this)
        var  locationRequest= LocationRequest()
        locationRequest?.interval = 6000
        locationRequest?.fastestInterval = 6000 / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationLive.startLocationUpdate(locationRequest)
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Background location Track",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
        onNewLocation()

    }

    fun onNewLocation() {
        locationLive.observe(this, Observer {
            Log.i(TAG, it.longitude.toString() + " " + it.latitude.toString())
            loction = it
            EventBus.getDefault().post(LocationDetails(
                latitude = loction?.latitude!!,
                longitude = loction?.longitude!!
            ))
            startForeground(NOTIFICATION_ID!!,getNotification())
        })
    }

    fun getNotification(): Notification {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID!!)
            .setContentTitle("Train Location Update")
            .setContentText("Location Latitude --> ${loction?.latitude}\nLocation longitude --> ${loction?.longitude}")
            .setSmallIcon(com.google.android.material.R.drawable.ic_clear_black_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID!!)
        }
            return notification.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()

    }
}