package com.example.trainlivelocation.utli

import com.example.domain.entity.Latitude
import com.example.domain.entity.LocationDetails

interface Station_Dialog_Listener {
    fun onStationSelected(StationId:Int?,StationName: String?,longitude:Double?,latitude:Double?)
}