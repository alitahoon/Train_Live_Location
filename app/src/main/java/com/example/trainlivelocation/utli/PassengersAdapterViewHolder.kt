package com.example.trainlivelocation.utli


import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.*
import com.example.trainlivelocation.databinding.PassengersRcvItemLayoutBinding

class PassengersAdapterViewHolder(
    private val binding: PassengersRcvItemLayoutBinding,
    private val passengersListener: PassengersListener
    ) : RecyclerView.ViewHolder(binding.root) {
    private val TAG:String?="PassengersAdapterViewHolder"
    fun bind(passengers:UserInTrainResponseItem) {
        binding.passengers = passengers
        binding.listener = passengersListener
    }


}