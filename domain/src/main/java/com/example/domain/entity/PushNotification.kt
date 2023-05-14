package com.example.domain.entity

data class PushNotification(
    val data: DoctorNotificationData,
    val to: String
)