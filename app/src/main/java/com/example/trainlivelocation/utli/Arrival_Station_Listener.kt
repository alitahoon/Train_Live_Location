package com.example.trainlivelocation.utli

import com.example.domain.entity.Latitude
import com.example.domain.entity.LocationDetails

interface Arrival_Station_Listener {
    fun onArrivalStationSelected(StationId:Int?,StationName: String?,longitude:Double?,latitude:Double?)
}