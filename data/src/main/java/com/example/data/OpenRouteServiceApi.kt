package com.example.data

import com.example.domain.entity.OpenRouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteServiceApi {
    @GET("v2/directions/driving-car")
    suspend fun getDirections(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Response<OpenRouteResponse>

    @GET("v2/directions/driving-car")
    suspend fun getDirectionsWayPoints(
        @Query("api_key") apiKey: String,
        @Query("coordinates") waypoints: String
    ): Response<OpenRouteResponse>
}