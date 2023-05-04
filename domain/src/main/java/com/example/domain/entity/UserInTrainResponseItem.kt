package com.example.domain.entity

data class UserInTrainResponseItem(
    val arrivalStation: String,
    val jop: String,
    val takeOffStation: String,
    val ticketId: Int,
    val userEmail: String,
    val userId: Int,
    val userName: String,
    val userPhone: String
)