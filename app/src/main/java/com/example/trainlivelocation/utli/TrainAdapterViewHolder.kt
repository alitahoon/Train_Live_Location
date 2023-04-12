package com.example.trainlivelocation.utli

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.TrainResponseItem
import com.example.trainlivelocation.databinding.TrainRcvItemLayoutBinding

class TrainAdapterViewHolder(
    private val binding:TrainRcvItemLayoutBinding,
    private val stationListener: Train_Dialog_Listener
) :RecyclerView.ViewHolder(binding.root){

    fun bind(train: TrainResponseItem) {
        binding.train = train
        binding.listener=stationListener

    }

}