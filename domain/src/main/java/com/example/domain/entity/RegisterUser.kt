package com.example.domain.entity

data class RegisterUser(
    val address: String,
    val birthDate: String,
    val email: String,
    val gender: String,
    val jop: String,
    val name: String,
    val password: String,
    val phone: String,
    val role: String,
    val tokenForNotifications:String,
    val image:String?
)