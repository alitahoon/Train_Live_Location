package com.example.domain.entity

data class MessageNotificationData(
    val title: String,
    val message: String,
    val senderPhone: String,
    val reciverPhone: String,
    val senderUsername: String,
    val reciverUsername: String,

    ) : java.io.Serializable