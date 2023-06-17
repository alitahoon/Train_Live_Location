package com.example.trainlivelocation.ui

import Resource
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.StationAlarmEntity
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddStationAlarmBinding
import com.example.trainlivelocation.databinding.FragmentAlarmsBinding
import com.example.trainlivelocation.utli.Add_station_Alarm_listener
import com.example.trainlivelocation.utli.Station_Dialog_Listener
import com.example.trainlivelocation.utli.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Add_station_alarm(private val addStationAlarmListener: Add_station_Alarm_listener) : BottomSheetDialogFragment(), Station_Dialog_Listener {

    private val TAG: String? = "Add_station_alarm"
    private lateinit var binding: FragmentAddStationAlarmBinding
    private val add_station_alarmViewmodel: Add_station_alarmViewmodel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddStationAlarmBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = add_station_alarmViewmodel
            }
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        add_station_alarmViewmodel.btnCloseClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                dismiss()
                addStationAlarmListener.onDismiss()
            }
        })


        add_station_alarmViewmodel.btnChooseStationClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var dialog = ChooseStationDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseStationDialogFragment")
            }
        })

        add_station_alarmViewmodel.btnAddStationAlarmClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                if (stationId == null || stationName == null || Longitude == null || Latitude == null) {
                    toast("Please Enter Alarm Data ")
                } else {
                    add_station_alarmViewmodel.insertNewStationAlarmIntoDatabase(
                        StationAlarmEntity(
                            apiId = stationId!!,
                            name = stationName!!,
                            distance = binding.addStationAlarmTxtDistance.displayedValues?.get(
                                binding.addStationAlarmTxtDistance.value
                            ).toString().toInt(),
                            longitude = Longitude!!,
                            latitude = Latitude!!
                        )
                    )
                    add_station_alarmViewmodel.insertStationAlarm!!.observe(
                        viewLifecycleOwner,
                        Observer {
                            when (it) {
                                is Resource.Loading -> {
                                    Log.i(TAG, "Inserting alarm...")
                                }
                                is Resource.Success -> {
                                    Log.i(TAG, "${it.data}")
                                    dismiss()
                                    addStationAlarmListener.onDismiss()
                                }
                                is Resource.Failure -> {
                                    Log.i(TAG, "${it.error}")
                                }
                                else -> {

                                }
                            }
                        })
                }

            }
        })
    }

    companion object {
        private var stationId: Int? = null
        private var stationName: String? = null
        private var Longitude: Double? = null
        private var Latitude: Double? = null
    }

    override fun onStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        binding.addAlarmTxtStation.setText(stationName)
        stationId = StationId
        stationName = StationName
        Longitude = longitude
        Latitude = latitude


    }
}