package com.example.domain.entity

data class DoctorNotificationData(
    val title: String,
    val message: String,
    val latitude: Double,
    val longitude: Double
):java.io.Serializable