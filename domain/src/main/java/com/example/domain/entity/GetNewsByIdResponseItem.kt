package com.example.domain.entity

data class GetNewsByIdResponseItem(
    val contentOfPost: String,
    val date: String,
    val id: Int,
    val img: String,
    val title:String

)