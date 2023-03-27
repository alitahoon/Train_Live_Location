package com.example.domain.entity

data class Post(
    val content: String,
    val trainNumber: Int,
    val critical: Boolean,
    val img: String,
    val userid:Int,
    val userPhone: String,
    val userName:String,
    val adminId:Int?
)
