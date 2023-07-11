package com.example.trainlivelocation.ui

import Resource
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LocationLive
import com.example.domain.entity.*
import com.example.domain.usecase.*
import com.example.trainlivelocation.utli.LocationTrackBackgroundService
import com.example.trainlivelocation.utli.SingleLiveEvent
import com.example.trainlivelocation.utli.TrackLocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class TrackLocationFeatureViewModel @Inject constructor(
    private val context: Context,
    private val getLiveLoctationFromApi: GetLiveLoctationFromApi,
    private var getLocationTrackForegroundService: GetLocationTrackForegroundService,
    private val getUserLocation: GetUserLocation,
    private val startLocationUpdate: StartLocationUpdate,
    private val stopLocationUpdate: StopLocationUpdate,
    private val locationLive: LocationLive,
    private val getUserCurrantLocationJustOnce: GetUserCurrantLocationJustOnce,
    private val gettingTrainlocationFromApi: GettingTrainlocationFromApi,
    private val getAllStationsFromDatabase: GetAllStationsFromDatabase,
    private val getAllStations: GetAllStations,
    private val insertNewStationToDatabase: InsertNewStationToDatabase,
    private val insertnewDirctionRouteInDatabase: InsertnewDirctionRouteInDatabase,
    private val getDirctionRoutesFromDatabase: GetDirctionRoutesFromDatabase,
    private val getLocationDirctionFromOpenRouteService: GetLocationDirctionFromOpenRouteService

) : ViewModel() {
    private var TAG: String? = "TrackLocationFeatureViewModel"

    var btnDetailsFloatingButton = SingleLiveEvent<Boolean>()

    private var MAP_VIEW_Bundle: Bundle? = null

    private val _userLocationMuta: MutableLiveData<LocationDetails?> = MutableLiveData(null)
    val userLocation: LiveData<LocationDetails?> = _userLocationMuta

    private val _nearbyStation: MutableLiveData<Resource<StationResponseItem>?> = MutableLiveData(null)
    val nearbyStation: LiveData<Resource<StationResponseItem>?> = _nearbyStation


    private val _trainLocation = MutableStateFlow<Location_Response>(Location_Response(0.0,0.0))
    val trainLocation= _trainLocation


    private val _userCurrantLocationJustOnce: MutableLiveData<Resource<Location>?> = MutableLiveData(null)
    val userCurrantLocationJustOnce: LiveData<Resource<Location>?> = _userCurrantLocationJustOnce

    private val _stations: MutableLiveData<Resource<ArrayList<StationResponseItem>>?> =
        MutableLiveData(null)
    val stations: LiveData<Resource<ArrayList<StationResponseItem>>?> = _stations

    private val _getStationsFromDatabase: MutableLiveData<Resource<ArrayList<StationItemEntity>>?> =
        MutableLiveData(null)
    val getStationsFromDatabase: LiveData<Resource<ArrayList<StationItemEntity>>?> = _getStationsFromDatabase


    private val _insertingStationsToDatabase: MutableLiveData<Resource<String>?> =
        MutableLiveData(null)
    val insertingStationsToDatabase: LiveData<Resource<String>?> = _insertingStationsToDatabase

    val userLiveLocation = locationLive

    val _distanceMuta: MutableLiveData<Double?> = MutableLiveData(null)
    val distanceLiveData: LiveData<Double?> = _distanceMuta

    lateinit var trackLocationListener: TrackLocationListener
    val _trainLocationMuta: MutableLiveData<Location_Response?> =
        MutableLiveData(null)

    val trainLocationLive: LiveData<Location_Response?> =
        _trainLocationMuta

    var btnTrackLocationFeature = SingleLiveEvent<Boolean>()
    var txtChooseTrainIdClicked = SingleLiveEvent<Boolean>()
    var trainid = MutableLiveData<String>()

    private lateinit var activity: Activity
    var locationForegrondservice: Intent? = null

    private val _dirction: MutableLiveData<Resource<OpenRouteDirectionResult>?> =
        MutableLiveData(null)
    val dirction: LiveData<Resource<OpenRouteDirectionResult>?> = _dirction

    private val _insertRoutes: MutableLiveData<Resource<String>?> =
        MutableLiveData(null)
    val insertRoutes: LiveData<Resource<String>?> = _insertRoutes

    private val _getRoutes: MutableLiveData<Resource<ArrayList<RouteDirctionEntity>>?> =
        MutableLiveData(null)
    val getRoutes: LiveData<Resource<ArrayList<RouteDirctionEntity>>?> = _getRoutes


    fun onBtnDetailsFloatingButton(view: View){
        btnDetailsFloatingButton.value = true
    }
    fun getMAP_VIEW_KEY(): Bundle? {
        MAP_VIEW_Bundle?.putString("MapViewBundleKey", "102")
        return MAP_VIEW_Bundle
    }

    fun setbaseActivity(baseActivity: Activity) {
        activity = baseActivity
    }

    fun setLocationFromMap(location: Location_Response){
        val mapSharedPreferences: SharedPreferences = context.getSharedPreferences("location",
            Context.MODE_PRIVATE)

        val editor = mapSharedPreferences.edit()
        val gson = Gson()
        val locationGson = gson.toJson(location)

        editor.putString("Location", locationGson)
        editor.commit()
    }

    fun getLocationSharedPrefrence(): Location_Response{
        val locationSharedPreferences: SharedPreferences =
            context.getSharedPreferences("location", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = locationSharedPreferences.getString("Location", gson.toJson(Location_Response(30.062959005017905,31.2472764196547)))
        return gson.fromJson(json, Location_Response::class.java)
    }

    fun getLocationDirctions(
        origin: LatLng,
        destination: LatLng
    ) {
        viewModelScope.launch {
            _dirction.value = Resource.Loading
            val child1 = launch(Dispatchers.IO) {
                getLocationDirctionFromOpenRouteService(origin, destination) {
                    val child2 = launch(Dispatchers.Main) {
                        _dirction.value = it
                        Log.i(TAG, "from getLocationDirections method")
                    }
                }
            }
            child1.join()
        }
    }

    fun insertingRoutesInDatabase(routeDirctionEntity: RouteDirctionEntity){
        viewModelScope.launch {
            _insertRoutes.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                insertnewDirctionRouteInDatabase(routeDirctionEntity){
                    val child2=launch(Dispatchers.Main) {
                        _insertRoutes.value=it
                    }
                }
            }
            child1.join()
        }
    }
    fun gettingRoutesFromDatabase(){
        viewModelScope.launch {
            _getRoutes.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                getDirctionRoutesFromDatabase(){
                    val child2=launch(Dispatchers.Main) {
                        _getRoutes.value=it
                    }
                }
            }
            child1.join()
        }
    }

    public fun onBtnTrackTrainLocationClicked(view: View) {
        btnTrackLocationFeature.postValue(true)
    }
    fun onTxtChooseTrainIdClicked(view: View){
        txtChooseTrainIdClicked.postValue(true)
    }

    fun setLocationForgroundServices() {
//        viewModelScope.launch {
//            Log.e(TAG, "setLocationForgroundServices")
//            getLocationTrackForegroundService(trainid?.toInt())
//            locationForegrondservice =
//                Intent(activity, getLocationTrackForegroundService::class.java)
//        }
    }

    public fun getTrainLocationFromApi() {
        viewModelScope.launch {

            getLiveLoctationFromApi(18){
                Log.i(TAG,"${it}")
            }
        }
    }

    fun startTrackLocationForgroundService() {
        Log.e(TAG, "startTrackLocationForgroundService")
        setLocationForgroundServices()
        activity.startService(locationForegrondservice)
    }

    fun stopTrackLocationForgroundService() {
        activity.stopService(locationForegrondservice)
    }

    fun getUserCurrantLocation() {
        viewModelScope.launch {
//            getUserLocation(){
//                location ->
//                _userLocationMuta.postValue(location)
//            }
            startLocationUpdate(6000)
        }
    }




    //new flow for tracking location
    fun startGettingUserLocation(){

        var locationRequest = LocationRequest()
        locationRequest?.interval = 6000
        locationRequest?.fastestInterval = 6000 / 4
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationLive.startLocationUpdate(locationRequest)
    }

    fun stopGettingUserLocation() {
        Log.e(TAG,"startObservingLocation")
        locationLive.stopLocationLiveUpdate()
    }

    fun gettingTrainLocaion(trinID:Int?){
            viewModelScope.launch {
                Log.i(TAG,"trainId : ${trainid}")
                val child1=launch (Dispatchers.IO){
                    gettingTrainlocationFromApi(trinID!!){
                        val child2=launch (Dispatchers.Main){
                            when(it){
                                is Resource.Loading->{
                                    Log.i(TAG,"getting train location")
                                }
                                is Resource.Success->{
                                    Log.i(TAG,"train location ---> ${it.data}")
                                    _trainLocation.value=it.data
                                    gettingTrainLocaion(trinID)
                                }
                                is Resource.Failure->{
                                    Log.e(TAG,"${it.error}")
                                }
                                else -> {}
                            }
                        }
                    }
                }
                child1.join()
            }

    }


    fun gettingUserCurrantLocationJustOnce(){
        _userCurrantLocationJustOnce.value=Resource.Loading
        viewModelScope.launch {
            getUserCurrantLocationJustOnce{
                _userCurrantLocationJustOnce.value=it
            }
        }
    }

    //station fun



    fun getAllStation() {
        viewModelScope.launch {
            _stations.value = Resource.Loading
            val child1 = launch(Dispatchers.IO) {
                getAllStations {
                    val child2 = launch(Dispatchers.Main) {
                        _stations.value = it
                    }
                }
            }
            child1.join()
        }
    }

    fun gettingStationsFromDatabase(){
        viewModelScope.launch {
            _getStationsFromDatabase.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                getAllStationsFromDatabase(){
                    val child2=launch(Dispatchers.Main) {
                        _getStationsFromDatabase.value=it
                    }
                }
            }
            child1.join()
        }
    }


    fun insertingNewStationsToDatabase(stationItemEntity: StationItemEntity){
        viewModelScope.launch {
            _insertingStationsToDatabase.value=Resource.Loading
            val child1=launch (Dispatchers.IO){
                insertNewStationToDatabase(stationItemEntity){
                    val child2=launch(Dispatchers.Main) {
                        _insertingStationsToDatabase.value=it
                    }
                }
            }
            child1.join()
        }
    }
    fun getttingNearbyStation(trainLocation:LatLng){
        var distanceBetweenTrainAndStaions = arrayListOf<StationDistanceModel>()
            _nearbyStation.value=Resource.Loading
        viewModelScope.launch {
            val child1=launch (Dispatchers.IO){
                getAllStations{
                    val child2=launch (Dispatchers.Main){
                        when(it){
                            is Resource.Success->{
                                Log.i(TAG,"${it.data}")
                                Log.i(TAG,"train location ${trainLocation}")
                                for (staion in it.data){
                                    distanceBetweenTrainAndStaions!!.add(
                                        StationDistanceModel(staion!!,
                                    getDistanceInKM(
                                        trainLocation.latitude,trainLocation.longitude,
                                        staion.latitude,staion.longitude
                                    ))
                                    )
                                }
                                _nearbyStation.value=Resource.Success(distanceBetweenTrainAndStaions!!.minByOrNull { it.distance }!!.station)
                            }
                            is Resource.Loading->{
                                Log.i(TAG, "Getting stations")
                            }
                            is Resource.Failure->{
                                Log.e(TAG, "${it.error}")
                                _nearbyStation.value=Resource.Failure("${it.error}")
                            }

                            else -> {}
                        }
                    }
                }
            }
            child1.join()
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
}