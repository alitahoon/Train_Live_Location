package com.example.domain.entity

data class UpdateUser(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val jop: String,
    val address: String,
    val gender: String,
    val birthDate: String,
    val role:String
)
