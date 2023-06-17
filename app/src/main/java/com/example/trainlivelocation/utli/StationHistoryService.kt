package com.example.trainlivelocation.utli

import Resource
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.domain.entity.Location_Response
import com.example.domain.entity.StationHistoryAlarmEntity
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.TrainConverterDistanceModel
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import com.google.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class StationHistoryService : LifecycleService() {
    private var job: Job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val TAG = "StationHistoryService"

    private var stationIDTrainGoingTo: Int? = null
    var _locationLLiveDate = MutableLiveData<Location_Response>()
    private var notificationManager: NotificationManager? = null
    lateinit var notificationCustomLayout: RemoteViews

    private var trainLocationsDitanceAfter = arrayListOf<TrainConverterDistanceModel>()
    private var trainLocationsDitanceBefore = arrayListOf<TrainConverterDistanceModel>()

    private var stationsList = arrayListOf<StationResponseItem>()


    private var currantTime = Date()
    private var trainSpeed: Double? = null


    private var currantTrainLocation = LatLng()

    @Inject
    lateinit var insertNewStationHistroyItemToDatabase: InsertNewStationHistroyItemToDatabase

    @Inject
    lateinit var getUserCurrantLocationJustOnce: GetUserCurrantLocationJustOnce

    @Inject
    lateinit var getAllStations: GetAllStations

    @Inject
    lateinit var getStationById: GetStationById

    @Inject
    lateinit var context: Context


    @Inject
    lateinit var getStationHistroyItemsFromDatabase: GetStationHistroyItemsFromDatabase


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        bindNotificationItem()
        startForeground(NOTIFICATION_ID!!,getStationAlarmNotification())
        //first we should compute train speed
        coroutineScope.launch {
            getUserCurrantLocationJustOnce() {
                when (it) {
                    is Resource.Success -> {
                        Log.i(TAG, "${it.data}")
                        currantTrainLocation = LatLng(it.data.latitude, it.data.longitude)
                        currantTime = Calendar.getInstance().time

                        //getting  train location at currant time
                        coroutineScope.launch {
                            getAllStations() {
                                when (it) {
                                    is Resource.Success -> {
                                        Log.i(TAG, "${it.data}")
                                        stationsList = it.data

                                        //get distance between train and stations
                                        for (station in stationsList) {
                                            trainLocationsDitanceBefore.add(
                                                TrainConverterDistanceModel(
                                                    getUserCurrantTrainIntoSharedPrefrences()!!,
                                                    getDistanceInKM(
                                                        currantTrainLocation.lat,
                                                        currantTrainLocation.lng,
                                                        station.latitude,
                                                        station.longitude
                                                    )
                                                )
                                            )
                                        }

                                    }
                                    is Resource.Failure -> {
                                        Log.i(TAG, "${it.error}")
                                    }
                                    is Resource.Loading -> {
                                        Log.i(TAG, "getting stations....")
                                    }
                                    else -> {}
                                }
                            }

                        }
                    }

                    is Resource.Loading -> {
                        Log.i(TAG, "getting train Location....")
                    }

                    is Resource.Failure -> {
                        Log.i(TAG, "${it.error}")
                    }
                    else -> {

                    }
                }
            }
            // waiting 30 seconds for getting the second location
            delay(30000)
            getUserCurrantLocationJustOnce() {
                when (it) {
                    is Resource.Success -> {
                        Log.i(TAG, "Location after 30 seconds ${it.data}")

                        //first we should detect train side
                        //get distance between train and stations after 30s
                        for (station in stationsList) {
                            trainLocationsDitanceAfter.add(
                                TrainConverterDistanceModel(
                                    station.id,
                                    getDistanceInKM(
                                        currantTrainLocation.lat,
                                        currantTrainLocation.lng,
                                        station.latitude,
                                        station.longitude
                                    )
                                )
                            )
                        }

                        //get nearby station before
                        var nearbyStationBefore =
                            trainLocationsDitanceBefore.minByOrNull { it.distance }!!.trainId

                        //get nearby station After
                        var nearbyStationAfter =
                            trainLocationsDitanceAfter.minByOrNull { it.distance }!!.trainId

                        //decide side
                        for (i in 1..trainLocationsDitanceBefore.size - 1) {
                            if (trainLocationsDitanceBefore.get(i).distance <= trainLocationsDitanceAfter.get(i).distance
                                &&
                                trainLocationsDitanceBefore.get(i).trainId == trainLocationsDitanceAfter.get(i).trainId
                                && trainLocationsDitanceBefore.get(i).trainId == nearbyStationBefore &&
                                trainLocationsDitanceAfter.get(i).trainId == nearbyStationAfter
                            ) {
                                //the station is going to this station
                                stationIDTrainGoingTo = trainLocationsDitanceBefore.get(i).trainId
                            }

                        }


                        //now we will compute time it takes between fetching tow locations
                        val duration: Double = java.time.Duration.between(
                            currantTime.toInstant(),
                            Calendar.getInstance().time.toInstant()
                        ).toHours().toDouble()

                        Log.i(TAG, "Duration $duration")

                        //here we are getting distance
                        val distance: Double = getDistanceInKM(
                            currantTrainLocation.lat,
                            currantTrainLocation.lng,
                            it.data.latitude,
                            it.data.longitude
                        ).toDouble()

                        // compute train  Speed = Distance / Time
                        trainSpeed = (distance / duration)
                        Log.i(TAG, "Train Speed ${trainSpeed} KM/H")
                        getStationPostion()
                    }

                    is Resource.Loading -> {
                        Log.i(TAG, "getting train Location after 30 seconds....")
                    }

                    is Resource.Failure -> {
                        Log.i(TAG, "${it.error}")
                    }
                    else -> {

                    }
                }
            }

        }





    }

    fun getStationPostion(){
        //here will set alarm for the stations after that station
        //first get station postion
        coroutineScope.launch {
            Log.i(TAG, "stationIDTrainGoingTo ${stationIDTrainGoingTo}")
            getStationById(stationIDTrainGoingTo) {
                when (it) {
                    is Resource.Loading -> {
                        Log.i(TAG, "getting station data...")
                    }

                    is Resource.Success -> {
                        Log.i(TAG, "${it.data}")
                        for (i in it.data.Postion..stationsList.size - 1) {
                            //that are the station which we shoude get it's data and store it as alarms in database
                            coroutineScope.launch {
                                insertNewStationHistroyItemToDatabase(
                                    StationHistoryAlarmEntity(
                                        distance = trainLocationsDitanceBefore[i].distance!!,
                                        stationName = stationsList[i].name,
                                        discription = getStationDecriptionByID(
                                            trainLocationsDitanceBefore[i].trainId
                                        )!!,
                                        duration = getDurationBetweenTrainAndStation(
                                            trainSpeed!!,
                                            trainLocationsDitanceAfter[i].distance
                                        )
                                    )
                                ) {
                                    when (it) {
                                        is Resource.Failure -> {
                                            Log.e(TAG, "${it.error}")
                                        }
                                        is Resource.Success -> {
                                            Log.i(TAG, "${it.data}")

                                        }
                                        is Resource.Loading -> {
                                            Log.e(TAG, "Adding station history item to database")
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }

                    is Resource.Failure -> {
                        Log.e(TAG, "${it.error}")
                    }
                    else -> {}
                }
            }
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

    fun getDurationBetweenTrainAndStation(trainSpeed: Double, stationDistance: Double): Double {
        return trainSpeed / stationDistance
    }

    fun getStationHistoryItemsfromdatabase(){
        coroutineScope.launch (Dispatchers.IO){
            getStationHistroyItemsFromDatabase(){
                when(it){
                    is Resource.Loading->{
                        Log.i(TAG,"getting station history alarms...")
                    }
                    is Resource.Failure->{
                        Log.e(TAG,"${it.error}")
                    }
                    is Resource.Success->{
                        Log.i(TAG,"${it.data}")
                        setData(it.data[0].stationName,it.data[0].duration)
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
            packageName,R.layout.station_history_notification
        )
    }
    fun setData(stationname:String?,stationDuration:Double?){
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
                    .setSmallIcon(R.drawable.logo_icon_white)
                    .setContentTitle("${stationname}")
                    .setContentText("Elapsed time: ${elapsedTime?.div(1000)} seconds")

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notificationId, notificationBuilder.build())

                // Schedule the next update
                handler.postDelayed(this, updateIntervalMillis)
            }
        }

        handler.postDelayed(updateRunnable, updateIntervalMillis)
    }
}