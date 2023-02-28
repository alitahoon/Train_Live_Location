package com.example.domain.entity

data class Location_Request(
    val latitude: Float,
    val longitude: Float,
    val trainId: Int,
    val userId: Int
)