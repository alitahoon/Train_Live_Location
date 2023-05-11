package com.example.domain.entity

data class CreateNewsResponseItem(
    val contentOfPost: String,
    val date: String,
    val id: Int,
    val img: String
)