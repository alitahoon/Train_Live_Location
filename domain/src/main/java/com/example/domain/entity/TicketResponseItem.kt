package com.example.domain.entity

data class TicketResponseItem(
    val arrivalStation: String,
    val price: Int,
    val takeOffDate: String,
    val takeOffStation: String,
    val trainDegree: String,
    val trainId: Int,
    val trainNumber: String,
    val userId: Int
)