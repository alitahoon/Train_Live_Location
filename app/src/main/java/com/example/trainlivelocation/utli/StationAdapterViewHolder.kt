package com.example.trainlivelocation.utli

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.Resource
import com.example.domain.entity.StationResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.StationRcvItemLayoutBinding

class StationAdapterViewHolder(
    private val binding:StationRcvItemLayoutBinding,
    private val stationListener: Station_Dialog_Listener
) :RecyclerView.ViewHolder(binding.root){
    init {
        itemView.setOnClickListener {
            selectedItemPos = adapterPosition
            if(lastItemSelectedPos == -1)
                lastItemSelectedPos = selectedItemPos
            else {
                notifyItemChanged(lastItemSelectedPos)
                lastItemSelectedPos = selectedItemPos
            }
            notifyItemChanged(selectedItemPos)
        }
    }
    fun bind(station: StationResponseItem) {
        binding.station = station
        binding.listener=stationListener

    }
    fun defaultBg() {
        binding.stationRCVIremColor.setBackgroundResource(R.drawable.bg_capsule_unselected)
    }

    fun selectedBg() {
        binding.stationRCVIremColor.setBackgroundResource(R.drawable.bg_capsule_selected)
    }
}