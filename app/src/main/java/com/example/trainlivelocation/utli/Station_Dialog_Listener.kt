package com.example.trainlivelocation.utli

import com.example.domain.entity.LocationDetails

interface Station_Dialog_Listener {
    fun onStationSelected(StationId:Int?,StationName: String?,StationLocation:LocationDetails?)
}