package com.example.trainlivelocation.utli

import Resource
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.ApiService
import com.example.domain.entity.Location_Response
import com.example.domain.entity.StationAlarmEntity
import com.example.domain.usecase.GetLiveLoctationFromApi
import com.example.domain.usecase.GetStationAlarmsFromDatabase
import com.example.trainlivelocation.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject


@AndroidEntryPoint
class StationAlarmService() : LifecycleService() {
    //    private lateinit var handler: Handler
//    private lateinit var runnable: Runnable
    private var job: Job = Job()

    //    val trainId: Int? = trainID
    private var _locationLLiveDate = MutableLiveData<Location_Response>()
    var locationLLiveDate: LiveData<Location_Response> = _locationLLiveDate
    private var notificationManager: NotificationManager? = null
    lateinit var notificationCustomLayout: RemoteViews
    private val TAG: String = "StationAlarmService"
    var _locationStateFlow : MutableStateFlow<Location_Response> = MutableStateFlow(
        Location_Response(0.0,0.0)
    )


    val eventBus: EventBus = EventBus.getDefault()


    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var getLiveLoctationFromApi: GetLiveLoctationFromApi

    @Inject
    lateinit var context: Context

    private var stations= arrayListOf<StationAlarmEntity>()

    @Inject
    lateinit var getStationAlarmsFromDatabase: GetStationAlarmsFromDatabase

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        // Unregister from the event bus
        EventBus.getDefault().unregister(this)
    }


    override fun onCreate() {
        super.onCreate()
        bindNotificationItem()
        subscribeToEvent()
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Background Train Location Track",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = ContextCompat.getColor(this, R.color.DarkPrimaryColor) // Set the desired color here
            notificationManager?.createNotificationChannel(notificationChannel)

        }
        observeTrainLocation()
    }

    fun getNotification(): Notification {
        val packageName = applicationContext.packageName
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID!!).
            setContentTitle("Train Location Update")
                .setSmallIcon(R.drawable.app_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(this, R.color.DarkPrimaryColor)) // Set the desired color here
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
        notificationCustomLayout = RemoteViews(
            packageName, R.layout.station_alarm_notification
        )

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)


        return START_STICKY
    }

    fun observeTrainLocation(){
        //getAllStations
        val coroutineScope = CoroutineScope(Dispatchers.Main + job)
        var distance= 0.0
        coroutineScope.launch (Dispatchers.IO){
            getStationAlarmsFromDatabase(){
                coroutineScope.launch (Dispatchers.Main){
                    when(it){
                        is Resource.Loading->{
                            Log.i(TAG,"getting stations from database...")
                        }
                        is Resource.Success->{
                            Log.i(TAG,"stations from database ${it.data}")
                            for (station in it.data){
                                _locationStateFlow.collect{
                                    distance=getDistanceInKM(it.longitude!!,it.latitude!!,station.latitude!!,station.longitude!!)
                                    Log.i(TAG,"distance between station ${station.name } and train $distance")
                                    setData(station.name,distance.toInt(),station.distance)
                                }
                            }
                        }
                        is Resource.Failure->{
                            Log.e(TAG,"${it.error}")

                        }

                        else -> {}
                    }
                }
            }
        }
    }


    companion object {
        private var trainID: Int? = null
        private val CHANNEL_ID: String? = "126"
        private val NOTIFICATION_ID: Int? = 111
    }





    fun setData(stationName: String?, distance: Int, alarmDistance: Int) {
        var c=0;
        Log.i(TAG,"from setData station ${stationName} , distance ${distance} , Alarm distance ${alarmDistance}")
        if (distance!! >= alarmDistance!!){
            notificationCustomLayout!!.setTextViewText(
                R.id.stationAlarmstationName,
                "Track distance for station ${stationName}"
            )
            notificationCustomLayout!!.setTextViewText(
                R.id.stationAlarmstationDistance,
                "Distance to the station is  ${distance} KM"
            )
        }else {
                notificationCustomLayout!!.setTextViewText(
                    R.id.stationAlarmstationName,
                    "Station ${stationName} is almost nearby"
                )
                notificationCustomLayout!!.setTextViewText(
                    R.id.stationAlarmstationDistance,
                    "Distance to the station is  ${distance} KM"
                )
        }

        startForeground(NOTIFICATION_ID!!, getNotification())

        }

    // Subscribe to the event bus
    fun subscribeToEvent() {
        EventBus.getDefault().register(this)
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

    @Subscribe
    fun onMyEvent(trainLocation: Resource<Location_Response>) {
        // Handle the event here
        when(trainLocation){
            is Resource.Loading->{
                Log.i(TAG,"getting Location ")
            }
            is Resource.Success->{
                _locationStateFlow.value=trainLocation.data
            }
            is Resource.Failure->{
                Log.e(TAG,"${trainLocation.error}")
            }
            else -> {

            }
        }
    }
}