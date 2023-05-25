package com.example.trainlivelocation.ui

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
import com.example.trainlivelocation.utli.StationAlarmAdapterCustomAdapter
import com.example.trainlivelocation.utli.StationAlarmListener
import com.example.trainlivelocation.utli.toast


class Alarms : Fragment() ,StationAlarmListener{
    private val TAG: String? = "Alarms"
    private lateinit var binding: FragmentAlarmsBinding
    private val alarms_viewModel: Alarms_viewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAlarmsBinding.inflate(inflater,container,false)
            .apply {
                this.viewmodel=alarms_viewModel
            }
        alarms_viewModel.getAlarmsFromDatabase()
        binding.adapter=setAdapterItems()
        return binding.root
    }

    private fun setAdapterItems(): StationAlarmAdapterCustomAdapter {
        val adapter = StationAlarmAdapterCustomAdapter(this)
        alarms_viewModel.alarms!!.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    toast("getting alarms....")
                }
                is Resource.Success -> {
                    if (it.data.isEmpty()) {
                        Log.i(TAG, "${it.data}")
                        adapter.setData(it.data)

                    } else {
                        Log.i(TAG, "${it.data}")
                        adapter.setData(it.data)
                    }
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
        TODO("Not yet implemented")
    }

    override fun OnSwitchButtonChecked(isChecked: Boolean, stationAlarmEntity: StationAlarmEntity) {
        TODO("Not yet implemented")
    }
}