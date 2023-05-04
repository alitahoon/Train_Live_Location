package com.example.trainlivelocation.utli

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.data.ApiService
import com.example.domain.entity.Location_Response
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.TrackTrainCustomNotificationLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class TrackTrainService() : LifecycleService() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var job: Job = Job()
    val trainId:Int?= trainID
    var _locationLLiveDate=MutableLiveData<Location_Response>()
    private var notificationManager: NotificationManager? = null
    lateinit var notificationCustomLayout: TrackTrainCustomNotificationLayoutBinding
    private val TAG: String = "GetTrainLocationService"

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var context: Context

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
        val notification = NotificationCompat.Builder(this, CHANNEL_ID!!)
            .setContentTitle("Train Location Update")
            .setContentText("Location Latitude --> ${locationResponse?.latitude!!.latitude}\nLocation longitude --> ${locationResponse?.longitude?.longitude}")
            .setSmallIcon(com.google.android.material.R.drawable.ic_clear_black_24)
            .setOnlyAlertOnce(true) // Set to true to update notification without showing popup
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID)
        }
        return notification.build()
    }

    fun bindNotificationItem() {
        // Set the custom layout for the notification item using data binding
        notificationCustomLayout =
            DataBindingUtil.inflate<TrackTrainCustomNotificationLayoutBinding>(
                LayoutInflater.from(this),
                R.layout.track_train_custom_notification_layout,
                null,
                false
            )
        notificationCustomLayout.services=this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Perform desired actions here
        Log.i(TAG, "GetTrainLocationService....")
        // Create a new handler and runnable to fetch data from the API
        handler = Handler()
        runnable = Runnable {
            fetchDataFromApi(trainID)
            handler.postDelayed(runnable, 10000) // Fetch data every minute
        }

        // Start the handler to continuously fetch data from the API
        handler.post(runnable)
        return START_STICKY
    }


    internal fun setTrainId(trainId: Int?) {
        trainID = trainId

    }

    companion object {
        private var trainID: Int? = null
        private val CHANNEL_ID: String? = "125"
        private val NOTIFICATION_ID: Int? = 110
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
                if (response.isSuccessful) {
                    Log.i(TAG, "success  ${response.body()}")
                    _locationLLiveDate.value=response.body()
                    startForeground(NOTIFICATION_ID!!, getNotification(response.body()!!))
                } else {
                    Log.e(TAG, "${response.message()}")
                }
            } catch (e: Exception) {
                // Handle the exception here
            }
        }

    }
}