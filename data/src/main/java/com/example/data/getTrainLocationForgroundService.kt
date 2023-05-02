package com.example.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.Location_Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
@AndroidEntryPoint
class getTrainLocationForgroundService(private val apiService: ApiService,private val context: Context) : LifecycleService() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private  var job: Job=Job()
    private var notificationManager: NotificationManager? = null
    private val TAG: String = "getTrainLocationForgroundService"

    override fun onCreate() {
        super.onCreate()
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Background Train Location Track From API",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }

    }
    fun getNotification(locationResponse: Location_Response): Notification {
        val notification = NotificationCompat.Builder(this,CHANNEL_ID!!)
            .setContentTitle("Train Location Update")
            .setContentText("Location Latitude --> ${locationResponse?.latitude}\nLocation longitude --> ${locationResponse?.longitude}")
            .setSmallIcon(com.google.android.material.R.drawable.ic_clear_black_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID)
        }
        return notification.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Perform desired actions here
        Log.i(TAG,"getTrainLocationForgroundService....")

        return START_STICKY
    }



    internal fun setTrainId(trainId: Int?) {
        trainID = trainId
        // Create a new handler and runnable to fetch data from the API
        handler = Handler()
        runnable = Runnable {
            fetchDataFromApi(trainId)
            handler.postDelayed(runnable, 10000) // Fetch data every minute
        }

        // Start the handler to continuously fetch data from the API
        handler.post(runnable)
    }

    companion object {
        private var trainID: Int? = null
        private val CHANNEL_ID:String? = "125"
        private val NOTIFICATION_ID:Int? = 110
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Stop the handler when the service is destroyed
        stopSelf()
    }

    private fun fetchDataFromApi(trainId: Int?) {
        val coroutineScope = CoroutineScope(Dispatchers.Main + job)

        coroutineScope.launch {
            try {
                val response = apiService.GetLocation(trainId!!)
                if (response.isSuccessful){
                    Log.i(TAG,"success  ${response.body()}")
                    startForeground(NOTIFICATION_ID!!,getNotification(response.body()!!))
                }else{
                    Log.e(TAG,"${response.message()}")
                }
            } catch (e: Exception) {
                // Handle the exception here
            }
        }

    }
}