package com.example.trainlivelocation.utli

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.LocationDetails
import com.example.domain.entity.StationResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.StationRcvItemLayoutBinding

class StationCustomAdapter(private val stationListener: Station_Dialog_Listener) :
    RecyclerView.Adapter<StationAdapterViewHolder>(), BindableAdapter<ArrayList<StationResponseItem>> {
    private var TAG:String?="StationCustomAdapter"
    var selectedItemPos = -1
    var lastItemSelectedPos = -1
    private var binding: StationRcvItemLayoutBinding? = null
    lateinit var stationArrayList: ArrayList<StationResponseItem>
    var stationList = emptyList<StationResponseItem>()

    public fun updateData(stationList: ArrayList<StationResponseItem>) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationAdapterViewHolder {
        binding = StationRcvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StationAdapterViewHolder(binding!!, stationListener)
    }

    override fun getItemCount(): Int = stationList.size

    override fun onBindViewHolder(holder: StationAdapterViewHolder, position: Int) {
        val station = stationList.get(position)
        Log.i(TAG,"${stationList.get(position)}")
        holder.bind(station)
        holder.itemView.setOnClickListener{
            stationListener.onStationSelected(station.id,station.name, LocationDetails(station.longitude,station.latitude))
        }
    }

    override fun setData(data: ArrayList<StationResponseItem>) {
        this.stationList = data
        notifyDataSetChanged()
    }
    fun defaultBg() {
        binding!!.stationRCVIremColor.setBackgroundResource(R.drawable.bg_capsule_unselected)
    }

    fun selectedBg() {
        binding!!.stationRCVIremColor.setBackgroundResource(R.drawable.bg_capsule_selected)
    }
}
