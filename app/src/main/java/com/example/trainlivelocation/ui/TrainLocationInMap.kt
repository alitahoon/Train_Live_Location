package com.example.trainlivelocation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentTrainLocationInMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainLocationInMap : Fragment(), OnMapReadyCallback {
    private val trainLocationInMapViewModel: TrainLocationInMapViewModel? by activityViewModels()
    private var binding: FragmentTrainLocationInMapBinding? = null
    private var mMap: GoogleMap? = null
    lateinit var mapView: MapView
    lateinit var latlong: LatLng
    private val args by navArgs<TrainLocationInMapArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrainLocationInMapBinding.inflate(inflater, container, false)
            .apply {
                binding?.viewmodel = trainLocationInMapViewModel
            }
        mapView = binding!!.trainLocationMapMapView
        mapView.onCreate(trainLocationInMapViewModel?.getMAP_VIEW_KEY())
        mapView.getMapAsync(this)
        setObserver()
        return binding?.root
    }

    private fun setObserver() {
        trainLocationInMapViewModel!!.distanceLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.trainLocationTxtTrainDistanceValue.setText("${it} meter")
        })
        trainLocationInMapViewModel!!.addressLiveData.observe(viewLifecycleOwner, Observer {
            binding!!.trainLocationTxtTrainAddressValue.setText("${it}")
        })
        trainLocationInMapViewModel!!.getAddress(args.trainLocation)
        trainLocationInMapViewModel!!.getDistanceInMeter(
            args.trainLocation.longitude.toDouble(),
            args.trainLocation.latitude.toDouble(),
            args.userLocation.latitude.toDouble(),
            args.userLocation.longitude.toDouble()
        )
    }


    companion object {

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mapView.onResume()
        mMap = googleMap
        mMap?.isMyLocationEnabled()

        mMap!!.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        var sydny: LatLng =
            LatLng(args.trainLocation.longitude.toDouble(), args.trainLocation.latitude.toDouble())
        mMap!!.addMarker(MarkerOptions().position(sydny).title("Train Location"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydny))
        val cameraPosition: CameraPosition = CameraPosition.Builder()
            .target(
                sydny
            ) // Sets the center of the map to location user
            .zoom(14F) // Sets the zoom
            .bearing(0F) // Sets the orientation of the camera to east
//            .tilt(40F) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder

        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        val polyline1 = mMap?.addPolyline(
            PolylineOptions().clickable(true)
                .add(
                    LatLng(args.trainLocation.latitude.toDouble(), args.trainLocation.longitude.toDouble()),
                    LatLng(args.userLocation.longitude.toDouble(), args.userLocation.latitude.toDouble())
                )
                .geodesic(true) // this will turn this line to curve
        )
        stylePolyline(polyline1)

    }

    fun getLocations(): Float? {
        return arguments?.getFloat("TrainLocation")!!
    }

    private fun stylePolyline(polyline: Polyline?) {
        polyline?.let {
            it.color = ContextCompat.getColor(requireContext(), R.color.basicColor)
            it.pattern = mutableListOf(Dot(), Gap(5F))
            it.startCap = RoundCap()
            it.jointType = JointType.ROUND
        }
    }
}