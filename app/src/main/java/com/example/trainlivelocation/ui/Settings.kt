package com.example.trainlivelocation.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentSettingsBinding
import com.example.trainlivelocation.utli.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Settings : Fragment() {

    private val TAG: String? = "Settings"
    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding= FragmentSettingsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=settingsViewModel
                this.lifecycleOwner=this@Settings
            }

        setObservers()

        return binding.root
    }

    private fun setObservers() {
        var stationHistoryService= Intent(requireActivity(),
            StationHistoryService::class.java)
        var trackTrainService= Intent(requireActivity(),
            TrackTrainService::class.java)


        settingsViewModel.startServices.observe(viewLifecycleOwner, Observer {
            if (it){
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                    ContextCompat.startForegroundService(requireContext(),stationHistoryService!!)
                }else{
                    requireContext().startService(stationHistoryService!!)
                }
            }
        })

        settingsViewModel.switchStationHistoryState.observe(viewLifecycleOwner, Observer {
            if (it){
                settingsViewModel.getTrainLocation(getUserCurrantTrainIntoSharedPrefrences(requireContext()))
                showCustomToast(requireContext(),"Opening service")

            }else{
                showCustomToast(requireContext(),"Closing service")
                requireContext().stopService(stationHistoryService)
            }
        })

        settingsViewModel.switchtraintrackState.observe(viewLifecycleOwner, Observer {
            if (it){
                showCustomToast(requireContext(),"Opening service")
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                    ContextCompat.startForegroundService(requireContext(),trackTrainService!!)
                }else{
                    requireContext().startService(stationHistoryService!!)
                }
            }else{
                showCustomToast(requireContext(),"Closing service")
                requireContext().stopService(stationHistoryService)
            }
        })

    }



    companion object {

    }
}