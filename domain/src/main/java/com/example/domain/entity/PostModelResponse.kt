package com.example.domain.entity

data class PostModelResponse(
    val adminId: Int,
    val content: String,
    val critical: Boolean,
    val date: String,
    val id: Int,
    val img: String,
    val imgId: String,
    val trainNumber: Int,
    val userId: Int,
    val userName: String,
    val userPhone: String
)