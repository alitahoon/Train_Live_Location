package com.example.domain.entity

data class PostModelResponse(
    val adminId: Int,
    val content: String,
    val critical: Boolean,
    val id: Int,
    val img: String,
    val trainNumber: Int,
    val userId: Int
)