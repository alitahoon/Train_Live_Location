package com.example.trainlivelocation.utli

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.*
import com.example.trainlivelocation.databinding.DoctorRcvItemLayoutBinding

class DoctorAdapterViewHolder(
    private val binding: DoctorRcvItemLayoutBinding,
    private val doctorListener: DoctorListener
    ) : RecyclerView.ViewHolder(binding.root) {
    private val TAG:String?="DoctorAdapterViewHolder"
    fun bind(doctor: DoctorResponseItem) {
        binding.doctor = doctor
        binding.listener=doctorListener
    }


}