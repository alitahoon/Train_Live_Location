package com.example.trainlivelocation.ui

import Resource
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.StationAlarmEntity
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddStationAlarmBinding
import com.example.trainlivelocation.databinding.FragmentAlarmsBinding
import com.example.trainlivelocation.utli.Station_Dialog_Listener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Add_station_alarm : BottomSheetDialogFragment(), Station_Dialog_Listener {

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
        add_station_alarmViewmodel.btnChooseStationClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var dialog = ChooseStationDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseStationDialogFragment")
            }
        })

        add_station_alarmViewmodel.btnAddStationAlarmClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                add_station_alarmViewmodel.insertNewStationAlarmIntoDatabase(
                    StationAlarmEntity(
                        apiId = stationId!!,
                        name = stationName!!,
                        distance = 0,
                        longitude = Longitude!!,
                        latitude = Latitude!!
                    )
                )
                add_station_alarmViewmodel.insertStationAlarm!!.observe(viewLifecycleOwner, Observer {
                    when(it){
                        is Resource.Loading->{

                        }
                        is Resource.Success->{

                        }
                        is Resource.Failure->{

                        }
                        else -> {

                        }
                    }
                })
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
        stationId = StationId
        stationName = StationName
        Longitude = longitude
        Latitude = latitude
    }
}