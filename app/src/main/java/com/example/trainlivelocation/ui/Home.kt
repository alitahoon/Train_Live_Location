package com.example.trainlivelocation.ui

import Resource
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.UserResponseItem
import com.example.domain.entity.StationSydny
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentHomeBinding
import com.example.trainlivelocation.utli.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : Fragment(), Train_Dialog_Listener, OnMapReadyCallback {
    private val TAG: String? = "Home";

    private val homeViewModel: HomeViewModel? by activityViewModels()
    private var binding: FragmentHomeBinding? = null
    private val permissionManager = PermissionManager.from(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            this.viewmodel = homeViewModel
        }
        setObservers()

        userModel = getuserModelFromSharedPreferences(requireContext())
        if (getDistance() != null) {
            binding?.homeTxtTrainDistance?.setText(getDistance().toString() + " Meal")
        }

        //check if track services is running
        val serviceClass = TrackTrainService::class.java
        val isRunning = isServiceRunning(serviceClass)
        if (isRunning) {
            // Service is running here we will show the map
            //start mapView
            binding!!.homeCardTrainId.visibility = View.GONE
            binding!!.homeCardTrainIcon.visibility = View.GONE
            binding!!.homeMapCardView.visibility = View.VISIBLE
            binding!!.homeMapView.onCreate(homeViewModel!!.getMAP_VIEW_KEY())
            binding!!.homeMapView.getMapAsync(this)


        } else {
            // Service is not running
        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager.request(Permission.Notification)
                .rationale("We need permission to show Notifications")
                .checkPermission { granted: Boolean ->
                    if (granted) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No Permission to show notifications",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


        return binding!!.root
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
                findNavController().navigate(action)
            }
        })


        homeViewModel?.postsBtn?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(HomeDirections.actionHomeToPosts(null))
            }
        })


        homeViewModel?.locationCardBtn?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(R.id.action_home2_to_trainLocationInMap)
            }
        })

        homeViewModel?.btnEmergancyClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = HomeDirections.actionHome2ToEmergency(userModel!!)
                findNavController().navigate(action)
            }
        })

        homeViewModel?.btnTicketClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = HomeDirections.actionHome2ToTickets(userModel!!)
                findNavController().navigate(action)
            }
        })

        homeViewModel?.passengersbtnClicked?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = HomeDirections.actionHome2ToPassengers(userModel!!)
                findNavController().navigate(action)
            }
        })



        homeViewModel?.chooseTrainTxtClicked!!.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var dialog = ChooseTrainDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTrainDialogFragment")
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
        binding!!.homeTrackTrainIDTxt.setText(trainId!!.toString())
        homeViewModel!!.getTrainLocationInbackground(trainId)
        var locationForegrondservice: Intent? =
            Intent(requireActivity(), TrackTrainService::class.java)
        locationForegrondservice!!.putExtra(
            "trainId", binding!!.homeTrackTrainIDTxt.text.toString().toInt()
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(locationForegrondservice)
            //check if track services is running
            val serviceClass = TrackTrainService::class.java
            val isRunning = isServiceRunning(serviceClass)
            if (isRunning) {
                // Service is running here we will show the map
                //start mapView
                binding!!.homeCardTrainId.visibility = View.GONE
                binding!!.homeCardTrainIcon.visibility = View.GONE
                binding!!.homeMapCardView.visibility = View.VISIBLE
                binding!!.homeMapView.onCreate(homeViewModel!!.getMAP_VIEW_KEY())
                binding!!.homeMapView.getMapAsync(this)


            } else {
                // Service is not running
            }
        } else {
            requireActivity().startService(locationForegrondservice)
        }
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
        //first we will get all stations from api
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
                    Log.i(TAG, "${it.data}")
                    val stationSydny = genenrateStationsSydny(ArrayList(it.data.distinct()))

                    var origin: LatLng? = null// Alexandria
                    var destination: LatLng? = null  // Ramsis(Cairo)
                    // Draw Polyline for the route
                    val polylineOptions = PolylineOptions().color(Color.RED).width(5f)
                    for (station in stationSydny) {
                        if (station.stationName.equals("Alexandria")) {
                            origin=station.stationSydnyvalue
                        }else if(station.stationName.equals("Ramsis(Cairo)")){
                            destination=station.stationSydnyvalue
                        }
                    }

                    //requst for dirctionfrom google maps api
                    homeViewModel!!.getLocationDirctions(origin!!, destination!!)
                    homeViewModel!!.dirction.observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is Resource.Loading -> {
                                Log.i(TAG,"getting dirctions")
                            }
                            is Resource.Success -> {
                                Log.i(TAG,"${it.data}")
                                if (it.data != null) {
                                    val route = it.data.routes[0]
                                    val legs = route.legs


                                    for (leg in legs) {
                                        for (step in leg.steps) {
                                            val points = PolyUtil.decode(step.polyline.encodedPath)
                                            for (point in points) {
                                                polylineOptions.add(LatLng(point.latitude, point.longitude))
                                            }
                                        }
                                    }

                                    googleMap.addPolyline(polylineOptions)
                                }
                            }
                            is Resource.Failure -> {
                                Log.e(TAG,"${it.error}")

                            }
                            else -> {}
                        }
                    }
                    )

                        // Mark Stations with Markers
                        for (station in stationSydny) {
                            val markerOptions = MarkerOptions()
                                .position(station.stationSydnyvalue)
                                .title(station.stationName)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                            val marker = googleMap.addMarker(markerOptions)
                            marker!!.showInfoWindow() // Show info window without requiring a click
                        }
                        // Move camera to include all the markers
                        val builder = LatLngBounds.builder()
                        for (station in stationSydny) {
                            builder.include(station.stationSydnyvalue)
                        }
                        val bounds = builder.build()
                        val padding = 100 // Adjust as needed
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                        val center = LatLng(
                            (bounds.southwest.latitude + bounds.northeast.latitude) / 2,
                            (bounds.southwest.longitude + bounds.northeast.longitude) / 2
                        )
                        // Rotate the camera
                        val rotation = 45f // Rotation angle in degrees
                        val rotatedCameraUpdate = CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(center)
                                .zoom(googleMap.cameraPosition.zoom)
                                .bearing(rotation)
                                .build()
                        )

                        googleMap.moveCamera(rotatedCameraUpdate)

                    }
                else -> {

                }
            }
        })
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
}