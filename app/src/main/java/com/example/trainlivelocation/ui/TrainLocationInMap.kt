package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.Train
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentTrackLocationFeatureBinding
import com.example.trainlivelocation.databinding.FragmentTrainLocationInMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class TrainLocationInMap : Fragment() ,OnMapReadyCallback{
    private val trainLocationInMapViewModel:TrainLocationInMapViewModel? by activityViewModels()
    private var binding: FragmentTrainLocationInMapBinding? = null
    private var mMap:GoogleMap?=null
    lateinit var mapView:MapView
    lateinit var latlong:LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentTrainLocationInMapBinding.inflate(inflater,container,false)
           .apply {
               binding?.viewmodel=trainLocationInMapViewModel
           }
        mapView=binding!!.trainLocationMapMapView
        mapView.onCreate(trainLocationInMapViewModel?.getMAP_VIEW_KEY())
        mapView.getMapAsync(this)
        return binding?.root
    }


    companion object {

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mapView.onResume()
        mMap=googleMap
        mMap?.isMyLocationEnabled()
    }
}