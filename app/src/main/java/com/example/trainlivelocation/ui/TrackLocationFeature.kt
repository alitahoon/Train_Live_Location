package com.example.trainlivelocation.ui

import Resource
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.*
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentTrackLocationFeatureBinding
import com.example.trainlivelocation.utli.TrackLocationListener
import com.example.trainlivelocation.utli.Train_Dialog_Listener
import com.example.trainlivelocation.utli.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

@AndroidEntryPoint
class TrackLocationFeature : Fragment(), TrackLocationListener, Train_Dialog_Listener,
    OnMapReadyCallback {
    private val _userLocationMuta: MutableLiveData<LocationDetails?> = MutableLiveData(null)
    private val trackLocationFeatureViewModel: TrackLocationFeatureViewModel? by activityViewModels()
    private var binding: FragmentTrackLocationFeatureBinding? = null
    private var mMap: GoogleMap? = null
    lateinit var latlong: LatLng

    var origin1: LatLng? = null// Ramsis(Cairo)
    var destination1: LatLng? = null  // Shubra El-Kheima

    var origin2: LatLng? = null// Qalyoub
    var destination2: LatLng? = null  // Qalyoub Al , Balad

    var origin3: LatLng? = null// al-qanater al-khairieh al-jadideh
    var destination3: LatLng? = null  // Droh

    var origin4: LatLng? = null// shatanov
    var destination4: LatLng? = null  // al-halawasi al-balad

    var origin5: LatLng? = null// al-halawasi
    var destination5: LatLng? = null  // Ashmon


    var origin6: LatLng? = null// al-halawasi
    var destination6: LatLng? = null  // Ashmon

    var origin7: LatLng? = null// al-halawasi
    var destination7: LatLng? = null  // Ashmon

    var origin8: LatLng? = null// al-halawasi
    var destination8: LatLng? = null  // Ashmon

    var origin9: LatLng? = null// al-halawasi
    var destination9: LatLng? = null  // Ashmon

    var origin10: LatLng? = null// al-halawasi
    var destination10: LatLng? = null  // Ashmon

    var origin11: LatLng? = null// al-halawasi
    var destination11: LatLng? = null  // Ashmon

    var origin12: LatLng? = null// al-halawasi
    var destination12: LatLng? = null  // Ashmon


    var origin13: LatLng? = null// al-halawasi
    var destination13: LatLng? = null  // Ashmon

    var origin14: LatLng? = null// al-halawasi
    var destination14: LatLng? = null  // Ashmon

    var origin15: LatLng? = null// al-halawasi
    var destination15: LatLng? = null  // Ashmon

    var origin16: LatLng? = null// al-halawasi
    var destination16: LatLng? = null  // Ashmon

    var origin17: LatLng? = null// al-halawasi
    var destination17: LatLng? = null  // Ashmon

    var origin18: LatLng? = null// al-halawasi
    var destination18: LatLng? = null  // Ashmon

    var origin19: LatLng? = null// al-halawasi
    var destination19: LatLng? = null  // Ashmon

    var origin20: LatLng? = null// al-halawasi
    var destination20: LatLng? = null  // Ashmon


    private val TAG: String? = "TrackLocationFeature"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackLocationFeatureBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = trackLocationFeatureViewModel
            }
        binding?.viewmodel?.trackLocationListener = this
        binding?.lifecycleOwner = this
        trackLocationFeatureViewModel?.setbaseActivity(requireActivity())
        trackLocationFeatureViewModel?.getTrainLocationFromApi()
        scaleUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.enter)
        scaleDownAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.exit)

        setObservers()

        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
    }


    companion object {
        private var scaleUpAnimation: Animation? = null
        private var scaleDownAnimation: Animation? = null
        private var sydnyTrainLocation: LatLng? = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
//        trackLocationFeatureViewModel?.stopTrackLocationForgroundService()
    }

    override fun onBtnClickListener() {
//        trackLocationFeatureViewModel?.btnTrackLocationFeature?.observe(viewLifecycleOwner, Observer {
//            if (it==true){
////                //get location from API
////                Log.e(TAG,"setObservers")
////                trackLocationFeatureViewModel?.getTrainLocationFromApi()
////                trackLocationFeatureViewModel?._trainLocationMuta?.observe(viewLifecycleOwner,
////                    Observer {
////                        Log.e(TAG,"observe")
////                        Log.e(TAG,it?.longitude.toString()+"  "+it?.longitude)
////                    })
//            }
//        })
    }

    fun setObservers() {
        trackLocationFeatureViewModel?.txtChooseTrainIdClicked?.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    var dialog = ChooseTrainDialogFragment(this)
                    var childFragmentManager = getChildFragmentManager()
                    dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
                }
            })

        trackLocationFeatureViewModel?.btnTrackLocationFeature?.observe(
            viewLifecycleOwner,
            Observer {

                if (it == true) {
//                    findNavController().navigate(R.id.action_trackLocationFeature_to_trackTrainInOpenStreetMap)

                    if (binding!!.trackLocationTxtTrainId.text.toString().isEmpty()) {
                        toast("Please Choose train To track")
                    } else {
                        binding!!.trackLocationTxtTrainId.visibility = View.GONE
                        binding!!.trackBtnTrack.visibility = View.GONE
                        binding!!.trackLocationMap.startAnimation(scaleUpAnimation)
                        scaleUpAnimation!!.setAnimationListener(object :
                            Animation.AnimationListener {
                            override fun onAnimationStart(p0: Animation?) {
                                binding!!.trackLocationMap.visibility = View.VISIBLE
                                binding!!.trackLocationMap.onCreate(trackLocationFeatureViewModel?.getMAP_VIEW_KEY())
                                binding!!.trackLocationMap.getMapAsync(this@TrackLocationFeature)
                            }

                            override fun onAnimationEnd(p0: Animation?) {

                            }

                            override fun onAnimationRepeat(p0: Animation?) {

                            }

                        })
                    }


//
//
//
//                    binding?.trackLocationProgressBar?.setVisibility(View.VISIBLE)
//                    var trainLocation: LocationDetails? = null
//                    var userLocation: LocationDetails? = null
//                    var distance: Double? = null
//
//                    //get train location
////                trackLocationFeatureViewModel?.startTrackLocationForgroundService()
////                get location from API
//                    Log.e(TAG, "get train location")
//                    trackLocationFeatureViewModel?.getTrainLocationFromApi()
//                    trackLocationFeatureViewModel?._trainLocationMuta?.observe(viewLifecycleOwner,
//                        Observer {
//                            it?.let {
//                                Log.e(
//                                    TAG,
//                                    it?.longitude.toString() + "  " + it?.latitude.toString()
//                                )
//                                trainLocation =
//                                    LocationDetails(
//                                        it!!.longitude.toFloat(),
//                                        it!!.latitude.toFloat()
//                                    )
//                            }
//
//                        })
//
//
//                    //get user location
//                    Log.e(TAG, "get user location")
//                    trackLocationFeatureViewModel?.getUserCurrantLocation()
//                    _userLocationMuta?.observe(
//                        viewLifecycleOwner,
//                        Observer {
//                            it?.let {
//                                Log.e(
//                                    TAG,
//                                    it?.longitude.toString() + "  " + it?.latitude.toString()
//                                )
//                                userLocation = it
//                            }
//
//                        })
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        if (trainLocation != null && userLocation != null) {
//                            //compute distance between user and train
//                            Log.e(TAG, "compute distance between user and train")
//                            distance = distanceInMeter(
//                                trainLocation?.longitude!!.toDouble(),
//                                trainLocation?.latitude!!.toDouble(),
//                                userLocation?.latitude!!.toDouble(),
//                                userLocation?.longitude!!.toDouble()
//                            )
////                            distance=getDistanceUsingGoogleMapApi(
////                                LatLng(
////                                    trainLocation?.latitude!!.toDouble(),
////                                    trainLocation?.longitude!!.toDouble()
////                                ),
////                                LatLng(
////                                    userLocation?.latitude!!.toDouble(),
////                                    userLocation?.longitude!!.toDouble()
////                                )
////                            )
//
//
//                            if (distance != null) {
//                                Log.e(TAG, "Distance ${distance}")
//                                val bundle = Bundle()
//                                bundle.putFloat("distance", distance!!.toFloat())
//                                binding?.trackLocationProgressBar?.setVisibility(View.INVISIBLE)
////                                findNavController().navigate(
////                                    R.id.action_trackLocationFeature_to_home2,
////                                    bundle
////                                )
//                                val action: NavDirections =
//                                    TrackLocationFeatureDirections.actionTrackLocationFeatureToTrainLocationInMap(
//                                        userLocation!!,
//                                        trainLocation!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                        } else {
//                            Log.e(TAG, "Error")
//                        }
//                    }, 10000)


                }

            })
    }

    fun getDistanceBetweenUserAndTrain(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    private fun computeDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double? {
        val startPoint = Location("locationA")
        startPoint.setLatitude(lat1)
        startPoint.setLongitude(lon1)

        val endPoint = Location("locationB")
        endPoint.setLatitude(lat2)
        endPoint.setLongitude(lon2)

        return startPoint.distanceTo(endPoint).toDouble()

    }

    fun getKilometers(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val PI_RAD = Math.PI / 180.0
        val phi1 = lat1 * PI_RAD
        val phi2 = lat2 * PI_RAD
        val lam1 = long1 * PI_RAD
        val lam2 = long2 * PI_RAD
        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1))
    }

    @Subscribe
    fun getUserLocationLive(locationDetails: LocationDetails) {
        Log.e(
            TAG,
            locationDetails.latitude.toString() + "////" + locationDetails.longitude.toString()
        )
        _userLocationMuta.postValue(locationDetails)
    }

    fun getDistanceUsingGoogleMapApi(point1: LatLng, point2: LatLng): Double? {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }

    private fun distanceInMeter(
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
        Log.e(TAG, "distanceInMeter ${results[0]}")
        return results[0].toDouble()
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding!!.trackLocationTxtTrainId.setText("${trainId}")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var trainId=binding!!.trackLocationTxtTrainId.text.toString().toInt()
        binding!!.trackLocationMap.onResume()
        mMap = googleMap
        mMap?.isMyLocationEnabled()
        mMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        var trainMarker:MarkerOptions?=null
//        val trainMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.train_location)
        addStationsMarkersToMap(mMap!!)
        //new flow
        trackLocationFeatureViewModel!!.gettingUserCurrantLocationJustOnce()
        trackLocationFeatureViewModel!!.userCurrantLocationJustOnce.observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Loading -> {
                        Log.i(TAG, "getting user Location...")
                    }

                    is Resource.Success -> {
                        var isCodeExecuted = true
                        var sydnyUserLocation: LatLng =
                            LatLng(it.data.latitude, it.data.longitude)
                        addMarkersToMap(mMap!!, sydnyUserLocation, "Your Location", "user")

                        //getting train Location
                        trackLocationFeatureViewModel!!.gettingTrainLocaion(trainId)
                        lifecycleScope.launch (Dispatchers.IO){
                            trackLocationFeatureViewModel!!.trainLocation.collect{
                                Log.i(TAG, "Train location from track fragment ----> ${it}")
                                sydnyTrainLocation =
                                    LatLng(it.longitude, it.latitude)

                                if (isCodeExecuted) {
                                    lifecycleScope.launch (Dispatchers.Main){
                                        trainMarker=addMarkersToMap(
                                            mMap!!,
                                            sydnyTrainLocation!!,
                                            "Train Location",
                                            "train"
                                        )
                                    }
                                    isCodeExecuted = false
                                }
                                lifecycleScope.launch(Dispatchers.Main){
                                    moveMarker(trainMarker!!,LatLng(it.latitude,it.longitude))
                                    trackLocationFeatureViewModel!!.gettingTrainLocaion(trainId)
                                }
//                                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydnyUserLocation))
//                                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydnyTrainLocation))
                                // Initialize the GoogleMap instance (googleMap) and add the marker
                                // Set the initial location of the marker
                                // Set the OnCameraMoveListener
//                                        mMap!!.setOnCameraMoveListener {
//                                            val screenTrainPosition =
//                                                mMap!!.projection.toScreenLocation(
//                                                    sydnyTrainLocation!!
//                                                )
//                                            val screenUserPosition =
//                                                mMap!!.projection.toScreenLocation(sydnyUserLocation)
//                                            if (!mMap!!.projection.visibleRegion.latLngBounds.contains(
//                                                    sydnyUserLocation
//                                                ) ||
//                                                !googleMap.projection.visibleRegion.latLngBounds.contains(
//                                                    sydnyTrainLocation!!
//                                                )
//                                            ) {
//                                                val cameraTrainUpdate =
//                                                    CameraUpdateFactory.newLatLng(
//                                                        googleMap.projection.fromScreenLocation(
//                                                            screenTrainPosition
//                                                        )
//                                                    )
//                                                val cameraUserUpdate =
//                                                    CameraUpdateFactory.newLatLng(
//                                                        googleMap.projection.fromScreenLocation(
//                                                            screenUserPosition
//                                                        )
//                                                    )
//                                                mMap!!.moveCamera(cameraTrainUpdate)
//                                                mMap!!.moveCamera(cameraUserUpdate)
//                                            }
//                                        }

                                // Add markers to the map

//                        val cameraPosition: CameraPosition = CameraPosition.Builder()
//                            .target(
//                                sydnyDoctor
//                            ) // Sets the center of the map to location user
//                            .zoom(16F) // Sets the zoom
//                            .bearing(0F) // Sets the orientation of the camera to east
////            .tilt(40F) // Sets the tilt of the camera to 30 degrees
//                            .build() // Creates a CameraPosition from the builder
                                // Calculate bounds that encompass both positions
//                                        val bounds = LatLngBounds.Builder()
//                                            .include(sydnyUserLocation)
//                                            .include(sydnyTrainLocation!!)
//                                            .build()
//
//                                        // Draw a line between the locations
//                                        val polylineOptions = PolylineOptions()
//                                            .add(sydnyUserLocation, sydnyTrainLocation)
//                                            .color(R.color.PrimaryColor)
//                                            .width(10f)
//
//                                        // Animate camera to the calculated bounds
//                                        // Calculate the padding based on the map size and desired zoom level
//                                        val mapWidth = binding!!.trackLocationMap.width
//                                        val mapHeight = binding!!.trackLocationMap.height
//                                        val padding = (Math.max(
//                                            mapWidth,
//                                            mapHeight
//                                        ) * 0.1).toInt() // Optional padding around the bounds (in pixels)
//                                        val cameraUpdate =
//                                            CameraUpdateFactory.newLatLngBounds(bounds, padding)
//                                        mMap?.animateCamera(cameraUpdate)
//                                        mMap?.addPolyline(polylineOptions)


                            }

                        }
                    }

                    is Resource.Failure -> {
                        toast("${it.error}")
                    }

                    else -> {}
                }
            })


        trackLocationFeatureViewModel!!.startGettingUserLocation()
        trackLocationFeatureViewModel!!.userLiveLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) {

            }
        })
    }


    private fun addMarkersToMap(
        mapView: GoogleMap,
        markerSydny: LatLng,
        markerName: String?,
        iconType: String?
    ):MarkerOptions{
        var marker:MarkerOptions?=null
        when (iconType) {
            "station" -> {
                marker=
                    MarkerOptions().flat(true).position(markerSydny).title(markerName)
                        .icon(bitmapDescriptorFromVector(R.drawable.station_in_map))
                mapView.addMarker(marker)
            }
            else -> {
                marker=
                    MarkerOptions().flat(true).position(markerSydny).title(markerName)
                mapView.addMarker(marker)
            }
        }
        return marker
    }

    fun moveMarker(marker:MarkerOptions,newPostion:LatLng){
        marker.position(newPostion)
    }

    private fun bitmapDescriptorFromVector(
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val background: Drawable =
            ContextCompat.getDrawable(requireContext(), vectorDrawableResourceId)!!
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight())
        val vectorDrawable: Drawable =
            ContextCompat.getDrawable(requireContext(), vectorDrawableResourceId)!!
        vectorDrawable.setBounds(
            40,
            20,
            vectorDrawable.getIntrinsicWidth() + 40,
            vectorDrawable.getIntrinsicHeight() + 20
        )
        val bitmap = Bitmap.createBitmap(
            background.getIntrinsicWidth(),
            background.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    fun addStationsMarkersToMap(mapView: GoogleMap) {
        val builder = LatLngBounds.builder()
        var stationSydny = arrayListOf<StationSydny>()
        //get stations from database
        trackLocationFeatureViewModel!!.gettingStationsFromDatabase()
        trackLocationFeatureViewModel!!.getStationsFromDatabase.observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Success -> {
                        Log.i(TAG, "${it.data}")
                        if (it.data.isEmpty()) {
                            trackLocationFeatureViewModel!!.getAllStation()
                            trackLocationFeatureViewModel!!.stations.observe(viewLifecycleOwner,
                                Observer {
                                    when (it) {
                                        is Resource.Loading -> {
                                            Log.i(TAG, "getting from API stations ")
                                        }
                                        is Resource.Failure -> {
                                            Log.i(TAG, "${it.error}")
                                        }
                                        is Resource.Success -> {
                                            Log.i(TAG, "${it.data}")
                                            stationSydny =
                                                genenrateStationsSydny(ArrayList(it.data.distinct()))
                                            //inserting stations to database
                                            for (station in it.data) {
                                                builder.include(
                                                    LatLng(
                                                        station.latitude,
                                                        station.longitude
                                                    )
                                                )
                                                addMarkersToMap(
                                                    mapView,
                                                    LatLng(station.latitude, station.longitude),
                                                    station.name,
                                                    "station"
                                                )
                                                trackLocationFeatureViewModel!!.insertingNewStationsToDatabase(
                                                    StationItemEntity(
                                                        description = station.description,
                                                        latitude = station.latitude,
                                                        longitude = station.longitude,
                                                        name = station.name,
                                                        apiID = station.id,
                                                        Postion = station.Postion,
                                                        nextStation = station.nextStation,
                                                        trainId = 0
                                                    )
                                                )
                                            }
                                            addRouteToMap(mapView, stationSydny)
                                            zoomingOnStations(mMap!!, builder, stationSydny)
                                        }
                                        else -> {}
                                    }
                                })
                        } else {
                            //it all ready cashed before
                            stationSydny =
                                genenrateStationsSydnyFromDatbaseStationsItems(ArrayList(it.data.distinct()))
                            for (station in it.data) {
                                builder.include(LatLng(station.latitude, station.longitude))
                                addMarkersToMap(
                                    mapView,
                                    LatLng(station.latitude, station.longitude),
                                    station.name,
                                    "station"
                                )
                            }
                            addRouteToMap(mapView, stationSydny)
                            zoomingOnStations(mMap!!, builder, stationSydny)
                        }
                    }
                    is Resource.Loading -> {
                        Log.i(TAG, "getting stations ")
                    }
                    is Resource.Failure -> {
                        Log.i(TAG, "${it.error}")
                    }
                    else -> {}
                }
            })
    }

    fun addRouteToMap(mapView: GoogleMap, stations: ArrayList<StationSydny>){
        for (station in stations) {
            if (station.stationName.equals("cairo")) {
                origin1 = station.stationSydnyvalue
            } else if (station.stationName.equals("Shubra El-Kheima")) {
                destination1 = station.stationSydnyvalue
                origin2 = station.stationSydnyvalue
            } else if (station.stationName.equals("Qelyoub")) {
                destination2 = station.stationSydnyvalue
                origin3 = station.stationSydnyvalue
            } else if (station.stationName.equals("Qelyoub Al-Balad")) {
                destination3 = station.stationSydnyvalue
                origin4 = station.stationSydnyvalue
            } else if (station.stationName.equals("Al-Qanatir Al-Khairiya")) {
                destination4 = station.stationSydnyvalue
                origin5 = station.stationSydnyvalue
            } else if (station.stationName.equals("Darwah")) {
                destination5 = station.stationSydnyvalue
                origin6 = station.stationSydnyvalue
            } else if (station.stationName.equals("Shatanof")) {
                destination6 = station.stationSydnyvalue
                origin7 = station.stationSydnyvalue
            } else if (station.stationName.equals("Al Hilwasi Al-Balad")) {
                destination7 = station.stationSydnyvalue
                origin8 = station.stationSydnyvalue
            } else if (station.stationName.equals("Al Hilwasi")) {
                destination8 = station.stationSydnyvalue
                origin9 = station.stationSydnyvalue
            } else if (station.stationName.equals("Ashmoun")) {
                destination9 = station.stationSydnyvalue
                origin10 = station.stationSydnyvalue
            } else if (station.stationName.equals("Samadun")) {
                destination10 = station.stationSydnyvalue
                origin11 = station.stationSydnyvalue
            } else if (station.stationName.equals("Ramlet Alonhab")) {
                destination11 = station.stationSydnyvalue
                origin12 = station.stationSydnyvalue
            } else if (station.stationName.equals("Menouf")) {
                destination12 = station.stationSydnyvalue
                origin13 = station.stationSydnyvalue
            } else if (station.stationName.equals("Al-Hamul")) {
                destination13 = station.stationSydnyvalue
                origin14 = station.stationSydnyvalue
            } else if (station.stationName.equals("Shanwan")) {
                destination14 = station.stationSydnyvalue
                origin15 = station.stationSydnyvalue
            } else if (station.stationName.equals("Shebin El-Kom")) {
                destination15 = station.stationSydnyvalue
                origin16 = station.stationSydnyvalue
            } else if (station.stationName.equals("New Shebin El-Kom")) {
                destination16 = station.stationSydnyvalue
                origin17 = station.stationSydnyvalue
            } else if (station.stationName.equals("Al-Batanoun")) {
                destination17 = station.stationSydnyvalue
                origin18 = station.stationSydnyvalue
            } else if (station.stationName.equals("Tala")) {
                destination18 = station.stationSydnyvalue
                origin19 = station.stationSydnyvalue
            } else if (station.stationName.equals("Tanta")) {
                destination19 = station.stationSydnyvalue
            }

        }

        trackLocationFeatureViewModel!!.gettingRoutesFromDatabase()
        trackLocationFeatureViewModel!!.getRoutes.observe(viewLifecycleOwner, Observer{
            when(it){
                is Resource.Success-> {
                    Log.i(TAG, "${it.data}")
                    if(it.data.isEmpty()){
                        getAllStationsRouteParrllel()
                        trackLocationFeatureViewModel!!.dirction.observe(viewLifecycleOwner, Observer{
                            when(it){
                                is Resource.Success->{
                                    Log.i(TAG, "${it.data}")
                                    trackLocationFeatureViewModel!!.insertingRoutesInDatabase(routeDirctionEntity = RouteDirctionEntity(
                                        polyline = it.data.polyline,
                                        distance = it.data.distance,
                                        duration = it.data.duration
                                    ))
                                    drawPolyLine(mapView, it.data)
                                }
                                is Resource.Loading-> {
                                    Log.i(TAG, "Getting Routes")
                                }
                                is Resource.Failure-> {
                                    Log.e(TAG, "${it.error}")
                                }
                                else->{}
                            }
                        })
                    }
                    else{
                        for(route in it.data){
                            drawPolyLine(mapView, OpenRouteDirectionResult(
                                polyline = route.polyline,
                                distance = route.distance,
                                duration = route.duration
                            )
                            )
                        }
                    }
                }
                is Resource.Loading-> {
                    Log.i(TAG, "Getting Routes")
                }
                is Resource.Failure-> {
                    Log.e(TAG, "${it.error}")
                }
                else->{}
            }
        })
    }

    fun drawPolyLine(mapView: GoogleMap, point: OpenRouteDirectionResult){

            val points = PolyUtil.decode(point.polyline)

            // Draw the polyline on the map
            val polylineOptions = PolylineOptions()
                .color(Color.BLUE)
                .width(5f)
                .addAll(points)
            Log.i(TAG, "draw poly.......")
            mapView.addPolyline(polylineOptions)
    }

    fun getAllStationsRouteParrllel() {
        // Create a CoroutineScope
        val coroutineScope = CoroutineScope(Dispatchers.Main)


        val origin1Distenation1 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin1!!, destination1!!) }
        val origin2Distenation2 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin2!!, destination2!!) }
        val origin3Distenation3 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin3!!, destination3!!) }
        val origin4Distenation4 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin4!!, destination4!!) }
        val origin5Distenation5 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin5!!, destination5!!) }
        val origin6Distenation6 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin6!!, destination6!!) }
        val origin7Distenation7 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin7!!, destination7!!) }
        val origin8Distenation8 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin8!!, destination8!!) }
        val origin9Distenation9 =
            coroutineScope.async { trackLocationFeatureViewModel!!.getLocationDirctions(origin9!!, destination9!!) }
        val origin10Distenation10 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin10!!,
                destination10!!
            )
        }
        val origin11Distenation11 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin11!!,
                destination11!!
            )
        }
        val origin12Distenation12 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin12!!,
                destination12!!
            )
        }
        val origin13Distenation13 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin13!!,
                destination13!!
            )
        }
        val origin14Distenation14 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin14!!,
                destination14!!
            )
        }
        val origin15Distenation15 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin15!!,
                destination15!!
            )
        }
        val origin16Distenation16 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin16!!,
                destination16!!
            )
        }
        val origin17Distenation17 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin17!!,
                destination17!!
            )
        }
        val origin18Distenation18 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin18!!,
                destination18!!
            )
        }
        val origin19Distenation19 = coroutineScope.async {
            trackLocationFeatureViewModel!!.getLocationDirctions(
                origin19!!,
                destination19!!
            )
        }


        coroutineScope.launch(Dispatchers.IO) {
            val result1 = origin1Distenation1.await()
            val result2 = origin2Distenation2.await()
            val result3 = origin3Distenation3.await()
            val result4 = origin4Distenation4.await()
            val result5 = origin5Distenation5.await()
            val result6 = origin6Distenation6.await()
            val result7 = origin7Distenation7.await()
            val result8 = origin8Distenation8.await()
            val result9 = origin9Distenation9.await()
            val result10 = origin10Distenation10.await()
            val result11 = origin11Distenation11.await()
            val result12 = origin12Distenation12.await()
            val result13 = origin13Distenation13.await()
            val result14 = origin14Distenation14.await()
            val result15 = origin15Distenation15.await()
            val result16 = origin16Distenation16.await()
            val result17 = origin17Distenation17.await()
            val result18 = origin18Distenation18.await()
            val result19 = origin19Distenation19.await()
        }

    }

    fun genenrateStationsSydny(stationsList: ArrayList<StationResponseItem>): ArrayList<StationSydny> {
        val stationSydnyList = ArrayList<StationSydny>()
        for (station in stationsList) {
            stationSydnyList.add(
                StationSydny(
                    LatLng(station.latitude, station.longitude), station.name
                )
            )
        }
        return stationSydnyList
    }

    fun genenrateStationsSydnyFromDatbaseStationsItems(stationsList: ArrayList<StationItemEntity>): ArrayList<StationSydny> {
        val stationSydnyList = ArrayList<StationSydny>()
        for (station in stationsList) {
            stationSydnyList.add(
                StationSydny(
                    LatLng(station.latitude, station.longitude), station.name
                )
            )
        }
        return stationSydnyList
    }

    fun zoomingOnStations(
        mapView: GoogleMap,
        builder: LatLngBounds.Builder,
        stationSydny: ArrayList<StationSydny>
    ) {
        // Move camera to include all the markers with zoom
        val bounds = builder.build()
        val padding = 100 // Adjust as needed
        val cameraUpdate =
            CameraUpdateFactory.newLatLngBounds(bounds, padding)
        // Apply both camera updates
        mapView.animateCamera(cameraUpdate)
    }
}