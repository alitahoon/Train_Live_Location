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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.domain.entity.LocationDetails
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentTrackLocationFeatureBinding
import com.example.trainlivelocation.utli.TrackLocationListener
import dagger.hilt.android.AndroidEntryPoint
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


    companion object {

    }

    override fun onDestroy() {
        super.onDestroy()
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
                    var distance:Double?=null

                    //get train location
//                trackLocationFeatureViewModel?.startTrackLocationForgroundService()
//                get location from API
                    Log.e(TAG, "get train location")
                    trackLocationFeatureViewModel?.getTrainLocationFromApi()
                    trackLocationFeatureViewModel?._trainLocationMuta?.observe(viewLifecycleOwner,
                        Observer {
                            it?.let {
                                Log.e(TAG, it?.longitude.toString() + "  " + it?.latitude.toString())
                                trainLocation =
                                    LocationDetails(it!!.longitude.longitude, it!!.latitude.latitude)
                            }

                        })


                    //get user location
                    Log.e(TAG, "get user location")
                    trackLocationFeatureViewModel?.getUserCurrantLocation()
                    trackLocationFeatureViewModel?.userLocation?.observe(
                        viewLifecycleOwner,
                        Observer {
                            it?.let {
                                Log.e(TAG, it?.longitude.toString() + "  " + it?.latitude.toString())
                                userLocation = it
                            }

                        })

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (trainLocation!= null && userLocation!=null){
                            //compute distance between user and train
                            Log.e(TAG, "compute distance between user and train")
                            distance=getKilometers(
                                trainLocation?.latitude!!.toDouble(),
                                trainLocation?.longitude!!.toDouble(),
                                userLocation?.latitude!!.toDouble(),
                                userLocation?.longitude!!.toDouble()
                            )


                            if (distance!=null){
                                val bundle = Bundle()
                                bundle.putFloat("distance", distance!!.toFloat())
                                binding?.trackLocationProgressBar?.setVisibility(View.INVISIBLE)
                                findNavController().navigate(R.id.action_trackLocationFeature_to_home2,bundle)
                            }
                        }else{
                            Log.e(TAG,"Error")
                        }
                    },4000)



                }

            })
    }

    fun getDistanceBetweenUserAndTrain(lat1: Double, lon1: Double, lat2: Double, lon2: Double):Double {
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

    private fun  computeDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double? {
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


}