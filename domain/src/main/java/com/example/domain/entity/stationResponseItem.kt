package com.example.domain.entity

data class stationResponseItem(
    val description: String,
    val id: Int,
    val latitude: Int,
    val longitude: Int,
    val name: String,
    val nextStation: String,
    val trainId: Int
)