package com.example.trainlivelocation.utli


import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.*
import com.example.trainlivelocation.databinding.AlarmsItemLayoutBinding
import com.example.trainlivelocation.databinding.PassengersRcvItemLayoutBinding

class StationAlarmAdapterViewHolder(
    private val binding: AlarmsItemLayoutBinding,
    private val stationAlarmListener: StationAlarmListener
    ) : RecyclerView.ViewHolder(binding.root) {
    private val TAG:String?="PassengersAdapterViewHolder"
    fun bind(alarmEntity: StationAlarmEntity) {
        binding.alarm = alarmEntity
        binding.listener = stationAlarmListener
    }


}