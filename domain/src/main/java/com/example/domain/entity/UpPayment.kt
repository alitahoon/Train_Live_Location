package com.example.domain.entity

import java.util.Date

data class UpPayment(
    val id: Int,
    val bankName: String,
    val cardNumber: String,
    val cost: Int,
    val date:Date

)
