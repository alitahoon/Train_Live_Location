package com.example.trainlivelocation.utli

import Resource
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.example.domain.entity.*
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class StationHistoryService : LifecycleService() {
    private var job: Job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val TAG = "StationHistoryService"

    private var stationIDTrainGoingTo: Int? = null
    var _trainLocation = MutableStateFlow<Location_Response?>(null)
    private var notificationManager: NotificationManager? = null
    lateinit var notificationCustomLayout: RemoteViews


    private var stationsList = arrayListOf<StationResponseItem>()

    private var trainSpeed: Double? = null


    private var timerCount = 0
    private lateinit var timerHandler: Handler
    private lateinit var timerRunnable: Runnable


    private var stationsDistatnceList = arrayListOf<StationDistanceModel>()

    @Inject
    lateinit var insertNewStationHistroyItemToDatabase: InsertNewStationHistroyItemToDatabase

    @Inject
    lateinit var getUserCurrantLocationJustOnce: GetUserCurrantLocationJustOnce

    @Inject
    lateinit var getAllStations: GetAllStations

    @Inject
    lateinit var getStationById: GetStationById





    @Inject
    lateinit var getLocationDirctionFromOpenRouteService: GetLocationDirctionFromOpenRouteService

    @Inject
    lateinit var context: Context


    @Inject
    lateinit var getStationHistroyItemsFromDatabase: GetStationHistroyItemsFromDatabase
    override fun onDestroy() {
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
        stopForeground(true) // Remove the service from the foreground
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        timerHandler = Handler()
        createNotificationChannel()
        coroutineScope.launch (Dispatchers.Main){
            getUserCurrantLocationJustOnce{
                when(it){
                    is Resource.Loading->{
                        Log.i(TAG,"getting currant location")
                    }
                    is Resource.Success->{
                        Log.i(TAG,"${it.data}")
                        Log.i(TAG,"from shared ${getStationHistoryAlarm()!!.station}")
//                        var duration =getDurationBetweentrainAndStation(
//                            LatLng(
//                                it.data.latitude,
//                                it.data.longitude
//                            ),
//                           LatLng(
//                                getStationHistoryAlarm()!!.station.latitude,
//                                getStationHistoryAlarm()!!.station.longitude,
//                            )
//                        )

                        coroutineScope.launch(Dispatchers.IO) {
                            getLocationDirctionFromOpenRouteService(  LatLng(
                                it.data.latitude,
                                it.data.longitude
                            ),    LatLng(
                                getStationHistoryAlarm()!!.station.latitude,
                                getStationHistoryAlarm()!!.station.longitude,
                            )) {
                                when (it) {
                                    is Resource.Loading -> {
                                        Log.i(TAG, "getting duration ...")
                                    }
                                    is Resource.Failure -> {
                                        Log.e(TAG, "${it.error}")
                                    }
                                    is Resource.Success -> {
                                        Log.i(TAG, " duration -------> ${it.data.duration}")
//                                        duration = it.data.duration
                                        coroutineScope.launch (Dispatchers.Main){
                                            startNotificationTimer(it.data.duration!!.toInt())
                                        }

                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                    is Resource.Failure->{
                        Log.i(TAG,"${it.error}")
                    }
                    else -> {}
                }
            }
        }
    }
    private fun startNotificationTimer(durationInSeconds:Int?) {
        timerCount = durationInSeconds!!
        timerRunnable = object : Runnable {
            override fun run() {
                timerCount--
                updateNotification(timerCount)
                if (timerCount > 0) {
                    timerHandler.postDelayed(this, 1000) // 1 second interval
                } else {
                    // Timer finished, perform your action here
                    sendHistoryNotification()
                }
            }
        }
        timerHandler.postDelayed(timerRunnable, 1000) // initial delay
    }
    private fun sendHistoryNotification() {
        // Perform history notification
    }

    private fun updateNotification(timerCount: Int) {
        val hours = timerCount / 3600
        val minutes = (timerCount % 3600) / 60
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID!!)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("station Description Notificationion")
            .setContentText("Time remaining for the station ${getStationHistoryAlarm()!!.station.name} \n ${String.format("%02d Hours : %02d Minutes", hours, minutes)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true) // Set to true to update notification without showing popup
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID!!, notificationBuilder.build())
        startForeground(NOTIFICATION_ID!!, notificationBuilder.build())
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID!!,
                "Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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



    fun getStationDecriptionByID(stationID: Int?): String? {
        var description: String? = null
        coroutineScope.launch {
            getStationById(stationID) {
                when (it) {
                    is Resource.Loading -> {
                        Log.i(TAG, "getting station data ....")
                    }
                    is Resource.Success -> {
                        Log.e(TAG, "${it.data}")
                        description = it.data.description
                    }
                    is Resource.Failure -> {
                        Log.i(TAG, "${it.error}")
                    }
                    else -> {}
                }
            }
        }

        return description
    }



//    private fun getDurationBetweentrainAndStation(
//        origin: LatLng,
//        destination:LatLng
//    ): Double? {
//        var duration: Double? = null
//
//        return duration
//    }

    fun getDurationBetweenTrainAndStation(trainSpeed: Double, stationDistance: Double): Double {
        return trainSpeed / stationDistance
    }

    fun getStationHistoryItemsfromdatabase() {
        coroutineScope.launch(Dispatchers.IO) {
            getStationHistroyItemsFromDatabase() {
                when (it) {
                    is Resource.Loading -> {
                        Log.i(TAG, "getting station history alarms...")
                    }
                    is Resource.Failure -> {
                        Log.e(TAG, "${it.error}")
                    }
                    is Resource.Success -> {
                        Log.i(TAG, "${it.data}")
                        setData(it.data[0].stationName, it.data[0].duration)
                    }
                    else -> {}
                }
            }
        }
    }

    fun getNotification(): Notification {
        val packageName = applicationContext.packageName
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID!!).setContentTitle("Stations History")
                .setSmallIcon(R.drawable.app_logo)
                .setOnlyAlertOnce(true) // Set to true to update notification without showing popup
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomContentView(notificationCustomLayout)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID)
        }
        return notification.build()
    }

    companion object {
        private var trainID: Int? = null
        private val CHANNEL_ID: String? = "126"
        private val NOTIFICATION_ID: Int? = 111
    }

    fun getStationAlarmNotification(): Notification {
        val packageName = applicationContext.packageName
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID!! + 1)
                .setContentTitle("Stations History")
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
            packageName, R.layout.station_history_notification
        )
    }

    fun setData(stationname: String?, stationDuration: Double?) {
        val handler = Handler()
        val notificationId = 1
        val updateIntervalMillis = 1000L // 1 second

        // Start the timer
        var elapsedTime = stationDuration
        val updateRunnable = object : Runnable {
            override fun run() {
                elapsedTime = elapsedTime?.plus(updateIntervalMillis)

                // Build the updated notification
                val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID!!)
                    .setContentTitle("${stationname}")
                    .setContentText("Elapsed time: ${elapsedTime?.div(1000)} seconds")
                    .setSmallIcon(R.drawable.app_logo)
                    .setOnlyAlertOnce(true) // Set to true to update notification without showing popup
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(longArrayOf(100, 200, 300, 400, 500)) // Set custom vibration pattern

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notificationId, notificationBuilder.build())
                startForeground(NOTIFICATION_ID!!,notificationBuilder.build()!!)
                // Schedule the next update
                handler.postDelayed(this, updateIntervalMillis)
            }
        }

        handler.postDelayed(updateRunnable, updateIntervalMillis)
    }
}