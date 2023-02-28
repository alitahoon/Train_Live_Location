package com.example.domain.entity

data class Location_Request_with_id(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val trainId: Int,
    val userId: Int
)