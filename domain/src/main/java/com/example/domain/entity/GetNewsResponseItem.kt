package com.example.domain.entity

data class GetNewsResponseItem(
    val contentOfPost: String,
    val date: String,
    val id: Int,
    val img: String,
    val title:String
)