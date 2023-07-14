package com.example.domain.entity

data class PhoneTokenResponse(
    val phone: String,
    val tokenForNotifications: String
)