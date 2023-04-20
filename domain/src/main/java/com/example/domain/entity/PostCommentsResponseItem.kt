package com.example.domain.entity

data class PostCommentsResponseItem(
    val content: String,
    val date: String,
    val id: Int,
    val img: String,
    val postId: Int,
    val userId: Int,
    val userName: String,
    val userPhone: String
)