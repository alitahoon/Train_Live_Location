package com.example.data

import com.example.domain.entity.Coordinates
import com.example.domain.entity.DirectionResponse
import com.example.domain.entity.OpenRouteResponse
import retrofit2.Response
import retrofit2.http.*
interface OpenRouteServiceApi {
    @GET("v2/directions/driving-car")
    suspend fun getDirections(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Response<OpenRouteResponse>
    @POST("v2/directions/driving-car")
    suspend fun getDirectionsWayPoints(
        @Header("Authorization") authorization: String,
        @Header("Accept") Accept: String,
        @Header("Content-Type") Content_Type: String,
        @Body coordinates: Coordinates
    ): Response<DirectionResponse>
}