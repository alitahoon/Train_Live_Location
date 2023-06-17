package com.example.trainlivelocation.utli

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.*
import com.example.trainlivelocation.databinding.AlarmsItemLayoutBinding
import com.example.trainlivelocation.databinding.PassengersRcvItemLayoutBinding
import com.example.trainlivelocation.databinding.StationRcvItemLayoutBinding

class StationAlarmAdapterCustomAdapter(
    private val stationAlarmListener: StationAlarmListener
) :
    RecyclerView.Adapter<StationAlarmAdapterViewHolder>(), BindableAdapter<ArrayList<StationAlarmEntity>> {
    private val TAG: String? = "StationAlarmAdapterCustomAdapter"
    private var binding: AlarmsItemLayoutBinding? = null
    lateinit var stationArrayList: ArrayList<StationAlarmEntity>
    var stationList = emptyList<StationAlarmEntity>()

    public fun updateData(postList: ArrayList<StationAlarmEntity>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationAlarmAdapterViewHolder {
        binding = AlarmsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StationAlarmAdapterViewHolder(binding!!, stationAlarmListener!!)
    }

    override fun getItemCount(): Int = stationArrayList.size

    override fun onBindViewHolder(holder: StationAlarmAdapterViewHolder, position: Int) {
        val stationAlarm = stationArrayList.get(position)
        // Bind data to the view holder

        // Apply animation
        holder.bind(stationAlarm)

    }

    override fun setData(data: ArrayList<StationAlarmEntity>) {
        Log.i(TAG, "data from adapter ---> ${data}")
        this.stationArrayList = data!!
        notifyDataSetChanged()
    }
}
