package com.example.domain.entity

data class CommentRequest(
    val adminId:Int?,
    val content: String,
    val img: String,
    val postId: Int,
    val userId: Int,
    val userName: String,
    val userPhone: String
)