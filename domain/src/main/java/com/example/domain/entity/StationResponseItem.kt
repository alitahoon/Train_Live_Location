package com.example.domain.entity

data class StationResponseItem(
    val description: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val nextStation: String,
    val trainId: Int
)