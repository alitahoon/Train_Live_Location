package com.example.trainlivelocation.ui

import Resource
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.StationItemEntity
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentDoctorLocationInMapBinding
import com.example.trainlivelocation.databinding.FragmentTrackTrainInOpenStreetMapBinding
import com.google.android.gms.maps.model.LatLng
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class TrackTrainInOpenStreetMap : Fragment(), MapView.OnFirstLayoutListener {
    private val trackTrainInOpenStreetMapViewModel: TrackTrainInOpenStreetMapViewModel? by activityViewModels()
    private var binding: FragmentTrackTrainInOpenStreetMapBinding? = null
    private val TAG = "TrackTrainInOpenStreetMap"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackTrainInOpenStreetMapBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = trackTrainInOpenStreetMapViewModel
            }

        // Initialize the osmdroid configuration
        Configuration.getInstance().load(
            requireContext().applicationContext,
            requireContext().getSharedPreferences("OpenStreetMap", MODE_PRIVATE)
        )

        binding!!.trackTrainOpenStreetMapView.setTileSource(TileSourceFactory.MAPNIK) // Set the tile source to Mapnik
        binding!!.trackTrainOpenStreetMapView.setBuiltInZoomControls(true)
        binding!!.trackTrainOpenStreetMapView.setMultiTouchControls(true)
        binding!!.trackTrainOpenStreetMapView.addOnFirstLayoutListener(this)
        // Enable tile caching
        binding!!.trackTrainOpenStreetMapView.setFlingEnabled(true) // Enable fling gesture for smoother scrolling

        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        binding!!.trackTrainOpenStreetMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding!!.trackTrainOpenStreetMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding!!.trackTrainOpenStreetMapView.onDetach()
        binding!!.trackTrainOpenStreetMapView.tileProvider.clearTileCache()
    }


    private fun addMarkersToMap(mapView: MapView, markerSydny: LatLng, markerName: String?) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(markerSydny.latitude, markerSydny.longitude) // Set the latitude and longitude of the marker
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM) // Adjust the anchor point of the marker if needed
        marker.title = markerName // Set the title of the marker
        mapView.overlays.add(marker)
        // Add more markers as needed
    }

    private fun zoomToMarkers(mapView: MapView) {
        val markers = mapView.overlays.filterIsInstance<Marker>()
        if (markers.isNotEmpty()) {
            val maxLat = markers.maxByOrNull { it.position.latitude }!!.position.latitude
            val minLat = markers.minByOrNull { it.position.latitude }!!.position.latitude
            val maxLon = markers.maxByOrNull { it.position.longitude }!!.position.longitude
            val minLon = markers.minByOrNull { it.position.longitude }!!.position.longitude

            val boundingBox = BoundingBox(maxLat, maxLon, minLat, minLon)
            val mapController: IMapController = mapView.controller
            mapController.setCenter(boundingBox.centerWithDateLine)
            mapController.zoomToSpan(boundingBox.latitudeSpan, boundingBox.longitudeSpan)
        }
    }

    companion object {

    }

    override fun onFirstLayout(v: View?, left: Int, top: Int, right: Int, bottom: Int) {
        //get stations from database
        trackTrainInOpenStreetMapViewModel!!.gettingStationsFromDatabase()
        trackTrainInOpenStreetMapViewModel!!.getStationsFromDatabase.observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Success -> {
                        Log.i(TAG, "${it.data}")
                        if (it.data.isEmpty()) {
                            trackTrainInOpenStreetMapViewModel!!.getAllStation()
                            trackTrainInOpenStreetMapViewModel!!.stations.observe(viewLifecycleOwner,
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
                                            //inserting stations to database
                                            for (station in it.data) {
                                                addMarkersToMap(
                                                    binding!!.trackTrainOpenStreetMapView,
                                                    LatLng(station.latitude, station.longitude),
                                                    station.name
                                                )
                                                trackTrainInOpenStreetMapViewModel!!.insertingNewStationsToDatabase(
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
                                            zoomToMarkers(binding!!.trackTrainOpenStreetMapView)
                                        }
                                        else -> {}
                                    }
                                })
                        } else {
                            for (station in it.data) {
                                addMarkersToMap(
                                    binding!!.trackTrainOpenStreetMapView,
                                    LatLng(station.latitude, station.longitude),
                                    station.name
                                )
                            }
                            binding!!.trackTrainOpenStreetMapView.invalidate()
                            zoomToMarkers(binding!!.trackTrainOpenStreetMapView)
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
}