package com.example.trainlivelocation.ui

import Resource
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.*
import com.example.domain.usecase.*
import com.example.trainlivelocation.R
import com.example.trainlivelocation.utli.LocationTrackBackgroundService
import com.example.trainlivelocation.utli.StationHistoryService
import com.example.trainlivelocation.utli.getUserCurrantTrainIntoSharedPrefrences
import com.example.trainlivelocation.utli.getuserModelFromSharedPreferences
import com.google.gson.Gson
import com.google.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val insertNewStationHistroyItemToDatabase: InsertNewStationHistroyItemToDatabase,
    private val getLiveLoctationFromApi: GetLiveLoctationFromApi,
    private val context: Context,
    private val getStationById: GetStationById,
    private val getAllStations: GetAllStations,
    private val getStationHistroyItemsFromDatabase: GetStationHistroyItemsFromDatabase,
    private val getUserCurrantLocationJustOnce: GetUserCurrantLocationJustOnce,
    private val getLocationDirctionFromOpenRouteService: GetLocationDirctionFromOpenRouteService
) :ViewModel(){
    val switchStatestationHistroyAlarms = ObservableBoolean()
    val switchStatePostsNotification = ObservableBoolean()
    val switchStatePostsCommentsNotification = ObservableBoolean()
    val switchStateMessagesNotification = ObservableBoolean()
    val switchStateSharingLocation = ObservableBoolean()
    var  stationHistoryService:Intent?= null
    private var trainLocationsDitanceAfter = arrayListOf<TrainConverterDistanceModel>()
    private var trainLocationsDitanceBefore = arrayListOf<TrainConverterDistanceModel>()


    private val TAG:String="SettingsViewModel"
    private var stationIDTrainGoingTo: Int? = null
    var _locationLLiveDate = MutableLiveData<Location_Response>()
    private var stationsList = arrayListOf<StationResponseItem>()
    private var currantTrainLocation = LatLng()
    private var trainLocationAfter30s = LatLng()

    private var currantTime = Date()
    private var trainSpeed: Double? = null
    private var stationsDistatnceList = arrayListOf<StationDistanceModel>()
    private val _switchStationHistoryState = MutableLiveData<Boolean>()
    val switchStationHistoryState: LiveData<Boolean> get() = _switchStationHistoryState

    private val _startServices = MutableLiveData<Boolean>(false)
    var startServices: LiveData<Boolean> = _startServices

    private val _switchtraintrackState = MutableLiveData<Boolean>()
    val switchtraintrackState: LiveData<Boolean> get() = _switchtraintrackState

    private val _switchStationAlarmState = MutableLiveData<Boolean>()
    val switchStationAlarmState: LiveData<Boolean> get() = _switchStationAlarmState

    private val _switchShareLocationState = MutableLiveData<Boolean>()
    val switchShareLocationState: LiveData<Boolean> get() = _switchShareLocationState

    fun onSwitchStationHistoryState(checked: Boolean) {
        _switchStationHistoryState.value = checked
    }
    fun onSwitchtraintrackState(checked: Boolean) {
        _switchtraintrackState.value = checked
    }

    fun onSwitchStationAlarmState(checked: Boolean) {
        _switchStationAlarmState.value = checked
    }

    fun onSwitchShareLocationState(checked: Boolean) {
        _switchShareLocationState.value = checked
    }


    // Observer for switchState
    fun onSwitchStatestationHistroyAlarms() {
        if (switchStatestationHistroyAlarms.get()) {
            // Switch is ON
            //start history service

        } else {
            // Switch is OFF
            //stop history service

        }
    }


    fun onSwitchStatePostsNotification() {
        if (switchStatePostsNotification.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }

    fun switchStatePostsCommentsNotification() {
        if (switchStatePostsCommentsNotification.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }

    fun onSwitchStateMessagesNotification() {
        if (switchStateMessagesNotification.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }

    fun onSwitchStateSharingLocation() {
        if (switchStateSharingLocation.get()) {
            // Switch is ON
        } else {
            // Switch is OFF
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDistanceBetweenUserInTrainAndStation(){
        viewModelScope.launch {
            getUserCurrantLocationJustOnce() {
                when (it) {
                    is Resource.Success -> {
                        Log.i(TAG, "${it.data}")
                        currantTrainLocation = LatLng(it.data.latitude, it.data.longitude)
                        currantTime = Calendar.getInstance().time

                        //getting  train location at currant time
                        viewModelScope.launch {
                            getAllStations() {
                                when (it) {
                                    is Resource.Success -> {
                                        Log.i(TAG, "stations ${it.data}")
                                        stationsList = it.data

                                        //get distance between train and stations
                                        for (station in stationsList) {
                                            trainLocationsDitanceBefore.add(
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
                                        getBestStation()

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
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getBestStation(){
        viewModelScope.launch {
            // waiting 30 seconds for getting the second location
            delay(30000)
            getUserCurrantLocationJustOnce() {
                when (it) {
                    is Resource.Success -> {
                        Log.i(TAG, "Location after 30 seconds ${it.data}")
                        trainLocationAfter30s = LatLng(it.data.latitude, it.data.longitude)
                        //first we should detect train side
                        //get distance between train and stations after 30s
                        for (station in stationsList) {
                            trainLocationsDitanceAfter.add(
                                TrainConverterDistanceModel(
                                    station.id,
                                    getDistanceInKM(
                                        it.data.latitude,
                                        it.data.longitude,
                                        station.latitude,
                                        station.longitude
                                    )
                                )
                            )
                        }
                        computeTrainSpeed()
                        //now we will compute time it takes between fetching tow locations
//                        val duration: Double = java.time.Duration.between(
//                            currantTime.toInstant(),
//                            Calendar.getInstance().time.toInstant()
//                        ).toHours().toDouble()
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


    fun computeTrainSpeed(){
        val duration =  0.00833333333
        Log.i(TAG, "Duration $duration")

        //here we are getting distance
        val distance: Double = getDistanceInKM(
            currantTrainLocation.lat,
            currantTrainLocation.lng,
            trainLocationAfter30s.lat,
            trainLocationAfter30s.lng
        )

        // compute train  Speed = Distance / Time
        trainSpeed = (distance / duration)
        Log.i(TAG, "Train Speed ${trainSpeed} KM/H")

        //get nearby station before
        var nearbyStationBefore =
            trainLocationsDitanceBefore.minByOrNull { it.distance }!!.trainId

        Log.i(TAG,"nearbyStationBefore ${nearbyStationBefore}")

        //get nearby station After
        var nearbyStationAfter =
            trainLocationsDitanceAfter.minByOrNull { it.distance }!!.trainId
        Log.i(TAG,"nearbyStationAfter ${nearbyStationAfter}")
//
//        stationIDTrainGoingTo = trainLocationsDitanceBefore.get(i).trainId
//
//        //decide side
//        if (nearbyStationBefore>nearbyStationAfter){
//
//        }else{
//
//        }
//        for (i in 1..trainLocationsDitanceBefore.size - 1) {
//            Log.i(TAG,"trainLocationsDitanceBefore.get(i).distance---->${trainLocationsDitanceBefore.get(i).distance}")
//            Log.i(TAG,"trainLocationsDitanceAfter.get(i).distance---->${trainLocationsDitanceAfter.get(i).distance}")
//            Log.i(TAG,"trainLocationsDitanceBefore.get(i).trainId---->${trainLocationsDitanceBefore.get(i).trainId}")
//            Log.i(TAG,"trainLocationsDitanceAfter.get(i).trainId---->${trainLocationsDitanceAfter.get(i).trainId}")
//            if (trainLocationsDitanceBefore.get(i).distance <= trainLocationsDitanceAfter.get(i).distance) {
//                //the station is going to this station

//                //here we are getting distance
//                val distance: Double = getDistanceInKM(
//                    currantTrainLocation.lat,
//                    currantTrainLocation.lng,
//                    trainLocationAfter30s.lat,
//                    trainLocationAfter30s.lng
//                )
//
//                // compute train  Speed = Distance / Time
//                trainSpeed = (distance / duration)
//                Log.i(TAG, "Train Speed ${trainSpeed} KM/H")
//                getStationPostion()
//                break
//            }
//            else{
//                Log.e(TAG,"Error getting best station....")
//            }
//
//        }
    }

    fun getStationPostion(){
        //here will set alarm for the stations after that station
        //first get station postion
        viewModelScope.launch {
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
                            viewModelScope.launch {
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

    fun getDurationBetweenTrainAndStation(trainSpeed: Double, stationDistance: Double): Double {
        return trainSpeed / stationDistance
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
        viewModelScope.launch {
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


    fun getTrainLocation(trainId: Int?) {
        // Handle the event here
//        _trainLocationFromService.value = trainLocation
        //find nearby station
        viewModelScope.launch(Dispatchers.Main) {
            getUserCurrantLocationJustOnce(){
                when(it){
                    is Resource.Success->{
                        var trainLocation=it.data
                        viewModelScope.launch (Dispatchers.IO){
                            getAllStations {
                                when (it) {
                                    is Resource.Success -> {
                                        Log.i(TAG, "${it.data}")
                                        for (station in it.data) {
                                            stationsDistatnceList.add(
                                                StationDistanceModel(
                                                    station, getDistanceInKM(
                                                        trainLocation.latitude,
                                                        trainLocation.longitude, station.latitude, station.longitude
                                                    )
                                                )
                                            )
                                        }
                                        Log.i(TAG,"StationDistanceModel   ${stationsDistatnceList}")
                                        viewModelScope.launch (Dispatchers.Main){
                                            //get nearby station
                                            var nearbyStationsdistance =
                                                stationsDistatnceList.minByOrNull { it.distance }!!.distance

                                            Log.i(TAG,"nearbyStationsPostion   ${nearbyStationsdistance}")

                                            val nearbyStations =
                                                stationsDistatnceList.find { it.distance == nearbyStationsdistance}

                                            Log.i(TAG,"nearbyStations   ${nearbyStations}")

                                            val indexOfnearbyStations =
                                                stationsDistatnceList.indexOf(nearbyStations)

                                            Log.i(TAG,"indexOfnearbyStations   ${indexOfnearbyStations}")

                                            var stationToAlarm = stationsDistatnceList.get(indexOfnearbyStations)

                                            var duration =getDurationBetweentrainAndStation(
                                                com.google.android.gms.maps.model.LatLng(
                                                    trainLocation.longitude,
                                                    trainLocation.latitude
                                                ),
                                                com.google.android.gms.maps.model.LatLng(
                                                    stationToAlarm.station.latitude,
                                                    stationToAlarm.station.longitude
                                                )
                                            )
                                            Log.i(TAG,"station to alarm ${stationToAlarm}")
                                            //set alarm
                                            val stationSharedPreferences: SharedPreferences =
                                                context.getSharedPreferences("stationHistory", Context.MODE_PRIVATE)
                                            var editor=stationSharedPreferences.edit()
                                            val gson = Gson()
                                            val json = gson.toJson(stationToAlarm)
                                            editor.putString("stationData",json!!)
                                            editor.commit()
                                        _startServices.value = true
                                    }
                                    }
                                    is Resource.Failure -> {
                                        Log.e(TAG, "${it.error}")
                                    }
                                    is Resource.Loading -> {
                                        Log.i(TAG, "getting stations")
                                    }

                                    else -> {}
                                }
                            }
                        }

                    }
                    is Resource.Failure->{

                    }
                    is Resource.Loading->{

                    }
                    else -> {}
                }
            }

        }
    }
    private fun getDurationBetweentrainAndStation(
        origin: com.google.android.gms.maps.model.LatLng,
        destination: com.google.android.gms.maps.model.LatLng
    ): Double? {
        var duration: Double? = null
        viewModelScope.launch(Dispatchers.IO) {
            getLocationDirctionFromOpenRouteService(origin, destination) {
                when (it) {
                    is Resource.Loading -> {
                        Log.i(TAG, "getting duration ...")
                    }
                    is Resource.Failure -> {
                        Log.e(TAG, "${it.error}")
                    }
                    is Resource.Success -> {
                        Log.i(TAG, "${it.data.duration}")
                        duration = it.data.duration
                    }
                    else -> {}
                }
            }
        }
        return duration
    }



}