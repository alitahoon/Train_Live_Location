package com.example.domain.entity

data class TicketRequestItem(
    val arrivalStation: String,
    val price: Double,
    val takeOffDate: String,
    val takeOffStation: String,
    val trainDegree: String,
    val trainId: Int,
    val trainNumber: String,
    val userId: Int
)