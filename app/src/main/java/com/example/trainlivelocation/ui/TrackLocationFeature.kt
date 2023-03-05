package com.example.trainlivelocation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.trainlivelocation.databinding.FragmentTrackLocationFeatureBinding
import dagger.hilt.android.AndroidEntryPoint

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
class TrackLocationFeature : Fragment() ,TrackLocationListener{
    // TODO: Rename and change types of parameters

    private val trackLocationFeatureViewModel:TrackLocationFeatureViewModel? by activityViewModels()
    private var binding: FragmentTrackLocationFeatureBinding? = null
    private var param1: String? = null
    private var param2: String? = null
    private val TAG:String?="TrackLocationFeature"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTrackLocationFeatureBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=trackLocationFeatureViewModel
            }
        binding?.viewmodel?.trackLocationListener=this
        trackLocationFeatureViewModel?.setbaseActivity(requireActivity())
        setObservers()

        return binding?.root
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TrackLocationFeature.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TrackLocationFeature().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        trackLocationFeatureViewModel?.stopTrackLocationForgroundService()
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
    fun setObservers(){
        trackLocationFeatureViewModel?.btnTrackLocationFeature?.observe(viewLifecycleOwner, Observer {
            if (it==true){
                trackLocationFeatureViewModel?.startTrackLocationForgroundService()
                //get location from API
//                Log.e(TAG,"setObservers")
//                trackLocationFeatureViewModel?.getTrainLocationFromApi()
//                trackLocationFeatureViewModel?._trainLocationMuta?.observe(viewLifecycleOwner,
//                    Observer {
//                        Log.e(TAG,"observe")
//                        Log.e(TAG,it?.longitude.toString()+"  "+it?.latitude.toString())
//                    })
            }
        })
    }
}