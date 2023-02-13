package com.example.domain.entity

data class Post(


    val id: Int,
    val content: String,
    val trainNumber: Int,
    val critical: Boolean,
    val img: String,
    val userId: Int,
    val adminId:Int
)
