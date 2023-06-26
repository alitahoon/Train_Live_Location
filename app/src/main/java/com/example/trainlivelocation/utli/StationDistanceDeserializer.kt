package com.example.trainlivelocation.utli

import com.example.domain.entity.StationDistanceModel
import com.example.domain.entity.StationResponseItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class StationDistanceDeserializer : JsonDeserializer<StationDistanceModel> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): StationDistanceModel? {
        val jsonObject = json?.asJsonObject
        val distance = jsonObject?.get("distance")?.asDouble
        val stationJson = jsonObject?.get("station")?.asJsonObject
        val position = stationJson?.get("Postion")?.asInt  // Handle field name "Postion"
        val description = stationJson?.get("description")?.asString
        val id = stationJson?.get("id")?.asInt
        val latitude = stationJson?.get("latitude")?.asDouble
        val longitude = stationJson?.get("longitude")?.asDouble
        val name = stationJson?.get("name")?.asString
        val nextStation = stationJson?.get("nextStation")?.asString
        val nextStationPosition = stationJson?.get("nextStationPostion")?.asInt  // Handle field name "nextStationPostion"
        val railwayId = stationJson?.get("railwayId")?.asInt  // Handle field name "railwayId"

        return StationDistanceModel(

            StationResponseItem(
                description!!,
                id!!,
                latitude!!,
                longitude!!,
                name!!,
                nextStation!!,
                position!!,
                railwayId!!,
                nextStationPosition!!

            ),
            distance ?: 0.0
        )
    }
}