package com.example.domain.entity

data class PushNotification(
    val data: NotificationData,
    val to: String,
    val latitude: Double,
    val longitude: Double
)