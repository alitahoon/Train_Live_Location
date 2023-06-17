package com.example.domain.entity

data class PushMessageNotification(
    val data: MessageNotificationData,
    val to: String
)