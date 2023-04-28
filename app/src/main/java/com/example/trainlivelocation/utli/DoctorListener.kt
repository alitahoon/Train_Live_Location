package com.example.trainlivelocation.utli

import android.view.View
import com.example.domain.entity.*

interface DoctorListener {
     fun OnNotifyClickListener(doctor:DoctorResponseItem)
     fun OnChatClickListener(doctor:DoctorResponseItem)
}