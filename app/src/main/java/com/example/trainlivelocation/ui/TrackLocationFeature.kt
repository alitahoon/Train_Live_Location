package com.example.trainlivelocation.ui

import Resource
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.LocationDetails
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
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
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
        binding?.lifecycleOwner=this
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
        private var sydnyTrainLocation:LatLng?=null
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

                    if (binding!!.trackLocationTxtTrainId.text.toString().isEmpty()){
                        toast("Please Choose train To track")
                    }else{
                        binding!!.trackLocationTxtTrainId.visibility=View.GONE
                        binding!!.trackBtnTrack.visibility=View.GONE
                        binding!!.trackLocationMap.startAnimation(scaleUpAnimation)
                        scaleUpAnimation!!.setAnimationListener(object :Animation.AnimationListener{
                            override fun onAnimationStart(p0: Animation?) {
                                binding!!.trackLocationMap.visibility=View.VISIBLE
                                binding!!.trackLocationMap.onCreate(trackLocationFeatureViewModel?.getMAP_VIEW_KEY())
                                binding!!.trackLocationMap.getMapAsync(this@TrackLocationFeature)
                            }

                            override fun onAnimationEnd(p0: Animation?) {

                            }

                            override fun onAnimationRepeat(p0: Animation?) {

                            }

                        })
                    }

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
        binding!!.trackLocationMap.onResume()
        mMap = googleMap
        mMap?.isMyLocationEnabled()
        mMap?.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
//        val trainMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.train_location)

        //new flow
        trackLocationFeatureViewModel!!.gettingUserCurrantLocationJustOnce()
        trackLocationFeatureViewModel!!.userCurrantLocationJustOnce.observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is Resource.Loading->{
                        Log.i(TAG,"getting user Location...")
                    }

                    is Resource.Success->{
                        var isCodeExecuted = true
                        var sydnyUserLocation: LatLng =
                            LatLng(it.data.latitude,it.data.longitude)
                        mMap!!.addMarker(MarkerOptions().flat(true).position(sydnyUserLocation).title("Your Location"))

                        //getting train Location
                        trackLocationFeatureViewModel!!.gettingTrainLocaion(this)
                        trackLocationFeatureViewModel!!.trainLocation.observe(viewLifecycleOwner,
                            Observer {
                                when(it){
                                    is Resource.Loading->{

                                        Log.i(TAG,"getting Train Location.")
                                        binding!!.trackLocationProgressBar.visibility=View.VISIBLE
                                    }
                                    is Resource.Success->{
                                        Log.i(TAG,"${it.data}")
                                        binding!!.trackLocationProgressBar.visibility=View.GONE
                                        sydnyTrainLocation=
                                            LatLng(it.data.longitude,it.data.latitude)
                                        if (isCodeExecuted){
                                            mMap!!.addMarker(MarkerOptions().flat(true).position(sydnyTrainLocation!!).title("Train Location"))
                                            isCodeExecuted=false
                                        }
//                                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydnyUserLocation))
//                                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydnyTrainLocation))
                                        // Initialize the GoogleMap instance (googleMap) and add the marker
                                        // Set the initial location of the marker
                                        // Set the OnCameraMoveListener
                                        mMap!!.setOnCameraMoveListener {
                                            val screenTrainPosition = mMap!!.projection.toScreenLocation(sydnyTrainLocation!!)
                                            val screenUserPosition = mMap!!.projection.toScreenLocation(sydnyUserLocation)
                                            if (!mMap!!.projection.visibleRegion.latLngBounds.contains(sydnyUserLocation) ||
                                                !googleMap.projection.visibleRegion.latLngBounds.contains(sydnyTrainLocation!!)) {
                                                val cameraTrainUpdate = CameraUpdateFactory.newLatLng(googleMap.projection.fromScreenLocation(screenTrainPosition))
                                                val cameraUserUpdate = CameraUpdateFactory.newLatLng(googleMap.projection.fromScreenLocation(screenUserPosition))
                                                mMap!!.moveCamera(cameraTrainUpdate)
                                                mMap!!.moveCamera(cameraUserUpdate)
                                            }
                                        }

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
                                        val bounds = LatLngBounds.Builder()
                                            .include(sydnyUserLocation)
                                            .include(sydnyTrainLocation!!)
                                            .build()

                                        // Draw a line between the locations
                                        val polylineOptions = PolylineOptions()
                                            .add(sydnyUserLocation, sydnyTrainLocation)
                                            .color(R.color.PrimaryColor)
                                            .width(10f)

                                        // Animate camera to the calculated bounds
                                        // Calculate the padding based on the map size and desired zoom level
                                        val mapWidth = binding!!.trackLocationMap.width
                                        val mapHeight = binding!!.trackLocationMap.height
                                        val padding = (Math.max(mapWidth, mapHeight) * 0.1).toInt() // Optional padding around the bounds (in pixels)
                                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                                        mMap?.animateCamera(cameraUpdate)
                                        mMap?.addPolyline(polylineOptions)

                                        trackLocationFeatureViewModel!!.gettingTrainLocaion(this)

                                    }
                                    is Resource.Failure->{
                                        Log.i(TAG,"${it.error}")
                                    }

                                    else -> {}
                                }
                            })
                    }

                    is Resource.Failure->{
                        toast("${it.error}")
                    }

                    else -> {}
                }
            })


        trackLocationFeatureViewModel!!.startGettingUserLocation()
        trackLocationFeatureViewModel!!.userLiveLocation.observe(viewLifecycleOwner, Observer {
                    if (it != null){

                    }
        })
    }

}