package com.example.trainlivelocation.utli

import Resource
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
import com.example.domain.usecase.GetStationAlarmsFromDatabase
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.TrackTrainCustomNotificationLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TrackTrainService() : LifecycleService() {
    //    private lateinit var handler: Handler
//    private lateinit var runnable: Runnable
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

    @Inject
    lateinit var getStationAlarmsFromDatabase: GetStationAlarmsFromDatabase

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

    fun getNotification(): Notification {
        val packageName = applicationContext.packageName
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID!!).setContentTitle("Train Location Update")
                .setSmallIcon(R.drawable.app_logo)
                .setOnlyAlertOnce(true) // Set to true to update notification without showing popup
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomContentView(notificationCustomLayout)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID)
        }
        return notification.build()
    }

    fun getStationAlarmNotification(stationName: String?): Notification {
        val packageName = applicationContext.packageName
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID!! + 1)
                .setContentTitle("${stationName} is almost nearby ....")
                .setSmallIcon(R.drawable.app_logo)
                .setOnlyAlertOnce(true) // Set to true to update notification without showing popup
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500)) // Set custom vibration pattern
                .setCustomContentView(notificationCustomLayout)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID + 1)
        }
        return notification.build()
    }

    fun bindNotificationItem() {
        // Set the custom layout for the notification item using data binding
//        notificationCustomLayout = TrackTrainCustomNotificationLayoutBinding.inflate(
//            LayoutInflater.from(this), null, false
//        )
        notificationCustomLayout = RemoteViews(
            packageName, R.layout.track_train_custom_notification_layout
        )

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Perform desired actions here
        trainID = intent!!.getIntExtra("trainId", 0)
        trainID
        // Create a new handler and runnable to fetch data from the API
        fetchDataFromApi(trainID)
//
//        handler = Handler()
//        runnable = Runnable {
//            Log.i(TAG, "GetTrainLocationService.... :${trainID}")
//            handler.postDelayed(runnable, 2000) // Fetch data every 1 seconds
//        }

//         Start the handler to continuously fetch data from the API
//        handler.post(runnable)
        return START_STICKY
    }


    companion object {
        private var trainID: Int? = null
        private val CHANNEL_ID: String? = "125"
        private val NOTIFICATION_ID: Int? = 110
    }

    override fun onDestroy() {
        super.onDestroy()
//        handler.removeCallbacks(runnable) // Stop the handler when the service is destroyed
        stopSelf()
    }

    private fun fetchDataFromApi(trainId: Int?) {
        val coroutineScope = CoroutineScope(Dispatchers.Main + job)
        coroutineScope.launch {
            try {
                Log.i(TAG, "From courtine scope")
                val response = apiService.GetLocation(trainId!!)
                Log.i(TAG, "success")
                if (response.isSuccessful) {
                    Log.i(TAG, "success  ${response.body()}")
                    _locationLLiveDate.value = response.body()
                    setData(trainId)
                } else {
                    Log.e(TAG, "${response.message()}")
                }
            } catch (e: Exception) {
                // Handle the exception here
                Log.i(TAG, "${e.message}")
            }
        }

    }

    fun setData(trainId: Int?) {
        notificationCustomLayout!!.setTextViewText(
            R.id.track_train_custom_notification_train_txt_id,
            "TrainId : ${trainID}"
        )
        _locationLLiveDate.observe(this, androidx.lifecycle.Observer {
            Log.i(
                "setAddressFromLocation",
                "${it.longitude},${it.latitude}"
            )
            notificationCustomLayout!!.setTextViewText(
                R.id.track_train_custom_notification_train_longitude_value,
                "Longitude : ${it.longitude}"
            )
            notificationCustomLayout!!.setTextViewText(
                R.id.track_train_custom_notification_train_latitude_value,
                "Longitude : ${it.latitude}"
            )
            val geocoder: Geocoder
            val addresses: List<Address>?
            geocoder = Geocoder(context, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                it.longitude, it.latitude, 4
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
                notificationCustomLayout!!.setTextViewText(
                    R.id.track_train_custom_notification_train_address_value,
                    "Address : "
                )
            } else {
                //get first name of state
                val stateArr = addresses!![0].adminArea.split(" ")
                notificationCustomLayout!!.setTextViewText(
                    R.id.track_train_custom_notification_train_address_value,
                    "Address : ${addresses!![0].locality},${stateArr[0]},${addresses!![0].countryName}"
                )

            }
        })
        startForeground(NOTIFICATION_ID!!, getNotification())

        //check for alarm
        val courtineScope = CoroutineScope(Dispatchers.Main)
        courtineScope.launch {
            getStationAlarmsFromDatabase() {
                when (it) {
                    is Resource.Loading -> {
                        Log.i(TAG, "getting Alarms from Database...")
                    }
                    is Resource.Failure -> {
                        Log.i(TAG, "${it.error}")
                    }
                    is Resource.Success -> {
                        for (station in it.data) {
                            //observe location of train
                            _locationLLiveDate.observe(
                                this@TrackTrainService,
                                androidx.lifecycle.Observer {
                                    if (it != null) {
                                        //compute distance
                                        val distance:Int = getDistanceInKM(it.latitude, it.longitude,station.latitude,station.longitude).toInt()
                                        Log.i(TAG,"distance between staion ${station.name} and train = ${distance}")
                                        if (distance <= station.distance){
                                            getStationAlarmNotification(station.name)
                                        }
                                    }
                                })
                        }
                    }

                    else -> {

                    }
                }
            }
        }


        fetchDataFromApi(trainId)
    }


    private fun getDistanceInKM(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): Double {
        var results = FloatArray(1)
        Log.e(TAG, "startLat ${startLat}")
        Log.e(TAG, "startLon ${startLon}")
        Log.e(TAG, "endLat ${endLat}")
        Log.e(TAG, "endLon ${endLon}")

        Location.distanceBetween(startLat, startLon, endLat, endLon, results)
        Log.e(TAG, "distance In Kilo Meter${results[0]}")
        return results[0].toDouble() / 1000
    }
}