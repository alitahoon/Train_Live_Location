package com.example.trainlivelocation.ui

import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TrackLocationFeature.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class TrackLocationFeature : Fragment(), TrackLocationListener {
    private val _userLocationMuta: MutableLiveData<LocationDetails?> = MutableLiveData(null)
    private val trackLocationFeatureViewModel: TrackLocationFeatureViewModel? by activityViewModels()
    private var binding: FragmentTrackLocationFeatureBinding? = null

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
        trackLocationFeatureViewModel?.setbaseActivity(requireActivity())
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
        trackLocationFeatureViewModel?.btnTrackLocationFeature?.observe(
            viewLifecycleOwner,
            Observer {

                if (it == true) {
                    binding?.trackLocationProgressBar?.setVisibility(View.VISIBLE)
                    var trainLocation: LocationDetails? = null
                    var userLocation: LocationDetails? = null
                    var distance: Double? = null

                    //get train location
//                trackLocationFeatureViewModel?.startTrackLocationForgroundService()
//                get location from API
                    Log.e(TAG, "get train location")
                    trackLocationFeatureViewModel?.getTrainLocationFromApi()
                    trackLocationFeatureViewModel?._trainLocationMuta?.observe(viewLifecycleOwner,
                        Observer {
                            it?.let {
                                Log.e(
                                    TAG,
                                    it?.longitude.toString() + "  " + it?.latitude.toString()
                                )
                                trainLocation =
                                    LocationDetails(
                                        it!!.longitude.longitude,
                                        it!!.latitude.latitude
                                    )
                            }

                        })


                    //get user location
                    Log.e(TAG, "get user location")
                    trackLocationFeatureViewModel?.getUserCurrantLocation()
                    _userLocationMuta?.observe(
                        viewLifecycleOwner,
                        Observer {
                            it?.let {
                                Log.e(
                                    TAG,
                                    it?.longitude.toString() + "  " + it?.latitude.toString()
                                )
                                userLocation = it
                            }

                        })

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (trainLocation != null && userLocation != null) {
                            //compute distance between user and train
                            Log.e(TAG, "compute distance between user and train")
                            distance = distanceInMeter(
                                trainLocation?.longitude!!.toDouble(),
                                trainLocation?.latitude!!.toDouble(),
                                userLocation?.latitude!!.toDouble(),
                                userLocation?.longitude!!.toDouble()
                            )
//                            distance=getDistanceUsingGoogleMapApi(
//                                LatLng(
//                                    trainLocation?.latitude!!.toDouble(),
//                                    trainLocation?.longitude!!.toDouble()
//                                ),
//                                LatLng(
//                                    userLocation?.latitude!!.toDouble(),
//                                    userLocation?.longitude!!.toDouble()
//                                )
//                            )


                            if (distance != null) {
                                Log.e(TAG, "Distance ${distance}")
                                val bundle = Bundle()
                                bundle.putFloat("distance", distance!!.toFloat())
                                binding?.trackLocationProgressBar?.setVisibility(View.INVISIBLE)
//                                findNavController().navigate(
//                                    R.id.action_trackLocationFeature_to_home2,
//                                    bundle
//                                )
                                val action: NavDirections =
                                    TrackLocationFeatureDirections.actionTrackLocationFeatureToTrainLocationInMap(
                                        userLocation!!,
                                        trainLocation!!
                                    )
                                findNavController().navigate(action)
                            }
                        } else {
                            Log.e(TAG, "Error")
                        }
                    }, 10000)


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

}