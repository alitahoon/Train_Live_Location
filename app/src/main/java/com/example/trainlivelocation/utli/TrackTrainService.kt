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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.data.ApiService
import com.example.domain.entity.Location_Response
import com.example.domain.usecase.GetLiveLoctationFromApi
import com.example.domain.usecase.GetStationAlarmsFromDatabase
import com.example.domain.usecase.GettingTrainlocationFromApi
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.TrackTrainCustomNotificationLayoutBinding
import com.google.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TrackTrainService() : LifecycleService() {
    //    private lateinit var handler: Handler
//    private lateinit var runnable: Runnable
    private var job: Job = Job()

    private val stopRunningService=MutableStateFlow<Boolean>(false)

    //    val trainId: Int? = trainID
    var _locationLLiveDate = MutableLiveData<Location_Response>()
    var _locationStateFlow :MutableStateFlow<Location_Response> = MutableStateFlow(Location_Response(0.0,0.0))
    private var notificationManager: NotificationManager? = null
    lateinit var notificationCustomLayout: RemoteViews
    private val TAG: String = "TrackTrainService"

    val eventBus: EventBus = EventBus.getDefault()
    val coroutineScope = CoroutineScope(Dispatchers.IO + job)


    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var getLiveLoctationFromApi: GetLiveLoctationFromApi

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
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomContentView(notificationCustomLayout)
                .setOnlyAlertOnce(true) // Set to true to update notification without showing popup

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID)
        }
        return notification.build()
    }

    fun getStationAlarmNotification(stationName: String?) {

        startForeground(NOTIFICATION_ID!!,getNotification())


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
        trainID = getUserCurrantTrainIntoSharedPrefrences()
        // Create a new handler and runnable to fetch data from the API
        fetchDataFromApi(1)
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
        stopRunningService.value=true
//        handler.removeCallbacks(runnable) // Stop the handler when the service is destroyed
        stopForeground(true) // Remove the service from the foreground
        stopSelf()
    }

    private fun fetchDataFromApi(trainId: Int?) {
        coroutineScope.launch (Dispatchers.IO){
            stopRunningService.collect{
                if (it==false){
                    coroutineScope.launch (Dispatchers.IO){
                        Log.i(TAG, "From courtine scope")
                        try {
                            getLiveLoctationFromApi(trainId!!){
                                when(it){
                                    is Resource.Loading->{
                                        Log.i(TAG,"getting train Location From API")
                                    }
                                    is Resource.Success->{
                                        Log.i(TAG, "success  ${it.data}")
//                            _locationLLiveDate.value = it.data!!
                                        _locationStateFlow.value=it.data
                                        val resource=it
                                        coroutineScope.launch (Dispatchers.Main){
                                            eventBus.post(resource.data)
                                            setData(trainId)
                                        }


                                    }
                                    is Resource.Failure->{

                                    }
                                    else -> {

                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Handle the exception here
                            Log.i(TAG, "${e.message}")
                        }
                    }
                }
            }

        }

    }

    fun setData(trainId: Int?) {
        notificationCustomLayout!!.setTextViewText(
            R.id.track_train_custom_notification_train_txt_id,
            "TrainId : ${trainID}"
        )
        coroutineScope.launch(Dispatchers.IO) {
            _locationStateFlow.collect {
                Log.i(
                    "setAddressFromLocation",
                    "${it.longitude},${it.latitude}"
                )
                notificationCustomLayout!!.setTextViewText(
                    R.id.track_train_custom_notification_train_longitude_value,
                    "Longitude : ${it.latitude}"
                )
                notificationCustomLayout!!.setTextViewText(
                    R.id.track_train_custom_notification_train_latitude_value,
                    "Longitude : ${it.longitude}"
                )
                notificationCustomLayout!!.setTextViewText(
                    R.id.track_train_custom_notification_train_address_value,
                    "Address : ${getLocationInfo(it.longitude,it.latitude)}"
                )
                startForeground(NOTIFICATION_ID!!, getNotification())
                fetchDataFromApi(trainId)
            }
        }



//
//        //check for alarm
//        val courtineScope = CoroutineScope(Dispatchers.Main)
//        courtineScope.launch {
//            getStationAlarmsFromDatabase() {
//                when (it) {
//                    is Resource.Loading -> {
//                        Log.i(TAG, "getting Alarms from Database...")
//                    }
//                    is Resource.Failure -> {
//                        Log.i(TAG, "${it.error}")
//                    }
//                    is Resource.Success -> {
//                        for (station in it.data) {
//                            //observe location of train
//                            Log.i(TAG,"Stations ${station}")
//                            _locationLLiveDate.observe(
//                                this@TrackTrainService,
//                                androidx.lifecycle.Observer {
//                                    if (it != null) {
//                                        //compute distance
//                                        val distance:Int = getDistanceInKM( it.longitude,it.latitude,station.latitude,station.longitude).toInt()
//                                        Log.i(TAG,"distance between staion ${station.name} and train = ${distance}")
//                                        if (distance <= station.distance){
//
//                                            getStationAlarmNotification(station.name)
//                                        }
//                                    }
//                                })
//                        }
//                    }
//
//                    else -> {
//
//                    }
//                }
//            }
//        }


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
        Log.e(TAG, "distance In Kilo Meter ${results[0].toDouble() / 1000}")
        return results[0].toDouble() / 1000
    }


    fun getLocationInfo(latitude: Double, longitude: Double): String {
        try {
            val geocoder = Geocoder(applicationContext)
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                if (address == null){
                    return " No Address Available for this place..."
                }else{
                    // Process the address information
                    val city: String = addresses!![0].locality
                    val state: String = addresses!![0].adminArea
                    val country: String = addresses!![0].countryName
                    val postalCode: String = addresses!![0].postalCode
                    val knownName: String = addresses!![0].featureName // Only if available else return NULL
                    return "$city,$state,$country"
                }

            }else{
                return " No Address Available for this place..."
            }
        } catch (e: IOException) {
            if (e.message?.contains("Service not Available") == true) {
                // Handle the "Service not Available" exception
                // Display an error message to the user or provide an alternative solution
                return "Geocoder service is not available."
            } else {
                // Handle other IOExceptions
                // Log the exception or handle it based on your requirements
                return "Error occurred while retrieving location information."
            }
        }

        return "No location information found."
    }
}