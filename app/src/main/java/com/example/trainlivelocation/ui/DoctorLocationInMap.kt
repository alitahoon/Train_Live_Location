package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentDoctorLocationInMapBinding
import com.example.trainlivelocation.databinding.FragmentTrainLocationInMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorLocationInMap : Fragment() ,OnMapReadyCallback{

    private val args: DoctorLocationInMapArgs by navArgs()
    private val doctorLocationInMapViewModel: DoctorLocationInMapViewModel? by activityViewModels()
    private var binding: FragmentDoctorLocationInMapBinding? = null
    private var mMap: GoogleMap? = null
    lateinit var mapView: MapView
    lateinit var latlong: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentDoctorLocationInMapBinding.inflate(inflater,container,false)
           .apply {
//               this.viewmodel=doctorLocationInMapViewModel
           }
        val viewModelff = ViewModelProvider(this).get(DoctorLocationInMapViewModel::class.java)
        binding?.viewmodel = viewModelff
        mapView=binding!!.doctorLocationMapMapView
        binding!!.doctorLocationMapMapView.onCreate(doctorLocationInMapViewModel?.getMAP_VIEW_KEY())
        binding!!.doctorLocationMapMapView.getMapAsync(this)
        doctorLocationInMapViewModel!!.getCurrantLocation(args.patientLocation)
        setObservers()

        return binding!!.root
    }

    private fun setObservers() {

    }

    companion object {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapView.onResume()
        mMap = googleMap
        mMap?.isMyLocationEnabled()
        mMap?.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        doctorLocationInMapViewModel!!.userCurrantLocation.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    binding!!.doctorLocationProgressBar.visibility=View.VISIBLE
                }
                is Resource.Success->{
                    binding!!.doctorLocationProgressBar.visibility=View.GONE
                    if (it != null){
                        var sydnyPatient: LatLng =
                            LatLng(args.patientLocation.latitude,args.patientLocation.longitude)
                        var sydnyDoctor: LatLng =
                            LatLng(it.data.latitude,it.data.longitude)
                        mMap!!.addMarker(MarkerOptions().position(sydnyDoctor).title("Your Location"))
                        mMap!!.addMarker(MarkerOptions().position(sydnyPatient).title("Patient Location"))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydnyDoctor))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydnyPatient))
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
                            .include(sydnyDoctor)
                            .include(sydnyPatient)
                            .build()

                        // Draw a line between the locations
                        val polylineOptions = PolylineOptions()
                            .add(sydnyDoctor, sydnyPatient)
                            .color(R.color.textAlarmColor)
                            .width(10f)

                        // Animate camera to the calculated bounds
                        // Calculate the padding based on the map size and desired zoom level
                        val mapWidth = mapView.width
                        val mapHeight = mapView.height
                        val padding = (Math.max(mapWidth, mapHeight) * 0.1).toInt() // Optional padding around the bounds (in pixels)
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                        mMap?.animateCamera(cameraUpdate)
                        mMap?.addPolyline(polylineOptions)

//                        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
                is Resource.Failure->{
                    binding!!.doctorLocationProgressBar.visibility=View.GONE
                }
                else -> {
                    binding!!.doctorLocationProgressBar.visibility=View.GONE

                }
            }
        })


    }
}