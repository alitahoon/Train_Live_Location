package com.example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.domain.entity.LocationDetails
import org.greenrobot.eventbus.EventBus

class LocationTrackBackgroundService : LifecycleService() {
    companion object {
        private val CHANNEL_ID:String? = "124"
        private val NOTIFICATION_ID:Int? = 106
    }

    private lateinit var locationLive: LocationLive
    private val TAG: String? = "LocationTrackForegroundService"
    private var notificationManager: NotificationManager? = null
    private var loction: LocationDetails? = null
    override fun onCreate() {
        super.onCreate()
        locationLive = LocationLive(this)
        locationLive.startLocationUpdate()
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Background location Track",
                NotificationManager.IMPORTANCE_HIGH
            )
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
        })
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }
}