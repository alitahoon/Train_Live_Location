package com.example.trainlivelocation.utli

import com.example.domain.entity.Latitude
import com.example.domain.entity.LocationDetails

interface Train_Dialog_Listener {
    fun onTrainSelected(trainId:Int?,trainDegree: String?)
}