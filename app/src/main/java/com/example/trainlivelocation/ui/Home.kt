package com.example.trainlivelocation.ui

import Permission
import Resource
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.*
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.utli.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.math.log2


@AndroidEntryPoint
class Home : Fragment(), Train_Dialog_Listener, OnMapReadyCallback ,OnBackPressedListener {
    private val TAG: String? = "Home";
    private val eventBus: EventBus = EventBus.getDefault()
    private val homeViewModel: HomeViewModel? by activityViewModels()
    private var binding: FragmentHomeBinding? = null
    private val permissionManager = PermissionManager.from(this)
    private var listener: HomeMapListener? = null
    private val _trainLocationFromService: MutableLiveData<Resource<Location_Response>> =
        MutableLiveData()
    var _locationStateFlow: MutableStateFlow<Location_Response> = MutableStateFlow(
        Location_Response(0.0, 0.0)
    )

    var trainLocationFromService: LiveData<Resource<Location_Response>> = _trainLocationFromService

    private var isCameraAnimated: Boolean = false

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove() // Remove the callback when the fragment is destroyed
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeMapListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ActionListener")
        }
        eventBus.register(this)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        eventBus.unregister(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        eventBus.unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            this.viewmodel = homeViewModel
        }



        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()

        userModel = getuserModelFromSharedPreferences(requireContext())
//        if (getDistance() != null) {
//            binding?.homeTxtTrainDistance?.setText(getDistance().toString() + " Meal")
//        }

//        //check if track services is running
//        val serviceClass = TrackTrainService::class.java
//        val isRunning = isServiceRunning(serviceClass)
//        if (isRunning) {
//            // Service is running here we will show the map
//            //start mapView
//            requireContext().stopService(Intent(requireActivity(),
//                TrackTrainService::class.java))
//        } else {
//            // Service is not running
//
//        }

        binding!!.root?.postDelayed({
            if (isMapOpen(requireContext())) {
                listener?.onMapOpened()
                homeViewModel?.geTrainLocation(getUserCurrantTrainIntoSharedPrefrences(requireContext()))
                homeViewModel?.StartHomeTracking()
                binding?.homeTrackTrainIDTxt?.setHint("Stop Map Tracking")
                startHomeMap()
                isCameraAnimated = true
            }
        }, 500) // Delay of 500 milliseconds




//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            permissionManager.request(Permission.Notification)
//                .rationale("We need permission to show Notifications")
//                .checkPermission { granted: Boolean ->
//                    if (granted) {
//                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
//                            .show()
//                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            "No Permission to show notifications",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//        }
    }

    fun startHomeMap(){
        binding!!.homeCardTrainId.visibility = View.GONE
        binding!!.homeCardTrainIcon.visibility = View.GONE
        binding!!.homeMapCardView.visibility = View.VISIBLE
        binding!!.homeMapView.onCreate(homeViewModel!!.getMAP_VIEW_KEY())
        binding!!.homeMapView.getMapAsync(this)
    }

    fun stopHomeMap(){
        binding!!.homeCardTrainId.visibility = View.VISIBLE
        binding!!.homeCardTrainIcon.visibility = View.VISIBLE
        binding!!.homeMapCardView.visibility = View.GONE
    }

    private fun setObservers() {
        homeViewModel!!.sendingNotificationToken.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.e(TAG, "sending token...")
                }
                is Resource.Success -> {
                    token = it.data!!
                    Log.e(TAG, "${it.data}")

                }
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                }
                else -> {
                    Log.e(TAG, "sendingNotificationToken else brunch...")

                }
            }
        })

        homeViewModel?.locationBtn?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = HomeDirections.actionHomeToLocationDialogFragment(userModel!!)
                homeViewModel!!.stopHometracking()
                findNavController().navigate(action)
            }
        })


        homeViewModel?.postsBtn?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                homeViewModel!!.stopHometracking()
                listener!!.onMoveFromHome()
                findNavController().navigate(HomeDirections.actionHomeToPosts(null))
            }
        })


        homeViewModel?.locationCardBtn?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                homeViewModel!!.stopHometracking()
                listener!!.onMoveFromHome()
                findNavController().navigate(R.id.action_home2_to_trainLocationInMap)
            }
        })

        homeViewModel?.btnEmergancyClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = HomeDirections.actionHome2ToEmergency(userModel!!)
                homeViewModel!!.stopHometracking()
                listener!!.onMoveFromHome()
                findNavController().navigate(action)
            }
        })

        homeViewModel?.btnTicketClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = HomeDirections.actionHome2ToTickets(userModel!!)
                homeViewModel!!.stopHometracking()
                listener!!.onMoveFromHome()
                findNavController().navigate(action)
            }
        })

        homeViewModel?.passengersbtnClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = HomeDirections.actionHome2ToPassengers(userModel!!)
                homeViewModel!!.stopHometracking()
                listener!!.onMoveFromHome()
                findNavController().navigate(action)
            }
        })



        homeViewModel?.chooseTrainTxtClicked!!.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                if(isMapOpen(requireContext()) && binding!!.homeTrackTrainIDTxt.hint.equals("Stop Map Tracking")){
                    listener!!.onMoveFromHome()
                    binding!!.homeCardTrainIcon.visibility=View.VISIBLE
                    binding!!.homeCardTrainId.visibility=View.VISIBLE
                    binding!!.homeMapCardView.visibility = View.GONE
                    isCameraAnimated = false
                    binding!!.homeTrackTrainIDTxt.setHint("Choose Train To Track")
                    setMapFlag(requireContext(),false)
                    homeViewModel!!.stopHometracking()
                }
                else{
                    var dialog = ChooseTrainDialogFragment(this)
                    var childFragmentManager = getChildFragmentManager()
                    dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
                }

            }
        })

        homeViewModel?.trainConverterbtnClicked!!.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                listener!!.onMoveFromHome()
                findNavController().navigate(R.id.action_home2_to_news)
            }
        })
    }

    fun getDistance(): Float? {
        return arguments?.getFloat("distance")!!
    }

    companion object {
        var userModel: UserResponseItem? = null
        private var token: String? = null
    }

    fun observeTrainLocationService() {
        homeViewModel!!.trainbackgroundTrackingServices.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    toast("getting service please wait...")
                }
                is Resource.Success -> {


                }

                else -> {}
            }
        })
    }

    override fun onTrainSelected(trainId: Int?, trainDegree: String?) {
        binding!!.homeTrackTrainIDTxt.setHint(trainId!!.toString())
        binding!!.homeTrackTrainIDTxt.setHint("Stop Map Tracking")
        listener!!.onMapOpened()
        homeViewModel!!.StartHomeTracking()
        insertUserCurrantTrainIntoSharedPrefrences(requireContext(), trainId)
        homeViewModel!!.geTrainLocation(trainId)
        setMapFlag(requireContext(),true)
        startHomeMap()
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        binding!!.homeMapView.onResume()
        val mMap = googleMap
        mMap?.isMyLocationEnabled()
        mMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        //first we will get all stations from database if it exits ok if not get it from api and cashing it on database local

        homeViewModel!!.gettingStationsFromDatabase()
        homeViewModel!!.getStationsFromDatabase.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                }
                is Resource.Loading -> {
                    Log.i(TAG, "getting stations from database....")
                }
                is Resource.Success -> {
                    var stationSydny = arrayListOf<StationSydny>()
                    if (it.data.isNotEmpty()) {
                        //it all ready cashed before
                        stationSydny =
                            genenrateStationsSydnyFromDatbaseStationsItems(ArrayList(it.data.distinct()))

                    } else {
                        homeViewModel!!.getAllStation()
                        homeViewModel!!.stations.observe(viewLifecycleOwner, Observer {
                            when (it) {
                                is Resource.Failure -> {
                                    Log.e(TAG, "${it.error}")
                                }
                                is Resource.Loading -> {
                                    Log.i(TAG, "getting stations....")
                                }
                                is Resource.Success -> {
                                    stationSydny =
                                        genenrateStationsSydny(ArrayList(it.data.distinct()))
                                    for (station in it.data) {
                                        homeViewModel!!.insertingNewStationsToDatabase(
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

                                }
                                else -> {

                                }
                            }
                        })

                        Log.i(TAG, "${it.data}")


                    }
                    if (stationSydny.isNotEmpty()){

                        val waypoints = mutableListOf<LatLng>()
                        // Draw Polyline for the route
                        for (station in stationSydny) {
                            waypoints.add(station.stationSydnyvalue)
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

                        //check if routes all ready exists in database
                        homeViewModel!!.gettingRoutesFromDatabase()
                        homeViewModel!!.getRoutes.observe(viewLifecycleOwner, Observer {
                            when (it) {
                                is Resource.Loading -> {
                                    Log.i(TAG, "getting routes directions from database ")
                                }
                                is Resource.Success -> {
                                    Log.e(TAG, "${it.data}")
                                    if (it.data.isEmpty()) {
                                        var c: Int? = 1
                                        getAllStationsRouteParrllel()
                                        homeViewModel!!.dirction.observe(viewLifecycleOwner, Observer {
                                            when (it) {
                                                is Resource.Loading -> {
                                                    Log.i(TAG, "getting directions...")
                                                }
                                                is Resource.Success -> {
                                                    Log.i(TAG, "Success ${it.data}")
                                                    if (it.data != null) {
                                                        // Decode the polyline string to LatLng points
                                                        val points = PolyUtil.decode(it.data.polyline)
                                                        val routeInfo = it.data

                                                        //inserting routes in database
                                                        homeViewModel!!.insertingRoutesInDatabase(
                                                            RouteDirctionEntity(
                                                                polyline = routeInfo.polyline,
                                                                distance = routeInfo.distance,
                                                                duration = routeInfo.duration
                                                            )
                                                        )

                                                        homeViewModel!!.insertRoutes.observe(
                                                            viewLifecycleOwner,
                                                            Observer {
                                                                when (it) {
                                                                    is Resource.Loading -> {
                                                                        Log.i(
                                                                            TAG,
                                                                            "inserting routes into database "
                                                                        )

                                                                    }
                                                                    is Resource.Success -> {
                                                                        Log.i(TAG, "${it.data}")
                                                                    }
                                                                    is Resource.Failure -> {
                                                                        Log.e(TAG, "${it.error}")
                                                                    }
                                                                    else -> {

                                                                    }
                                                                }
                                                            })

                                                        // Draw the polyline on the map
                                                        val polylineOptions = PolylineOptions()
                                                            .color(Color.BLUE)
                                                            .width(5f)
                                                            .addAll(points)
                                                        Log.i(TAG, "draw poly.......")
                                                        googleMap.addPolyline(polylineOptions)
                                                    }


                                                }
                                                is Resource.Failure -> {
                                                    Log.e(TAG, "Faieled ${it.error}")

                                                }
                                                else -> {}
                                            }
                                        }
                                        )
                                    } else {
                                        Log.e(TAG, "${it.data}")
                                        Log.i(TAG, "Success ${it.data}")
                                        if (it.data.isNotEmpty()) {
                                            // Decode the polyline string to LatLng points
                                            for (route in it.data) {


                                                val points = PolyUtil.decode(route.polyline)

                                                // Draw the polyline on the map
                                                val polylineOptions = PolylineOptions()
                                                    .color(Color.BLUE)
                                                    .width(5f)
                                                    .addAll(points)
                                                Log.i(TAG, "draw poly.......")
                                                googleMap.addPolyline(polylineOptions)
                                            }
                                        }
                                    }


                                    // Mark Stations with Markers
                                    val builder = LatLngBounds.builder()
                                    for (station in stationSydny) {
                                        Log.i(TAG, "station ---> ${station.stationName}")
                                        builder.include(station.stationSydnyvalue)
                                        val markerOptions = MarkerOptions()
                                            .position(station.stationSydnyvalue)
                                            .title(station.stationName)
                                            .icon(bitmapDescriptorFromVector(R.drawable.station_in_map))

                                        val marker = googleMap.addMarker(markerOptions)
                                        marker!!.showInfoWindow() // Show info window without requiring a click
                                    }

                                    // Move camera to include all the markers with zoom
                                    val bounds = builder.build()
                                    val padding = 100 // Adjust as needed
                                    val cameraUpdate =
                                        CameraUpdateFactory.newLatLngBounds(bounds, padding)

                                    // Calculate the zoom level based on the bounding box
                                    val width = resources.displayMetrics.widthPixels
                                    val height = resources.displayMetrics.heightPixels
                                    val zoomLevel = calculateZoomLevel(bounds, width, height)

                                    // Zoom the camera to the desired zoom level
                                    val zoomUpdate = CameraUpdateFactory.zoomTo(zoomLevel)
                                    // Apply both camera updates
                                    if(!isCameraAnimated){
                                        mMap.animateCamera(cameraUpdate)
                                    }
                                    else{
                                        mMap.moveCamera(cameraUpdate)
                                    }
                                    var trainMarker: Marker? = null
                                    val markerOptions = MarkerOptions()
                                        .position(
                                            LatLng(
                                                stationSydny[0].stationSydnyvalue.latitude,
                                                stationSydny[0].stationSydnyvalue.longitude
                                            )
                                        )
                                        .title("Train Location")
                                    trainMarker = googleMap.addMarker(markerOptions)
                                    trainMarker!!.showInfoWindow() // Show info window without requiring a click
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        homeViewModel!!.currantTrainLocation.collect {
                                            Log.i(
                                                TAG,
                                                "Location from  locationStateFlow collecting ${it}"
                                            )
                                            if (it!!.latitude != 0.0) {
                                                lifecycleScope.launch(Dispatchers.Main) {
                                                    trainMarker!!.setPosition(
                                                        LatLng(
                                                            it!!.longitude,
                                                            it!!.latitude
                                                        )
                                                    )
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
                        })
                    }else{
                        showCustomToast(requireContext(),"can't load stations")
                    }


                }
                else -> {

                }
            }
        })
        homeViewModel!!.getAllStation()
        homeViewModel!!.stations.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                }
                is Resource.Loading -> {
                    Log.i(TAG, "getting stations....")
                }
                is Resource.Success -> {


                }
                else -> {

                }
            }
        })
    }


    private fun calculateZoomLevel(bounds: LatLngBounds, width: Int, height: Int): Float {
        val padding = 100 // Adjust as needed

        val boundsWidth = bounds.northeast.longitude - bounds.southwest.longitude
        val boundsHeight = bounds.northeast.latitude - bounds.southwest.latitude

        val latRatio = (boundsHeight + padding) / height.toDouble()
        val lngRatio = (boundsWidth + padding) / width.toDouble()

        val maxRatio = maxOf(latRatio, lngRatio)

        return (16 - log2(maxRatio)).toFloat()
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
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            toast("done")
            // Handle the back button behavior for your specific fragment
            // For example, you can show a toast message or ignore the back button press
            requireActivity().finish() // Finish the current activity and exit the app
            // You can also perform a specific action or navigate to a different fragment/activity if needed
        }
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

    @Subscribe
    fun onMyEvent(trainLocation: Location_Response) {
        // Handle the event here
//        _trainLocationFromService.value = trainLocation
        _locationStateFlow.value = trainLocation
    }

    fun getAllStationsRouteParrllel() {
        // Create a CoroutineScope
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        Log.i(TAG,"$origin1 ,$destination1")
        Log.i(TAG,"$origin2 ,$destination2")
        Log.i(TAG,"$origin3 ,$destination3")
        Log.i(TAG,"$origin4 ,$destination4")
        Log.i(TAG,"$origin5 ,$destination5")
        Log.i(TAG,"$origin6 ,$destination6")
        Log.i(TAG,"$origin7 ,$destination7")
        Log.i(TAG,"$origin8 ,$destination8")
        Log.i(TAG,"$origin9 ,$destination9")
        val origin1Distenation1 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin1!!, destination1!!) }
        val origin2Distenation2 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin2!!, destination2!!) }
        val origin3Distenation3 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin3!!, destination3!!) }
        val origin4Distenation4 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin4!!, destination4!!) }
        val origin5Distenation5 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin5!!, destination5!!) }
        val origin6Distenation6 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin6!!, destination6!!) }
        val origin7Distenation7 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin7!!, destination7!!) }
        val origin8Distenation8 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin8!!, destination8!!) }
        val origin9Distenation9 =
            coroutineScope.async { homeViewModel!!.getLocationDirctions(origin9!!, destination9!!) }
        val origin10Distenation10 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin10!!,
                destination10!!
            )
        }
        val origin11Distenation11 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin11!!,
                destination11!!
            )
        }
        val origin12Distenation12 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin12!!,
                destination12!!
            )
        }
        val origin13Distenation13 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin13!!,
                destination13!!
            )
        }
        val origin14Distenation14 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin14!!,
                destination14!!
            )
        }
        val origin15Distenation15 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin15!!,
                destination15!!
            )
        }
        val origin16Distenation16 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin16!!,
                destination16!!
            )
        }
        val origin17Distenation17 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin17!!,
                destination17!!
            )
        }
        val origin18Distenation18 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
                origin18!!,
                destination18!!
            )
        }
        val origin19Distenation19 = coroutineScope.async {
            homeViewModel!!.getLocationDirctions(
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

    override fun onBackPressed() {
        requireActivity().finish()
    }

}