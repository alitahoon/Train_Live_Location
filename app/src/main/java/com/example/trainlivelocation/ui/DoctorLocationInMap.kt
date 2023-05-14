package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentDoctorLocationInMapBinding
import com.example.trainlivelocation.databinding.FragmentTrainLocationInMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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
               this.viewmodel=doctorLocationInMapViewModel
           }
        mapView=binding!!.doctorLocationMapMapView
        binding!!.doctorLocationMapMapView.onCreate(doctorLocationInMapViewModel?.getMAP_VIEW_KEY())
        binding!!.doctorLocationMapMapView.getMapAsync(this)
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

        mMap!!.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        var sydny: LatLng =
            LatLng(args.patientLocation.latitude,args.patientLocation.longitude)
        mMap!!.addMarker(MarkerOptions().position(sydny).title("Patient Location"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydny))
        val cameraPosition: CameraPosition = CameraPosition.Builder()
            .target(
                sydny
            ) // Sets the center of the map to location user
            .zoom(16F) // Sets the zoom
            .bearing(0F) // Sets the orientation of the camera to east
//            .tilt(40F) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder

        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}