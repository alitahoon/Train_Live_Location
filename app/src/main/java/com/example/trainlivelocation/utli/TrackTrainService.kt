package com.example.trainlivelocation.utli

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.RemoteViews
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
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TrackTrainService() : LifecycleService() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var job: Job = Job()
//    val trainId: Int? = trainID
    var _locationLLiveDate = MutableLiveData<Location_Response>()
    private var notificationManager: NotificationManager? = null
    lateinit var notificationCustomLayout: RemoteViews
    private val TAG: String = "TrackTrainService"

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        bindNotificationItem()
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
        val packageName = applicationContext.packageName
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID!!).setContentTitle("Train Location Update")
                .setContentText("Location Latitude --> ${locationResponse?.latitude!!.latitude}\nLocation longitude --> ${locationResponse?.longitude?.longitude}")
                .setSmallIcon(com.google.android.material.R.drawable.ic_clear_black_24)
                .setOnlyAlertOnce(true) // Set to true to update notification without showing popup
                .setPriority(NotificationCompat.PRIORITY_HIGH).setCustomContentView(notificationCustomLayout)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID)
        }
        return notification.build()
    }

    fun bindNotificationItem() {
        // Set the custom layout for the notification item using data binding
//        notificationCustomLayout = TrackTrainCustomNotificationLayoutBinding.inflate(
//            LayoutInflater.from(this), null, false
//        )
        notificationCustomLayout=RemoteViews(
            packageName, R.layout.track_train_custom_notification_layout
        )

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Perform desired actions here
        trainID = intent!!.getIntExtra("trainId",0)
        Log.i(TAG, "GetTrainLocationService....")
        // Create a new handler and runnable to fetch data from the API
        handler = Handler()
        runnable = Runnable {
            fetchDataFromApi(trainID)
            handler.postDelayed(runnable, 5000) // Fetch data every 5 seconds
        }

        // Start the handler to continuously fetch data from the API
        handler.post(runnable)
        return START_STICKY
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
                    _locationLLiveDate.value = response.body()
                    setData(trainId)
                    startForeground(NOTIFICATION_ID!!, getNotification(response.body()!!))
                } else {
                    Log.e(TAG, "${response.message()}")
                }
            } catch (e: Exception) {
                // Handle the exception here
            }
        }

    }

    fun setData(trainId: Int?) {
        notificationCustomLayout!!.setTextViewText(R.id.track_train_custom_notification_train_txt_id,"TrainId : ${trainID}")
        _locationLLiveDate.observe(this, androidx.lifecycle.Observer {
            Log.i(
                "setAddressFromLocation",
                "${it.longitude.longitude.toDouble()},${it.latitude.latitude.toDouble()}"
            )
            notificationCustomLayout!!.setTextViewText(R.id.track_train_custom_notification_train_longitude_value,"Longitude : ${it.longitude.longitude}")
            notificationCustomLayout!!.setTextViewText(R.id.track_train_custom_notification_train_latitude_value,"Longitude : ${it.latitude.latitude}")
            val geocoder: Geocoder
            val addresses: List<Address>?
            geocoder = Geocoder(context, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                it.longitude.longitude.toDouble(), it.latitude.latitude.toDouble(), 4
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            val address: String =
                addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            if (addresses[0] == null) {
                val city: String = addresses!![0].locality
                val state: String = addresses!![0].adminArea
                val country: String = addresses!![0].countryName
                val postalCode: String = addresses!![0].postalCode
                val knownName: String =
                    addresses!![0].featureName // Only if available else return NULL
                notificationCustomLayout!!.setTextViewText(R.id.track_train_custom_notification_train_address_value,"Address : ")
            } else {
                //get first name of state
                val stateArr = addresses!![0].adminArea.split(" ")
                notificationCustomLayout!!.
                setTextViewText(R.id.track_train_custom_notification_train_address_value,"Address : ${addresses!![0].locality},${stateArr[0]},${addresses!![0].countryName}")

            }
        })

    }
}