package com.example.domain.entity

data class DoctorResponseItem(
    val arrivalStation: String,
    val takeOffStation: String,
    val ticketId: Int,
    val userEmail: String,
    val userId: Int,
    val userName: String,
    val userPhone: String
){
}