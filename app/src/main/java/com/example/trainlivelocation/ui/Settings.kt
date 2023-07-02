package com.example.trainlivelocation.ui

import android.app.ActivityManager
import android.content.Context
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
    private var stationHistoryService: Intent?= null
    private var trackTrainService: Intent?= null
    private var locationTrackBackgroundService: Intent?= null
    private var stationAlarmService: Intent?= null



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

        stationHistoryService= Intent(requireActivity(),
            StationHistoryService::class.java)
        trackTrainService= Intent(requireActivity(),
            TrackTrainService::class.java)
        locationTrackBackgroundService= Intent(requireActivity(),
            LocationTrackBackgroundService::class.java)
        stationAlarmService= Intent(requireActivity(),
            StationAlarmService::class.java)

        setObservers()

        return binding.root
    }

    private fun setObservers() {


        if(isServiceRunning(StationHistoryService::class.java)){
            binding.settingsStationHistroyAlarmsSwitchBtn.isChecked = true
        }
        else{
            Log.i(TAG, "StationHistoryService")
        }

        if(isServiceRunning(TrackTrainService::class.java)){
            binding.settingsTracktrainSwitchBtn.isChecked = true
        }

        if(isServiceRunning(LocationTrackBackgroundService::class.java)){
            binding.settingsSharingLocationSwitchBtn.isChecked = true
        }

        if(isServiceRunning(StationAlarmService::class.java)){
            binding.settingsStationAlarmSwitchBtn.isChecked = true
        }

        settingsViewModel.startServices.observe(viewLifecycleOwner, Observer {
            if (it){
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                    ContextCompat.startForegroundService(requireContext(),stationHistoryService!!)
                }else{
                    requireContext().startService(stationHistoryService!!)
                }
            }
        })

        settingsViewModel.switchStationAlarmState.observe(viewLifecycleOwner, Observer{
            if(it){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    ContextCompat.startForegroundService(requireContext(), stationAlarmService!!)
                }else{
                    requireContext().startService(stationAlarmService)
                }
            }
            else{
                requireContext().stopService(stationAlarmService)
            }
        })

        settingsViewModel.switchShareLocationState.observe(viewLifecycleOwner, Observer{
            if(it){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    ContextCompat.startForegroundService(requireContext(), locationTrackBackgroundService!!)
                }else{
                    requireContext().startService(locationTrackBackgroundService)
                }
            }
            else{
                requireContext().stopService(locationTrackBackgroundService)
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
                    requireContext().startService(trackTrainService!!)
                }
            }else{
                showCustomToast(requireContext(),"Closing service")
                requireContext().stopService(trackTrainService)
            }
        })

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







    companion object {

    }
}