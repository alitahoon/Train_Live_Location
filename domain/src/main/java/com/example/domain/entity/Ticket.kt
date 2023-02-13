package com.example.domain.entity

import java.util.Date

data class Ticket(
    val trainNumber: String,
    val takeOffDate: Date,
    val takeOffStation: String,
    val arrivalStation: String,
    val price: Int,
    val trainDegree: String,
    val userId: Int,
    val paymentId: Int
)
