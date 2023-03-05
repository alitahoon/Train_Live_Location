package com.example.trainlivelocation.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.domain.entity.Location_Response
import com.example.domain.usecase.GetLiveLoctationFromApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GetLocationServiceui  : LifecycleService() {
    companion object {
        private val CHANNEL_ID: String? = "123"
        private val NOTIFICATION_ID: Int? = 105
    }

    //    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
//        throwable.printStackTrace()
//    }
    @Inject
    lateinit var getLiveLoctationFromApi: GetLiveLoctationFromApi

    //    private val job = SupervisorJob()
//    private val scope = CoroutineScope(Dispatchers.Main + job+coroutineExceptionHandler)
    private var supervisorJob = SupervisorJob(parent = null)
    private val TAG: String? = "LocationTrackForegroundService"
    private var notificationManager: NotificationManager? = null
    private var loction: Location_Response? = null
    private var trainid: Int? = null
    override fun onCreate() {
        super.onCreate()
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Background location Track",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }

    }

    fun setTrainID(trainID: Int?) {
        Log.e(TAG, "trainid set")
        this.trainid = trainID
    }


    fun onNewLocation() {
        Log.e(TAG, "onNewLocation")
        Log.e(TAG, "Get Location From Api")

        val serviceJob = lifecycleScope.launch{
            Log.e(TAG, "hh")
            var result = getLiveLoctationFromApi(trainid!!)
            if (result.isSuccessful) {
                if (result.body() != null) {
                    loction = result.body()
                    Log.e(
                        TAG,
                        "longitude " + loction?.longitude?.longitude.toString() +
                                " latitude" + loction?.latitude?.latitude.toString()
                    )
                }
                startForeground(NOTIFICATION_ID!!, getNotification())

            } else {
                Log.e(TAG, result.message())
            }
        }
//        scope.launch {
//
//        }


    }

    fun requsetLocationTimer() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                onNewLocation()
            }
        }, 0, 2000)
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
        requsetLocationTimer()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
    }
}