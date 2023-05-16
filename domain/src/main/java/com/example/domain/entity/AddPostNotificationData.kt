package com.example.domain.entity

data class AddPostNotificationData(
    val title: String,
    val message: String,
    val trainID: Int,
    val critical: Boolean
):java.io.Serializable