package com.example.trainlivelocation.ui

import Resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.StationAlarmEntity
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentAlarmsBinding
import com.example.trainlivelocation.utli.Add_station_Alarm_listener
import com.example.trainlivelocation.utli.StationAlarmAdapterCustomAdapter
import com.example.trainlivelocation.utli.StationAlarmListener
import com.example.trainlivelocation.utli.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Alarms : Fragment() ,StationAlarmListener,Add_station_Alarm_listener{
    private val TAG: String? = "Alarms"
    private lateinit var binding: FragmentAlarmsBinding
    private val alarms_viewModel: Alarms_viewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarms_viewModel.getAlarmsFromDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAlarmsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=alarms_viewModel
            }
        setAdapterItems()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        alarms_viewModel.btnAddAlarmClicked.observe(viewLifecycleOwner, Observer {
            if (it==true){
                var dialog = Add_station_alarm(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "Add_station_alarm")
            }
        })
    }

    private fun setAdapterItems(): StationAlarmAdapterCustomAdapter {
        val adapter = StationAlarmAdapterCustomAdapter(this)
        alarms_viewModel.alarms!!.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    toast("getting alarms....")
                    Log.i(TAG,"getting alarms....")
                }
                is Resource.Success -> {
                    Log.i(TAG, "${it.data}")
                    adapter.setData(it.data)
                    binding.alarmsRcv.adapter=adapter
                    adapter.notifyDataSetChanged()
                }
                is Resource.Failure -> {

                    Log.e(TAG, "${it.error}")
                }
                else -> {

                }
            }
        })
        return adapter
    }


    companion object {

    }

    override fun OnButtonEdtitClicked(stationAlarmEntity: StationAlarmEntity) {
        var dialog = Add_station_alarm(this)
        var childFragmentManager = getChildFragmentManager()
        dialog.show(childFragmentManager, "Add_station_alarm")
    }

    override fun OnSwitchButtonChecked(isChecked: Boolean, stationAlarmEntity: StationAlarmEntity) {

    }

    override fun OnDeleteAlarmButtonClicked(stationAlarmEntity: StationAlarmEntity) {
        alarms_viewModel.deleteAlarmFromDatabase(stationAlarmEntity.id)
        alarms_viewModel.deleteAlarm!!.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    Log.i(TAG,"Deleting Alarm...")
                }
                is Resource.Success->{
                    Log.i(TAG, "${it.data}")
                    alarms_viewModel.getAlarmsFromDatabase()
                }
                is Resource.Failure->{
                    Log.i(TAG, "${it.error}")
                }

                else -> {}
            }
        })
    }

    override fun onDismiss() {
        alarms_viewModel.getAlarmsFromDatabase()
        setAdapterItems()
    }
}